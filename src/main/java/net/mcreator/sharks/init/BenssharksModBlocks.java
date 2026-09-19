package net.mcreator.sharks.init;

import net.mcreator.sharks.block.LandMineBlock;
import net.mcreator.sharks.block.LandMinePrimedBlock;
import net.mcreator.sharks.block.SharkPlushieBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Blocks;

public class BenssharksModBlocks {
   public static final Blocks REGISTRY = DeferredRegister.createBlocks("dwurdysharks");
   public static final DeferredBlock<Block> LAND_MINE = REGISTRY.register("land_mine", LandMineBlock::new);
   public static final DeferredBlock<Block> SHARK_PLUSH_BLOCK = REGISTRY.register("shark_plush_block", SharkPlushieBlock::new);
   public static final DeferredBlock<Block> LAND_MINE_PRIMED = REGISTRY.register("land_mine_primed", LandMinePrimedBlock::new);
}
