package net.mcreator.sharks.init;

import net.minecraft.world.level.GameRules;

public class DwurdySharksModGameRules {
   public static final GameRules.Key<GameRules.BooleanValue> AGGRESSIVE_SHARKS =
      GameRules.register("dwurdySharksAggressiveSharks", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
   public static final GameRules.Key<GameRules.IntegerValue> LARGE_SHARK_LOCAL_CAP =
      GameRules.register("dwurdySharksLargeSharkLocalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(24));
   public static final GameRules.Key<GameRules.IntegerValue> AMBIENT_FISH_LOCAL_CAP =
      GameRules.register("dwurdySharksAmbientFishLocalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(64));
   public static final GameRules.Key<GameRules.IntegerValue> SPAWN_CAP_RADIUS =
      GameRules.register("dwurdySharksSpawnCapRadius", GameRules.Category.MOBS, GameRules.IntegerValue.create(128));
   public static final GameRules.Key<GameRules.BooleanValue> ENFORCE_CAP_FOR_MANUAL_SPAWNS =
      GameRules.register("dwurdySharksEnforceCapForManualSpawns", GameRules.Category.MOBS, GameRules.BooleanValue.create(true));
   public static final GameRules.Key<GameRules.IntegerValue> LARGE_SHARK_GLOBAL_CAP =
      GameRules.register("dwurdySharksLargeSharkGlobalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(-1));
   public static final GameRules.Key<GameRules.IntegerValue> AMBIENT_FISH_GLOBAL_CAP =
      GameRules.register("dwurdySharksAmbientFishGlobalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(-1));

   public static void register() {
   }
}
