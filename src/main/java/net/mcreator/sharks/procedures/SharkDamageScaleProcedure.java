package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public final class SharkDamageScaleProcedure {
   private static final ResourceKey<DamageType> MEGALODON_BITE = ResourceKey.create(
      Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("dwurdysharks", "megalodon_bite"));

   private SharkDamageScaleProcedure() {
   }

   @SubscribeEvent
   public static void onIncomingDamage(LivingIncomingDamageEvent event) {
      Entity attacker = event.getSource().getEntity();
      if (attacker == null || !attacker.getType().is(DwurdySharksEntityTypeTags.SHARKS)) {
         return;
      }
      if (event.getSource().is(MEGALODON_BITE)) {
         return;
      }
      double multiplier = DwurdySharksConfig.SHARK_DAMAGE_MULTIPLIER.get();
      if (attacker.getType() == DwurdySharksModEntities.MEGALODON.get()) {
         float damage = (float)(DwurdySharksConfig.MEGALODON_DAMAGE.get() * multiplier);
         if (DwurdySharksConfig.MEGALODON_ARMOR_BYPASS.get()) {
            event.setCanceled(true);
            DamageSource bypass = new DamageSource(
               event.getEntity().level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(MEGALODON_BITE),
               attacker, attacker);
            event.getEntity().hurt(bypass, damage);
            return;
         }
         event.setAmount(damage);
      } else {
         event.setAmount(event.getAmount() * (float)multiplier);
      }
   }
}
