package net.mcreator.sharks.procedures;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;

public class FrenzyOnEffectActiveTickProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      if (world instanceof ServerLevel _level) {
         _level.sendParticles(ParticleTypes.DOLPHIN, x, y, z, 5, 0.15, 0.0, 0.15, 2.0);
      }
   }
}
