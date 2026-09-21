package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class BonnetheadSharkOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double velocityY = 0.0;
         if (entity instanceof BonnetheadSharkEntity && !entity.isInWaterOrBubble()) {
            DryoutProcedure.dryTick(entity, 600, 600);
         } else if (entity.isInWaterOrBubble() && entity instanceof LivingEntity _entity) {
            DryoutProcedure.wetTick(_entity);
         }

         if (world.getBlockState(BlockPos.containing(x, y, z)).getBlock() == Blocks.TALL_SEAGRASS) {
            if (world.getBlockState(BlockPos.containing(x, y, z)).getBlock() == Blocks.TALL_SEAGRASS
               && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() == Blocks.TALL_SEAGRASS
               && world.getBlockState(BlockPos.containing(x, y - 2.0, z)).canOcclude()) {
               world.destroyBlock(BlockPos.containing(x, y, z), false);
               world.setBlock(BlockPos.containing(x, y - 1.0, z), Blocks.SEAGRASS.defaultBlockState(), 3);
            }

            if (world.getBlockState(BlockPos.containing(x, y, z)).getBlock() == Blocks.TALL_SEAGRASS
               && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).canOcclude()) {
               world.destroyBlock(BlockPos.containing(x, y, z), false);
               world.setBlock(BlockPos.containing(x, y, z), Blocks.SEAGRASS.defaultBlockState(), 3);
            }
         }
      }
   }
}
