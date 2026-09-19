package net.mcreator.sharks.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SharkPlushieOnBlockRightClickedProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      int _value = 1;
      BlockPos _pos = BlockPos.containing(x, y, z);
      BlockState _bs = world.getBlockState(_pos);
      if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp
         && _integerProp.getPossibleValues().contains(_value)) {
         world.setBlock(_pos, (BlockState)_bs.setValue(_integerProp, _value), 3);
      }

      if (world instanceof Level _level) {
         if (!_level.isClientSide()) {
            _level.playSound(
               null,
               BlockPos.containing(x, y, z),
               (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:squeak")),
               SoundSource.NEUTRAL,
               1.0F,
               1.0F
            );
         } else {
            _level.playLocalSound(
               x, y, z, (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:squeak")), SoundSource.NEUTRAL, 1.0F, 1.0F, false
            );
         }
      }
   }
}
