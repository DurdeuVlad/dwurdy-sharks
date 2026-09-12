package net.mcreator.sharks.potion;

import net.mcreator.sharks.procedures.SealingPotionOnEffectActiveTickProcedure;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SealingPotionMobEffect extends MobEffect {
   public SealingPotionMobEffect() {
      super(MobEffectCategory.BENEFICIAL, -3230104);
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      SealingPotionOnEffectActiveTickProcedure.execute(entity);
      return super.applyEffectTick(entity, amplifier);
   }
}
