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

   public static final int CURRENT_CONFIG_VERSION = 1;
   public static final ModConfigSpec SPEC;
   public static final ModConfigSpec.IntValue CONFIG_VERSION;
   public static final ModConfigSpec.BooleanValue SPAWNING_ENABLED;
   public static final ModConfigSpec.BooleanValue OCEAN_ONLY;
   public static final ModConfigSpec.IntValue LARGE_SHARK_GLOBAL_CAP;
   public static final ModConfigSpec.IntValue AMBIENT_FISH_GLOBAL_CAP;
   public static final ModConfigSpec.DoubleValue SHARK_DAMAGE_MULTIPLIER;
   public static final ModConfigSpec.DoubleValue MEGALODON_DAMAGE;
   public static final ModConfigSpec.BooleanValue MEGALODON_ARMOR_BYPASS;
   public static final ModConfigSpec.DoubleValue SHARK_SPEED_MULTIPLIER;
   public static final ModConfigSpec.IntValue DRYOUT_DELAY_TICKS;
   public static final ModConfigSpec.IntValue DRYOUT_DURATION_TICKS;
   public static final ModConfigSpec.BooleanValue ITEM_EATING_ENABLED;
   public static final ModConfigSpec.DoubleValue AGGRO_FOLLOW_RANGE_MULTIPLIER;
   public static final ModConfigSpec.IntValue HARD_DESPAWN_DISTANCE_BLOCKS;
   private static final Map<String, ModConfigSpec.BooleanValue> SPECIES_FLAGS = new HashMap<>();
   private static final Map<String, ModConfigSpec.DoubleValue> SPECIES_DAMAGE_MULTIPLIERS = new HashMap<>();
   private static final Map<String, ModConfigSpec.DoubleValue> SPECIES_HEALTH_OVERRIDES = new HashMap<>();
   private static final Map<String, ModConfigSpec.DoubleValue> SPECIES_SPEED_MULTIPLIERS = new HashMap<>();

   static final String[] SPECIES_KEYS = {
      "axodile", "barracuda", "basking_shark", "blacktip_reef_shark", "blue_shark", "bonnethead_shark",
      "bull_shark", "cookiecutter_shark", "greater_axodile", "greatwhiteshark", "greenland_shark", "krill",
      "land_shark", "lemon_shark", "mako_shark", "megalodon", "nurse_shark", "pilot_fish", "remora",
      "roll_particle", "thalassoger", "tiger_shark", "whale_shark", "whitetip_shark"
   };

   static {
      ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
      builder.comment(
         "Dwurdy Sharks server configuration. Gamerules remain the per-world runtime knobs (caps, aggression);",
         "this file holds global balance, defaults, and master toggles. Where a value exists in both places,",
         "a gamerule value of -1 means 'inherit this config'. Edits take effect on /reload or restart for",
         "new spawn checks and new damage applications; attribute changes apply to entities on next join."
      );
      CONFIG_VERSION = builder
         .comment("Schema version of this file. Managed by the mod; do not edit. If the file carries a newer",
            "version than the mod understands, a warning is logged at load.")
         .defineInRange("configVersion", 1, 1, Integer.MAX_VALUE);
      builder.push("spawning");
      SPAWNING_ENABLED = builder
         .comment("Master switch for natural dwurdysharks spawns. False cancels natural spawns of every mod species;",
            "manual spawns (/summon, spawn egg, dispenser, bucket, breeding) still work as an admin escape hatch.")
         .define("spawningEnabled", true);
      OCEAN_ONLY = builder
         .comment("True restricts water spawn predicates to biomes in the dwurdysharks:shark_spawning_oceans biome tag.",
            "False restores the pre-1.4.0 behavior of spawning in any water body (rivers, swamps, etc).")
         .define("oceanOnly", true);
      builder.comment("Per-species natural spawn toggles. False cancels natural spawns of that species only.").push("species");
      for (String species : SPECIES_KEYS) {
         SPECIES_FLAGS.put(species, builder.define(species + "Enabled", true));
      }
      builder.pop().pop();
      builder.comment("Whole-dimension population ceilings. These are the GLOBAL caps; the per-radius local caps",
         "are gamerules (largeSharkLocalCap, ambientFishLocalCap, sharkSpawnCapRadius). A gamerule value of -1",
         "inherits the config value here. Effective cap 0 = unlimited.").push("population");
      LARGE_SHARK_GLOBAL_CAP = builder
         .comment("Maximum wild non-exempt LARGE_SHARKS-tagged entities per dimension (0 = unlimited).",
            "Counts entities across the entire dimension, not a radius. Exempt: tamed, named,",
            "persistence-required, bucket-released, and bred entities. Gamerule largeSharkGlobalCap",
            "overrides this per world when set >= 0.")
         .defineInRange("largeSharkGlobalCap", 0, 0, 100000);
      AMBIENT_FISH_GLOBAL_CAP = builder
         .comment("Same as largeSharkGlobalCap but for the AMBIENT_FISH tag (krill, remora, pilot fish,",
            "roll particle). Gamerule ambientFishGlobalCap overrides per world when set >= 0.")
         .defineInRange("ambientFishGlobalCap", 0, 0, 100000);
      builder.pop();
      builder.comment("Damage balance. Effective damage = base damage x sharkDamageMultiplier x speciesMultiplier;",
         "megalodonDamage replaces the Megalodon's attribute base before multipliers apply.").push("damage");
      SHARK_DAMAGE_MULTIPLIER = builder
         .comment("Global multiplier applied to all shark attack damage (1.0 = unchanged). Range: 0-100.")
         .defineInRange("sharkDamageMultiplier", 1.0, 0.0, 100.0);
      MEGALODON_DAMAGE = builder
         .comment("Absolute base damage for Megalodon hits, before multipliers (default 20.0 = current balance).")
         .defineInRange("megalodonDamage", 20.0, 0.0, 1000.0);
      MEGALODON_ARMOR_BYPASS = builder
         .comment("False (default, recommended): Megalodon damage is reduced by armor like every other attack.",
            "True routes Megalodon hits through the dwurdysharks:megalodon_bite damage type, which bypasses armor.")
         .define("megalodonArmorBypass", false);
      builder.comment("Per-species damage multipliers, stacked with sharkDamageMultiplier (1.0 = unchanged).").push("speciesMultiplier");
      for (String species : SPECIES_KEYS) {
         SPECIES_DAMAGE_MULTIPLIERS.put(species, builder.defineInRange(species, 1.0, 0.0, 100.0));
      }
      builder.pop();
      builder.comment("Per-species absolute max-health overrides in half-hearts (0 = inherit the entity's base value).").push("speciesHealth");
      for (String species : SPECIES_KEYS) {
         SPECIES_HEALTH_OVERRIDES.put(species, builder.defineInRange(species, 0.0, 0.0, 100000.0));
      }
      builder.pop().pop();
      builder.comment("Movement speed balance. Effective speed = species base speed x sharkSpeedMultiplier",
         "x speciesMultiplier.").push("movement");
      SHARK_SPEED_MULTIPLIER = builder
         .comment("Global multiplier applied to every mod entity's movement speed (1.0 = unchanged).",
            "Range: 0-100. Applied to entities when they join; existing entities pick up changes on rejoin.")
         .defineInRange("sharkSpeedMultiplier", 1.0, 0.0, 100.0);
      builder.comment("Per-species speed multipliers, stacked with sharkSpeedMultiplier (1.0 = unchanged).").push("speciesMultiplier");
      for (String species : SPECIES_KEYS) {
         SPECIES_SPEED_MULTIPLIERS.put(species, builder.defineInRange(species, 1.0, 0.0, 100.0));
      }
      builder.pop().pop();
      builder.comment("Behavior toggles and timings.").push("behavior");
      DRYOUT_DELAY_TICKS = builder
         .comment("Ticks out of water before the dryout effect starts (default 600 = 30 s). 0 disables dryout",
            "entirely. Counter persists per entity; lowering mid-session applies sooner, raising delays.")
         .defineInRange("dryoutDelayTicks", 600, 0, 72000);
      DRYOUT_DURATION_TICKS = builder
         .comment("Duration in ticks of each dryout effect application (default 600 = 30 s). Minimum 20.")
         .defineInRange("dryoutDurationTicks", 600, 20, 72000);
      ITEM_EATING_ENABLED = builder
         .comment("True (default): sharks may consume dropped food items. False disables item eating mod-wide.")
         .define("itemEatingEnabled", true);
      AGGRO_FOLLOW_RANGE_MULTIPLIER = builder
         .comment("Multiplier on FOLLOW_RANGE, i.e. how far aggressive sharks notice and chase targets",
            "(1.0 = unchanged). Range: 0.1-10.")
         .defineInRange("aggroFollowRangeMultiplier", 1.0, 0.1, 10.0);
      builder.pop();
      builder.comment("Despawn control for wild mod entities.").push("despawn");
      HARD_DESPAWN_DISTANCE_BLOCKS = builder
         .comment("Hard despawn distance in blocks for wild, non-exempt mod entities (default 128 = vanilla",
            "WATER_CREATURE behavior). Tamed, named, and persistence-required entities never despawn.",
            "Vanilla random soft-despawn (32-128 blocks) still applies; values below ~32 make soft-despawn",
            "unreachable. Range: 16-512.")
         .defineInRange("hardDespawnDistanceBlocks", 128, 16, 512);
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

   @Nullable
   public static String speciesKey(@Nullable EntityType<?> type) {
      if (!isModEntity(type)) {
         return null;
      }
      return BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath();
   }

   public static boolean speciesEnabled(@Nullable EntityType<?> type) {
      if (!isModEntity(type)) {
         return true;
      }
      ModConfigSpec.BooleanValue flag = SPECIES_FLAGS.get(speciesKey(type));
      return flag == null || flag.get();
   }

   public static double damageMultiplier(@Nullable EntityType<?> type) {
      double global = SHARK_DAMAGE_MULTIPLIER.get();
      ModConfigSpec.DoubleValue perSpecies = SPECIES_DAMAGE_MULTIPLIERS.get(speciesKey(type));
      return perSpecies == null ? global : global * perSpecies.get();
   }

   public static double speedMultiplier(@Nullable EntityType<?> type) {
      double global = SHARK_SPEED_MULTIPLIER.get();
      ModConfigSpec.DoubleValue perSpecies = SPECIES_SPEED_MULTIPLIERS.get(speciesKey(type));
      return perSpecies == null ? global : global * perSpecies.get();
   }

   public static double healthOverride(@Nullable EntityType<?> type) {
      ModConfigSpec.DoubleValue override = SPECIES_HEALTH_OVERRIDES.get(speciesKey(type));
      return override == null ? 0.0 : override.get();
   }

   @Nullable
   public static ModConfigSpec.DoubleValue speciesDamageMultiplierValue(String speciesKey) {
      return SPECIES_DAMAGE_MULTIPLIERS.get(speciesKey);
   }

   @Nullable
   public static ModConfigSpec.DoubleValue speciesHealthOverrideValue(String speciesKey) {
      return SPECIES_HEALTH_OVERRIDES.get(speciesKey);
   }

   @Nullable
   public static ModConfigSpec.DoubleValue speciesSpeedMultiplierValue(String speciesKey) {
      return SPECIES_SPEED_MULTIPLIERS.get(speciesKey);
   }
}
