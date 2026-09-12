package net.mcreator.sharks.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class NurseSharkPlayerCollidesWithThisEntityProcedure {
   public static void execute(Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (entity instanceof TamableAnimal _tamIsTamedBy
            && sourceentity instanceof LivingEntity _livEnt
            && _tamIsTamedBy.isOwnedBy(_livEnt)
            && sourceentity instanceof LivingEntity _entity
            && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));
         }
      }
   }
}
