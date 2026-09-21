package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.NurseSharkEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

public class RightClickSpeedProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if ((entity instanceof TamableAnimal _tamEntx ? _tamEntx.getOwner() : null) instanceof Player
            && entity instanceof NurseSharkEntity
            && entity instanceof TamableAnimal _tamIsTamedBy
            && (entity instanceof TamableAnimal _tamEnt ? _tamEnt.getOwner() : null) instanceof LivingEntity _livEnt
            && _tamIsTamedBy.isOwnedBy(_livEnt)
            && entity.isInWaterOrBubble()) {
            if (entity instanceof NurseSharkEntity) {
               ((NurseSharkEntity)entity).setAnimation("sprint");
            }

            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1, true, false));
            }

            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, true, false));
            }
         }
      }
   }
}
