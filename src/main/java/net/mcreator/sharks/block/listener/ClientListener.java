package net.mcreator.sharks.block.listener;

import net.mcreator.sharks.block.renderer.LandMinePrimedTileRenderer;
import net.mcreator.sharks.block.renderer.LandMineTileRenderer;
import net.mcreator.sharks.block.renderer.SharkPlushieTileRenderer;
import net.mcreator.sharks.init.BenssharksModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;

@EventBusSubscriber(
   modid = "dwurdysharks",
   value = {Dist.CLIENT},
   bus = Bus.MOD
)
public class ClientListener {
   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void registerRenderers(RegisterRenderers event) {
      event.registerBlockEntityRenderer((BlockEntityType)BenssharksModBlockEntities.LAND_MINE.get(), context -> new LandMineTileRenderer());
      event.registerBlockEntityRenderer((BlockEntityType)BenssharksModBlockEntities.SHARK_PLUSH_BLOCK.get(), context -> new SharkPlushieTileRenderer());
      event.registerBlockEntityRenderer((BlockEntityType)BenssharksModBlockEntities.LAND_MINE_PRIMED.get(), context -> new LandMinePrimedTileRenderer());
   }
}
