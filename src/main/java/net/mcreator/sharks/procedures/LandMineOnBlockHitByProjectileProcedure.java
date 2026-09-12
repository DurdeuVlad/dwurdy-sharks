package net.mcreator.sharks.procedures;

import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.init.BenssharksModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class LandMineOnBlockHitByProjectileProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      int _value = 1;
      BlockPos _pos = BlockPos.containing(x, y, z);
      BlockState _bs = world.getBlockState(_pos);
      if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp
         && _integerProp.getPossibleValues().contains(_value)) {
         world.setBlock(_pos, (BlockState)_bs.setValue(_integerProp, _value), 3);
      }

      BlockPos _bp = BlockPos.containing(x, y, z);
      BlockState _bsx = ((Block)BenssharksModBlocks.LAND_MINE_PRIMED.get()).defaultBlockState();
      _bs = world.getBlockState(_bp);

      for (Property<?> _propertyOld : _bs.getProperties()) {
         Property _propertyNew = _bsx.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
         if (_propertyNew != null && _bsx.getValue(_propertyNew) != null) {
            try {
               _bsx = (BlockState)_bsx.setValue(_propertyNew, _bs.getValue(_propertyOld));
            } catch (Exception var14) {
            }
         }
      }

      world.setBlock(_bp, _bsx, 3);
      BenssharksMod.queueServerWork(
         1,
         () -> {
            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.bamboo_wood_button.click_on")),
                     SoundSource.BLOCKS,
                     1.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x,
                     y,
                     z,
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.bamboo_wood_button.click_on")),
                     SoundSource.BLOCKS,
                     1.0F,
                     1.0F,
                     false
                  );
               }
            }

            BenssharksMod.queueServerWork(10, () -> {
               world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
               if (world instanceof Level _levelx && !_levelx.isClientSide()) {
                  _levelx.explode(null, x, y, z, 3.5F, ExplosionInteraction.BLOCK);
               }
            });
         }
      );
   }
}
