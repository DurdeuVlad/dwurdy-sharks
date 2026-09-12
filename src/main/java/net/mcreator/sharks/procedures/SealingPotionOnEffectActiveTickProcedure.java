package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SealingPotionOnEffectActiveTickProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof LivingEntity _entity) {
            _entity.removeEffect(BenssharksModMobEffects.BLEEDING);
         }

         if (entity instanceof LivingEntity _entity) {
            _entity.removeEffect(BenssharksModMobEffects.PARASITE);
         }

         if (entity instanceof LivingEntity _entity) {
            _entity.removeEffect(MobEffects.WITHER);
         }

         if (entity instanceof LivingEntity _entity) {
            _entity.removeEffect(MobEffects.POISON);
         }
      }
   }
}
