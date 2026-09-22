package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModGameRules;
import net.minecraft.world.level.LevelAccessor;

public class AggressiveSharksProcedureProcedure {
   public static boolean execute(LevelAccessor world) {
      return world.getLevelData().getGameRules().getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
   }
}
