package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DetachedOnEffectActiveTickProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(BenssharksModMobEffects.DETACHED) && entity instanceof LivingEntity _entity) {
            _entity.removeEffect(BenssharksModMobEffects.PARASITE);
         }
      }
   }
}
