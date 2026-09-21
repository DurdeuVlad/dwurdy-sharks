package net.mcreator.sharks.init;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class DwurdySharksConfig {
   private DwurdySharksConfig() {
   }

   public static final ModConfigSpec SPEC;
   public static final ModConfigSpec.BooleanValue SPAWNING_ENABLED;
   public static final ModConfigSpec.BooleanValue OCEAN_ONLY;
   public static final ModConfigSpec.DoubleValue SHARK_DAMAGE_MULTIPLIER;
   public static final ModConfigSpec.DoubleValue MEGALODON_DAMAGE;
   public static final ModConfigSpec.BooleanValue MEGALODON_ARMOR_BYPASS;
   private static final Map<String, ModConfigSpec.BooleanValue> SPECIES_FLAGS = new HashMap<>();

   static {
      ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
      builder.comment(
         "Dwurdy Sharks server configuration. Gamerules remain the per-world runtime knobs (caps, aggression);",
         "this file holds global balance and master toggles. Edits take effect on /reload or restart for new",
         "spawn checks and new damage applications."
      ).push("spawning");
      SPAWNING_ENABLED = builder
         .comment("Master switch for natural dwurdysharks spawns. False cancels natural spawns of every mod species;",
            "manual spawns (/summon, spawn egg, dispenser, bucket, breeding) still work as an admin escape hatch.")
         .define("spawningEnabled", true);
      OCEAN_ONLY = builder
         .comment("True restricts water spawn predicates to biomes in the dwurdysharks:shark_spawning_oceans biome tag.",
            "False restores the pre-1.4.0 behavior of spawning in any water body (rivers, swamps, etc).")
         .define("oceanOnly", true);
      builder.comment("Per-species natural spawn toggles. False cancels natural spawns of that species only.").push("species");
      for (String species : new String[]{
         "axodile", "barracuda", "basking_shark", "blacktip_reef_shark", "blue_shark", "bonnethead_shark",
         "bull_shark", "cookiecutter_shark", "greater_axodile", "greatwhiteshark", "greenland_shark", "krill",
         "land_shark", "lemon_shark", "mako_shark", "megalodon", "nurse_shark", "pilot_fish", "remora",
         "roll_particle", "thalassoger", "tiger_shark", "whale_shark", "whitetip_shark"
      }) {
         SPECIES_FLAGS.put(species, builder.define(species + "Enabled", true));
      }
      builder.pop().pop();
      builder.comment("Damage balance. Effective damage = base damage x sharkDamageMultiplier;",
         "megalodonDamage replaces the Megalodon's attribute base before the multiplier applies.").push("damage");
      SHARK_DAMAGE_MULTIPLIER = builder
         .comment("Global multiplier applied to all shark attack damage (1.0 = unchanged).")
         .defineInRange("sharkDamageMultiplier", 1.0, 0.0, 100.0);
      MEGALODON_DAMAGE = builder
         .comment("Absolute base damage for Megalodon hits, before sharkDamageMultiplier (default 20.0 = current balance).")
         .defineInRange("megalodonDamage", 20.0, 0.0, 1000.0);
      MEGALODON_ARMOR_BYPASS = builder
         .comment("False (default, recommended): Megalodon damage is reduced by armor like every other attack.",
            "True routes Megalodon hits through the dwurdysharks:megalodon_bite damage type, which bypasses armor.")
         .define("megalodonArmorBypass", false);
      builder.pop();
      SPEC = builder.build();
   }

   public static boolean isModEntity(@Nullable EntityType<?> type) {
      if (type == null) {
         return false;
      }
      ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
      return id != null && "dwurdysharks".equals(id.getNamespace());
   }

   public static boolean speciesEnabled(@Nullable EntityType<?> type) {
      if (!isModEntity(type)) {
         return true;
      }
      ModConfigSpec.BooleanValue flag = SPECIES_FLAGS.get(BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath());
      return flag == null || flag.get();
   }
}
