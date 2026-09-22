package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.ShrakEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class ShrakOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double speed = 0.0;
         double Yaw = 0.0;
         if (entity instanceof ShrakEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
