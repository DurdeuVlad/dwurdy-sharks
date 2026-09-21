package net.mcreator.sharks.init;

import net.minecraft.world.level.GameRules;

public class BenssharksModGameRules {
   public static final GameRules.Key<GameRules.BooleanValue> AGGRESSIVE_SHARKS =
      GameRules.register("aggressiveSharks", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
   public static final GameRules.Key<GameRules.IntegerValue> LARGE_SHARK_LOCAL_CAP =
      GameRules.register("largeSharkLocalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(24));
   public static final GameRules.Key<GameRules.IntegerValue> AMBIENT_FISH_LOCAL_CAP =
      GameRules.register("ambientFishLocalCap", GameRules.Category.MOBS, GameRules.IntegerValue.create(64));
   public static final GameRules.Key<GameRules.IntegerValue> SPAWN_CAP_RADIUS =
      GameRules.register("sharkSpawnCapRadius", GameRules.Category.MOBS, GameRules.IntegerValue.create(128));
   public static final GameRules.Key<GameRules.BooleanValue> ENFORCE_CAP_FOR_MANUAL_SPAWNS =
      GameRules.register("enforceCapForManualSpawns", GameRules.Category.MOBS, GameRules.BooleanValue.create(true));

   public static void register() {
   }
}
