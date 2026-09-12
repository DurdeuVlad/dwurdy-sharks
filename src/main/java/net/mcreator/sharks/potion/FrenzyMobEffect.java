package net.mcreator.sharks.potion;

import net.mcreator.sharks.procedures.FrenzyEffectStartedappliedProcedure;
import net.mcreator.sharks.procedures.FrenzyOnEffectActiveTickProcedure;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrenzyMobEffect extends MobEffect {
   public FrenzyMobEffect() {
      super(MobEffectCategory.BENEFICIAL, -65536);
   }

   public void onEffectStarted(LivingEntity entity, int amplifier) {
      FrenzyEffectStartedappliedProcedure.execute(entity.level(), entity);
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      FrenzyOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
      return super.applyEffectTick(entity, amplifier);
   }
}
