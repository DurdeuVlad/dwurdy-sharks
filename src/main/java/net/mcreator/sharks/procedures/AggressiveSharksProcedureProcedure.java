package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.BenssharksModGameRules;
import net.minecraft.world.level.LevelAccessor;

public class AggressiveSharksProcedureProcedure {
   public static boolean execute(LevelAccessor world) {
      return world.getLevelData().getGameRules().getBoolean(BenssharksModGameRules.AGGRESSIVE_SHARKS);
   }
}
