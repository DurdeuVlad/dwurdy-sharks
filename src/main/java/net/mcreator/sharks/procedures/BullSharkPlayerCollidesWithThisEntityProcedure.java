package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BullSharkPlayerCollidesWithThisEntityProcedure {
   public static void execute(Entity sourceentity) {
      if (sourceentity != null) {
         if (sourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.PREY, 60, 0, true, false));
         }
      }
   }
}
