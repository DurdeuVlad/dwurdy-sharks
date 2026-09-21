package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
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

            DryoutProcedure.dryTick(entity, 600, 600);
         } else if (entity.isInWaterRainOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
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
