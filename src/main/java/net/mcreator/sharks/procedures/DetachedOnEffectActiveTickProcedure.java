package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DetachedOnEffectActiveTickProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(DwurdySharksModMobEffects.DETACHED) && entity instanceof LivingEntity _entity) {
            _entity.removeEffect(DwurdySharksModMobEffects.PARASITE);
         }
      }
   }
}
