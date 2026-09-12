package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class ShrakOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double speed = 0.0;
         double Yaw = 0.0;
         if (entity instanceof ShrakEntity && !entity.isInWaterOrBubble()) {
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
