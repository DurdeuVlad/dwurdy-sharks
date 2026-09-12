package net.mcreator.sharks.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class NurseSharkEntityVisualScaleProcedure {
   public static double execute(Entity entity) {
      if (entity == null) {
         return 0.0;
      } else {
         return entity instanceof LivingEntity _livEnt0 && _livEnt0.isBaby() ? 0.5 : 1.0;
      }
   }
}
