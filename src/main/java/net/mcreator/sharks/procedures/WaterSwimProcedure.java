package net.mcreator.sharks.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class WaterSwimProcedure {
   public static boolean execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      return entity == null
         ? false
         : entity.isInWaterRainOrBubble() || entity.isSwimming() || world.getBlockState(BlockPos.containing(x, y, z)).getBlock() == Blocks.WATER;
   }
}
