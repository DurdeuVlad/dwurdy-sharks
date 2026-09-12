package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class RollParticleOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity.isInWaterOrBubble()) {
            BenssharksMod.queueServerWork(60, () -> {
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            });
         }

         if (!entity.isVehicle() && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
