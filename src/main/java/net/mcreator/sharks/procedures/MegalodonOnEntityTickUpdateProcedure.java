package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.MegalodonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class MegalodonOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof MegalodonEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }
      }
   }
}
