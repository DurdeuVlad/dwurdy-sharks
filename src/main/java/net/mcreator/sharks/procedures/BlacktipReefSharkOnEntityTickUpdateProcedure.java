package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class BlacktipReefSharkOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof BlacktipReefSharkEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity, 600, 600);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
