package net.mcreator.sharks.procedures;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

final class DroppedFoodProcedure {
   private DroppedFoodProcedure() {
   }

   static void execute(LevelAccessor world, double x, double y, double z, Entity entity,
      Predicate<ItemStack> extraItemFilter, float healAmount) {
      Vec3 center = new Vec3(x, y, z);
      List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(center, 8.0, 8.0, 8.0),
         e -> e.getItem().has(DataComponents.FOOD) && extraItemFilter.test(e.getItem()));
      ItemEntity nearest = nearest(items, x, y, z);
      if (nearest == null) {
         return;
      }
      if (entity instanceof Mob mob) {
         mob.getNavigation().moveTo(nearest.getX(), nearest.getY(), nearest.getZ(), 1.0);
      }
      if (!nearest.getBoundingBox().intersects(AABB.ofSize(center, 2.5, 2.5, 2.5))) {
         return;
      }
      ItemStack stack = nearest.getItem();
      if (entity instanceof LivingEntity living && !living.level().isClientSide()) {
         if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3, true, true));
            living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1, true, true));
            living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0, true, true));
            living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0, true, true));
         } else if (stack.getItem() == Items.GOLDEN_APPLE) {
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0, true, true));
            living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, true));
         }
      }
      if (entity instanceof LivingEntity living) {
         living.swing(InteractionHand.MAIN_HAND, true);
      }
      if (!world.isClientSide()) {
         nearest.discard();
      }
      if (world instanceof Level level) {
         SoundEvent sound = (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.eat"));
         if (!level.isClientSide()) {
            level.playSound(null, BlockPos.containing(x, y, z), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
         } else {
            level.playLocalSound(x, y, z, sound, SoundSource.NEUTRAL, 1.0F, 1.0F, false);
         }
      }
      if (healAmount > 0.0F && entity instanceof LivingEntity living) {
         living.setHealth(living.getHealth() + healAmount);
      }
   }

   private static <T extends Entity> T nearest(List<T> entities, double x, double y, double z) {
      T best = null;
      double bestDist = Double.MAX_VALUE;
      for (T e : entities) {
         double d = e.distanceToSqr(x, y, z);
         if (d < bestDist) {
            bestDist = d;
            best = e;
         }
      }
      return best;
   }
}
