package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class BarracudaOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof BarracudaEntity && !entity.isInWaterOrBubble()) {
            if (entity instanceof BarracudaEntity) {
               ((BarracudaEntity)entity).setAnimation("land");
            }

            BenssharksMod.queueServerWork(600, () -> {
               if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                  _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.DRYOUT_EFFECT, 600, 0, true, false));
               }
            });
         } else if (entity.isInWaterRainOrBubble() && entity instanceof LivingEntity _entity) {
            _entity.removeEffect(BenssharksModMobEffects.DRYOUT_EFFECT);
         }

         if (entity instanceof BarracudaEntity
            && !entity.isInWaterRainOrBubble()
            && entity instanceof LivingEntity _livEnt9
            && _livEnt9.hasEffect(BenssharksModMobEffects.FRENZY)
            && entity instanceof BarracudaEntity) {
            ((BarracudaEntity)entity).setAnimation("sprint");
         }
      }
   }
}
