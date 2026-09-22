package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.MakoSharkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class MakoSharkOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof MakoSharkEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
