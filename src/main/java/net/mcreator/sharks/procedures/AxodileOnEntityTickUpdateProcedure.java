package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.AxodileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AxodileOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double velocityX = 0.0;
         double velocityY = 0.0;
         double velocityZ = 0.0;
         if (entity instanceof AxodileEntity) {
            velocityY = entity.getDeltaMovement().y();
            if (velocityY > 0.0 && !entity.isInWaterOrBubble()) {
               if (entity instanceof AxodileEntity) {
                  ((AxodileEntity)entity).setAnimation("landjump");
               }
            } else if (velocityY == 0.0 && !entity.isInWaterOrBubble() && entity instanceof AxodileEntity) {
               ((AxodileEntity)entity).setAnimation("idle");
            }
         }

         if (entity instanceof AxodileEntity
            && entity.isUnderWater()
            && world.isEmptyBlock(BlockPos.containing(x, y + 1.0, z))
            && (
               !world.getBlockState(BlockPos.containing(x, y - 2.0, z)).canOcclude()
                  || !world.getBlockState(BlockPos.containing(x, y - 2.0, z)).is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))
                  || !world.getBlockState(BlockPos.containing(x, y - 2.0, z)).is(BlockTags.create(ResourceLocation.parse("minecraft:glass")))
            )) {
            entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), 1.0, entity.getDeltaMovement().z()));
            if (entity instanceof AxodileEntity) {
               ((AxodileEntity)entity).setAnimation("landjump");
            }

            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.dolphin.jump")),
                     SoundSource.BLOCKS,
                     0.25F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x,
                     y,
                     z,
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.dolphin.jump")),
                     SoundSource.BLOCKS,
                     0.25F,
                     1.0F,
                     false
                  );
               }
            }
         }

         if (world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() == Blocks.ICE
            || world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() == Blocks.FROSTED_ICE) {
            world.destroyBlock(BlockPos.containing(x, y + 1.0, z), false);
            world.setBlock(BlockPos.containing(x, y + 1.0, z), Blocks.WATER.defaultBlockState(), 3);
         }

         if (world.getBlockState(BlockPos.containing(x, y + 1.0, z)).is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))) {
            world.destroyBlock(BlockPos.containing(x, y + 1.0, z), false);
            entity.setDeltaMovement(new Vec3(0.0, 1.0, 0.0));
            BenssharksMod.queueServerWork(2, () -> entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0)));
         }
      }
   }
}
