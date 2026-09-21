package net.mcreator.sharks.procedures;

import java.util.List;
import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.mcreator.sharks.entity.KrillEntity;
import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class EatKrillProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      Entity entity = event.getEntity();
      if (!(entity instanceof BaskingSharkEntity) && !(entity instanceof WhaleSharkEntity)) {
         return;
      }
      if ((entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!(entity instanceof BaskingSharkEntity || entity instanceof WhaleSharkEntity)) {
         return;
      }
      Vec3 center = new Vec3(x, y, z);
      AABB seekBox = AABB.ofSize(center, 8.0, 8.0, 8.0);
      AABB eatBox = AABB.ofSize(center, 3.0, 3.0, 3.0);

      List<KrillEntity> krill = world.getEntitiesOfClass(KrillEntity.class, seekBox, Entity::isAlive);
      KrillEntity nearestKrill = nearest(krill, x, y, z);
      if (nearestKrill != null) {
         if (entity instanceof Mob mob) {
            mob.getNavigation().moveTo(nearestKrill.getX(), nearestKrill.getY(), nearestKrill.getZ(), 1.0);
         }
         if (nearestKrill.getBoundingBox().intersects(eatBox)) {
            if (!world.isClientSide()) {
               nearestKrill.discard();
            }
            playEatSound(world, entity);
            if (entity instanceof LivingEntity living) {
               living.setHealth(living.getHealth() + 1.0F);
            }
         }
      }

      List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, seekBox,
         e -> e.getItem().getItem() == BenssharksModItems.KRILL_ITEM.get());
      ItemEntity nearestItem = nearest(items, x, y, z);
      if (nearestItem != null) {
         if (entity instanceof Mob mob) {
            mob.getNavigation().moveTo(nearestItem.getX(), nearestItem.getY(), nearestItem.getZ(), 1.0);
         }
         if (nearestItem.getBoundingBox().intersects(eatBox)) {
            if (!world.isClientSide()) {
               nearestItem.discard();
            }
            playEatSound(world, entity);
            if (entity instanceof LivingEntity living) {
               living.setHealth(living.getHealth() + 1.0F);
            }
         }
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

   private static void playEatSound(LevelAccessor world, Entity entity) {
      if (world instanceof Level level) {
         SoundEvent sound = (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.panda.bite"));
         if (!level.isClientSide()) {
            level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
         } else {
            level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F, false);
         }
      }
   }
}
