package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.NurseSharkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class NurseSharkOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double velocityY = 0.0;
         double velocityX = 0.0;
         double velocityZ = 0.0;
         double dis = 0.0;
         if (entity instanceof NurseSharkEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity, 600, 600);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
