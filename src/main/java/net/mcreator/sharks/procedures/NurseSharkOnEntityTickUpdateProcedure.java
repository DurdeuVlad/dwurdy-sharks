package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class NurseSharkOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double velocityY = 0.0;
         double velocityX = 0.0;
         double velocityZ = 0.0;
         double dis = 0.0;
         if (entity instanceof NurseSharkEntity && !entity.isInWaterOrBubble()) {
            BenssharksMod.queueServerWork(600, () -> {
               if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                  _entityx.addEffect(new MobEffectInstance(BenssharksModMobEffects.DRYOUT_EFFECT, 600, 0, true, false));
               }
            });
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            _entity.removeEffect(BenssharksModMobEffects.DRYOUT_EFFECT);
         }
      }
   }
}
