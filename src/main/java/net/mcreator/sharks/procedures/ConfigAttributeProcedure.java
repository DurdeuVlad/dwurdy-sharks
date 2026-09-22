package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public final class ConfigAttributeProcedure {
   private static final ResourceLocation SPEED_MODIFIER_ID =
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "config_speed");
   private static final ResourceLocation HEALTH_MODIFIER_ID =
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "config_health");
   private static final ResourceLocation AGGRO_RANGE_MODIFIER_ID =
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "config_aggro_range");

   private ConfigAttributeProcedure() {
   }

   @SubscribeEvent
   public static void onEntityJoin(EntityJoinLevelEvent event) {
      if (event.getLevel().isClientSide()
         || !(event.getEntity() instanceof LivingEntity living)
         || !DwurdySharksConfig.isModEntity(living.getType())) {
         return;
      }
      applySpeed(living);
      applyHealth(living);
      applyAggroRange(living);
   }

   private static void applySpeed(LivingEntity living) {
      AttributeInstance speed = living.getAttribute(Attributes.MOVEMENT_SPEED);
      if (speed == null) {
         return;
      }
      double multiplier = DwurdySharksConfig.speedMultiplier(living.getType());
      if (multiplier == 1.0) {
         speed.removeModifier(SPEED_MODIFIER_ID);
      } else {
         speed.addOrUpdateTransientModifier(
            new AttributeModifier(SPEED_MODIFIER_ID, multiplier - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
      }
   }

   private static void applyHealth(LivingEntity living) {
      AttributeInstance health = living.getAttribute(Attributes.MAX_HEALTH);
      if (health == null) {
         return;
      }
      double override = DwurdySharksConfig.healthOverride(living.getType());
      if (override <= 0.0) {
         health.removeModifier(HEALTH_MODIFIER_ID);
         return;
      }
      health.addOrUpdateTransientModifier(
         new AttributeModifier(HEALTH_MODIFIER_ID, override - health.getBaseValue(), AttributeModifier.Operation.ADD_VALUE));
      if (living.getHealth() > living.getMaxHealth()) {
         living.setHealth(living.getMaxHealth());
      }
   }

   private static void applyAggroRange(LivingEntity living) {
      AttributeInstance range = living.getAttribute(Attributes.FOLLOW_RANGE);
      if (range == null) {
         return;
      }
      double multiplier = DwurdySharksConfig.AGGRO_FOLLOW_RANGE_MULTIPLIER.get();
      if (multiplier == 1.0) {
         range.removeModifier(AGGRO_RANGE_MODIFIER_ID);
      } else {
         range.addOrUpdateTransientModifier(
            new AttributeModifier(AGGRO_RANGE_MODIFIER_ID, multiplier - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
      }
   }
}
