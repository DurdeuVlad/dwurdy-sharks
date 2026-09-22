package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MakoSprintProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity == null || (entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      if (!(entity instanceof MakoSharkEntity) || !entity.isInWaterOrBubble()) {
         return;
      }
      if (entity instanceof Mob mob && mob.getTarget() instanceof BarracudaEntity target && target.isAlive()) {
         boolean near = target.getBoundingBox().intersects(AABB.ofSize(new Vec3(x, y, z), 24.0, 24.0, 24.0));
         if (near && entity instanceof LivingEntity living && !living.level().isClientSide()) {
            living.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FRENZY, 60, 2, true, false));
         } else if (!near && entity instanceof LivingEntity living) {
            living.removeEffect(DwurdySharksModMobEffects.FRENZY);
         }
      }

      if (entity instanceof LivingEntity living && living.hasEffect(DwurdySharksModMobEffects.FRENZY)) {
         ((MakoSharkEntity)entity).setAnimation("sprint");
      } else {
         ((MakoSharkEntity)entity).setAnimation("empty");
      }
   }
}
