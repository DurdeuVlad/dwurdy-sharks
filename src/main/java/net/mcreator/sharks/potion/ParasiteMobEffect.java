package net.mcreator.sharks.potion;

import net.mcreator.sharks.procedures.ParasiteEffectStartedappliedProcedure;
import net.mcreator.sharks.procedures.ParasiteOnEffectActiveTickProcedure;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ParasiteMobEffect extends MobEffect {
   public ParasiteMobEffect() {
      super(MobEffectCategory.HARMFUL, -10340296);
   }

   public void onEffectStarted(LivingEntity entity, int amplifier) {
      ParasiteEffectStartedappliedProcedure.execute(entity.level(), entity);
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      ParasiteOnEffectActiveTickProcedure.execute(entity.level(), entity);
      return super.applyEffectTick(entity, amplifier);
   }
}
