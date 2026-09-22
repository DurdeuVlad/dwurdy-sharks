package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.LandSharkEntity;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LandSharkOnEntityTickUpdateProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         double Chain = 0.0;
         double ChainWait = 0.0;
         if (entity.isShiftKeyDown()) {
            if (entity instanceof LandSharkEntity) {
               ((LandSharkEntity)entity).setAnimation("sit");
            }
         } else if (!entity.isShiftKeyDown() && entity instanceof LandSharkEntity) {
            ((LandSharkEntity)entity).setAnimation("empty");
         }

         if (entity instanceof LandSharkEntity && entity.isInWaterOrBubble()) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FRENZY, 20, 3, true, false));
            }

            if (entity instanceof LandSharkEntity) {
               ((LandSharkEntity)entity).setAnimation("swim");
            }
         } else if (entity instanceof LandSharkEntity && !entity.isInWaterOrBubble() && entity instanceof LandSharkEntity) {
            ((LandSharkEntity)entity).setAnimation("empty");
         }
      }
   }
}
