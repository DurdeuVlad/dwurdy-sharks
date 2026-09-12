package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BenssharksModPotions {
   public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, "benssharks");
   public static final DeferredHolder<Potion, Potion> POTION_OF_SEALING = REGISTRY.register(
      "potion_of_sealing", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(BenssharksModMobEffects.SEALING, 3600, 0, false, true)})
   );
   public static final DeferredHolder<Potion, Potion> FRENZY_POTION = REGISTRY.register(
      "frenzy_potion", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(BenssharksModMobEffects.FRENZY, 3600, 0, false, true)})
   );
   public static final DeferredHolder<Potion, Potion> JAWS_POTION = REGISTRY.register(
      "jaws_potion", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(BenssharksModMobEffects.JAWS, 3600, 0, false, true)})
   );
}
