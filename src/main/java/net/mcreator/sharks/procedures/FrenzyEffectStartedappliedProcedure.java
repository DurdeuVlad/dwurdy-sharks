package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.mcreator.sharks.network.DwurdySharksModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.common.NeoForgeMod;

public class FrenzyEffectStartedappliedProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double SwimSpeed = 0.0;
         double KBRes = 0.0;
         DwurdySharksModVariables.MapVariables.get(world).SwimSpeed = entity instanceof LivingEntity _livingEntity0
               && _livingEntity0.getAttributes().hasAttribute(NeoForgeMod.SWIM_SPEED)
            ? _livingEntity0.getAttribute(NeoForgeMod.SWIM_SPEED).getBaseValue()
            : 0.0;
         DwurdySharksModVariables.MapVariables.get(world).syncData(world);
         DwurdySharksModVariables.MapVariables.get(world).KBRes = entity instanceof LivingEntity _livingEntity1
               && _livingEntity1.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
            ? _livingEntity1.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue()
            : 0.0;
         DwurdySharksModVariables.MapVariables.get(world).syncData(world);
         if (entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(NeoForgeMod.SWIM_SPEED)) {
            _livingEntity4.getAttribute(NeoForgeMod.SWIM_SPEED)
               .setBaseValue(
                  (
                        entity instanceof LivingEntity _livingEntity2 && _livingEntity2.getAttributes().hasAttribute(NeoForgeMod.SWIM_SPEED)
                           ? _livingEntity2.getAttribute(NeoForgeMod.SWIM_SPEED).getBaseValue()
                           : 0.0
                     )
                     + (
                           entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(DwurdySharksModMobEffects.FRENZY)
                              ? _livEnt.getEffect(DwurdySharksModMobEffects.FRENZY).getAmplifier()
                              : 0
                        )
                        * 2
               );
         }

         if (entity instanceof LivingEntity _livingEntity7 && _livingEntity7.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)) {
            _livingEntity7.getAttribute(Attributes.KNOCKBACK_RESISTANCE)
               .setBaseValue(
                  (
                        entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
                           ? _livingEntity5.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue()
                           : 0.0
                     )
                     + (
                           entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(DwurdySharksModMobEffects.FRENZY)
                              ? _livEnt.getEffect(DwurdySharksModMobEffects.FRENZY).getAmplifier()
                              : 0
                        )
                        * 2
               );
         }
      }
   }
}
