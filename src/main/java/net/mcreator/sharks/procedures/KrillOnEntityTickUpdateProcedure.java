package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.KrillEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class KrillOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof KrillEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity);
         } else if (entity.isInWaterRainOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
