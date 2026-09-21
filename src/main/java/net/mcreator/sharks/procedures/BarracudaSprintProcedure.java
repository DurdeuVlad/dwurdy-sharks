package net.mcreator.sharks.procedures;

import java.util.List;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class BarracudaSprintProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      Entity entity = event.getEntity();
      if (!(entity instanceof BarracudaEntity)) {
         return;
      }
      if ((entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!(entity instanceof BarracudaEntity) || !entity.isInWaterOrBubble()) {
         return;
      }
      Vec3 center = new Vec3(x, y, z);
      AABB farBox = AABB.ofSize(center, 16.0, 16.0, 16.0);
      AABB midBox = AABB.ofSize(center, 12.0, 12.0, 12.0);
      AABB closeBox = AABB.ofSize(center, 6.0, 6.0, 6.0);

      List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class, farBox, e -> e != entity);
      boolean frenzy = false;
      boolean playerNearby = false;
      for (LivingEntity candidate : nearby) {
         var type = candidate.getType();
         if (type.is(DwurdySharksEntityTypeTags.BARRACUDA_FRENZY_CLOSE)) {
            if (candidate.getBoundingBox().intersects(closeBox)) frenzy = true;
         } else if (type.is(DwurdySharksEntityTypeTags.BARRACUDA_FRENZY_MID)) {
            if (candidate.getBoundingBox().intersects(midBox)) frenzy = true;
         } else if (type.is(DwurdySharksEntityTypeTags.BARRACUDA_FRENZY_FAR)) {
            frenzy = true;
         }
         if (candidate instanceof Player) {
            playerNearby = true;
         }
      }
      if (!frenzy
         && entity instanceof Mob mob
         && mob.getTarget() instanceof Player target
         && playerNearby
         && !target.getAbilities().instabuild
         && target.isInWaterOrBubble()) {
         frenzy = true;
      }

      if (frenzy) {
         if (entity instanceof LivingEntity living && !living.level().isClientSide()) {
            living.addEffect(new MobEffectInstance(BenssharksModMobEffects.FRENZY, 60, 2, true, false));
         }
         ((BarracudaEntity)entity).setAnimation("sprint");
      } else {
         ((BarracudaEntity)entity).setAnimation("empty");
         if (entity instanceof LivingEntity living) {
            living.removeEffect(BenssharksModMobEffects.FRENZY);
         }
      }
   }
}
