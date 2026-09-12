package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BonnetheadSharkEntityVisualScaleProcedure {
   public static double execute(Entity entity) {
      if (entity == null) {
         return 0.0;
      } else {
         return entity instanceof BonnetheadSharkEntity && entity instanceof LivingEntity _livEnt1 && _livEnt1.isBaby() ? 0.5 : 1.0;
      }
   }
}
