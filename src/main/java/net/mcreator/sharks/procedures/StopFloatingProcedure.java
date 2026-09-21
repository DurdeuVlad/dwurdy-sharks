package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.MegalodonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class StopFloatingProcedure {
   public static void execute(LevelAccessor world, double x, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof MegalodonEntity
            && (
               world.getBlockState(BlockPos.containing(x, entity.getY() + 0.1, z)).getBlock() == Blocks.AIR
                  || world.getBlockState(BlockPos.containing(x, entity.getY() + 1.0, z)).getBlock() == Blocks.AIR
                  || world.getBlockState(BlockPos.containing(x, entity.getY() + 2.0, z)).getBlock() == Blocks.AIR
            )
            && entity.isInWaterOrBubble()) {
            entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), entity.getDeltaMovement().y() - 0.5, entity.getDeltaMovement().z()));
         }
      }
   }
}
