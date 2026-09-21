package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class BullSharkEntityIsHurtProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof BullSharkEntity && entity.isInWaterOrBubble()) {
            if (entity instanceof BullSharkEntity) {
               ((BullSharkEntity)entity).setAnimation("sprint");
            }

            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, true, false));
            }

            BenssharksMod.queueServerWork(600, () -> {

               if (entity instanceof BullSharkEntity) {
                  ((BullSharkEntity)entity).setAnimation("empty");
               }
            });
         }
      }
   }
}
