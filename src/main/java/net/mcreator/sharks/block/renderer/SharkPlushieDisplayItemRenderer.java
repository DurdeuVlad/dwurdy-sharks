package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.display.SharkPlushieDisplayItem;
import net.mcreator.sharks.block.model.SharkPlushieDisplayModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SharkPlushieDisplayItemRenderer extends GeoItemRenderer<SharkPlushieDisplayItem> {
   public SharkPlushieDisplayItemRenderer() {
      super(new SharkPlushieDisplayModel());
   }

   public RenderType getRenderType(SharkPlushieDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
