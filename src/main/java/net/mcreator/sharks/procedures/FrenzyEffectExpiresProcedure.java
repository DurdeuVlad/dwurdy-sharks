package net.mcreator.sharks.procedures;

import net.mcreator.sharks.network.DwurdySharksModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.common.NeoForgeMod;

public class FrenzyEffectExpiresProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double SwimSpeed = 0.0;
         double KBRes = 0.0;
         if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(NeoForgeMod.SWIM_SPEED)) {
            _livingEntity0.getAttribute(NeoForgeMod.SWIM_SPEED).setBaseValue(DwurdySharksModVariables.MapVariables.get(world).SwimSpeed);
         }

         if (entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)) {
            _livingEntity1.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(DwurdySharksModVariables.MapVariables.get(world).KBRes);
         }
      }
   }
}
