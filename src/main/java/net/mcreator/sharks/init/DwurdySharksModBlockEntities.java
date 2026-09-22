package net.mcreator.sharks.init;

import net.mcreator.sharks.block.entity.LandMinePrimedTileEntity;
import net.mcreator.sharks.block.entity.LandMineTileEntity;
import net.mcreator.sharks.block.entity.SharkPlushieTileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DwurdySharksModBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "dwurdysharks");
   public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> LAND_MINE = register(
      "land_mine", DwurdySharksModBlocks.LAND_MINE, LandMineTileEntity::new
   );
   public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SHARK_PLUSH_BLOCK = register(
      "shark_plush_block", DwurdySharksModBlocks.SHARK_PLUSH_BLOCK, SharkPlushieTileEntity::new
   );
   public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> LAND_MINE_PRIMED = register(
      "land_mine_primed", DwurdySharksModBlocks.LAND_MINE_PRIMED, LandMinePrimedTileEntity::new
   );

   private static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> register(
      String registryname, DeferredHolder<Block, Block> block, BlockEntitySupplier<?> supplier
   ) {
      return REGISTRY.register(registryname, () -> Builder.of(supplier, new Block[]{(Block)block.get()}).build(null));
   }
}
