package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BenssharksModSounds {
   public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, "benssharks");
   public static final DeferredHolder<SoundEvent, SoundEvent> AXODILE_BITE = REGISTRY.register(
      "axodile.bite", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("benssharks", "axodile.bite"))
   );
   public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_CHOMP = REGISTRY.register(
      "shark.chomp", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("benssharks", "shark.chomp"))
   );
   public static final DeferredHolder<SoundEvent, SoundEvent> SQUEAK = REGISTRY.register(
      "squeak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("benssharks", "squeak"))
   );
   public static final DeferredHolder<SoundEvent, SoundEvent> CLUB_HIT = REGISTRY.register(
      "club_hit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("benssharks", "club_hit"))
   );
}
