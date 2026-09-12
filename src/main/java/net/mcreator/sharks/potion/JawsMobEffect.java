package net.mcreator.sharks.potion;

import net.mcreator.sharks.procedures.JawsOnEffectActiveTickProcedure;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class JawsMobEffect extends MobEffect {
   public JawsMobEffect() {
      super(MobEffectCategory.BENEFICIAL, -7491127);
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      JawsOnEffectActiveTickProcedure.execute(entity);
      return super.applyEffectTick(entity, amplifier);
   }
}
