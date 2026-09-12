package net.mcreator.sharks.init;

import net.mcreator.sharks.potion.BleedingMobEffect;
import net.mcreator.sharks.potion.DetachedMobEffect;
import net.mcreator.sharks.potion.DryoutEffectMobEffect;
import net.mcreator.sharks.potion.FertilizedMobEffect;
import net.mcreator.sharks.potion.FrenzyMobEffect;
import net.mcreator.sharks.potion.JawsMobEffect;
import net.mcreator.sharks.potion.ParasiteMobEffect;
import net.mcreator.sharks.potion.PreyMobEffect;
import net.mcreator.sharks.potion.SealingPotionMobEffect;
import net.mcreator.sharks.procedures.FertilizedEffectExpiresProcedure;
import net.mcreator.sharks.procedures.FrenzyEffectExpiresProcedure;
import net.mcreator.sharks.procedures.JawsEffectExpiresProcedure;
import net.mcreator.sharks.procedures.ParasiteEffectExpiresProcedure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class BenssharksModMobEffects {
   public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, "benssharks");
   public static final DeferredHolder<MobEffect, MobEffect> BLEEDING = REGISTRY.register("bleeding", () -> new BleedingMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> JAWS = REGISTRY.register("jaws", () -> new JawsMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> DRYOUT_EFFECT = REGISTRY.register("dryout_effect", () -> new DryoutEffectMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> SEALING = REGISTRY.register("sealing", () -> new SealingPotionMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> PARASITE = REGISTRY.register("parasite", () -> new ParasiteMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> DETACHED = REGISTRY.register("detached", () -> new DetachedMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> PREY = REGISTRY.register("prey", () -> new PreyMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> FRENZY = REGISTRY.register("frenzy", () -> new FrenzyMobEffect());
   public static final DeferredHolder<MobEffect, MobEffect> FERTILIZED = REGISTRY.register("fertilized", () -> new FertilizedMobEffect());

   @SubscribeEvent
   public static void onEffectRemoved(Remove event) {
      MobEffectInstance effectInstance = event.getEffectInstance();
      if (effectInstance != null) {
         expireEffects(event.getEntity(), effectInstance);
      }
   }

   @SubscribeEvent
   public static void onEffectExpired(Expired event) {
      MobEffectInstance effectInstance = event.getEffectInstance();
      if (effectInstance != null) {
         expireEffects(event.getEntity(), effectInstance);
      }
   }

   private static void expireEffects(Entity entity, MobEffectInstance effectInstance) {
      if (effectInstance.getEffect().is(JAWS)) {
         JawsEffectExpiresProcedure.execute();
      } else if (effectInstance.getEffect().is(PARASITE)) {
         ParasiteEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
      } else if (effectInstance.getEffect().is(FRENZY)) {
         FrenzyEffectExpiresProcedure.execute(entity.level(), entity);
      } else if (effectInstance.getEffect().is(FERTILIZED)) {
         FertilizedEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
      }
   }
}
