package net.mcreator.sharks.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;

public class LandSharkFloatProcedure {
   public static boolean execute(LevelAccessor world, Entity entity) {
      return entity == null
         ? false
         : !entity.isInWaterOrBubble()
            || world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.0, entity.getZ())).getBlock() instanceof LiquidBlock
               && world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ())).getBlock() instanceof LiquidBlock
               && world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2.0, entity.getZ())).getBlock() instanceof LiquidBlock;
   }
}
