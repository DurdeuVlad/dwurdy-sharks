package net.mcreator.sharks.init;

import net.minecraft.world.level.GameRules;

public class BenssharksModGameRules {
   public static final GameRules.Key<GameRules.BooleanValue> AGGRESSIVE_SHARKS =
      GameRules.register("aggressiveSharks", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));

   public static void register() {
   }
}
