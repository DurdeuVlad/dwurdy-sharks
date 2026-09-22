package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class DryoutEffectOnEffectActiveTickProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!entity.isInWaterRainOrBubble()) {
            entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.DRY_OUT)), 1.0F);
         } else if (entity.isInWaterRainOrBubble() && entity instanceof LivingEntity _entity) {
            _entity.removeEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT);
         }
      }
   }
}
