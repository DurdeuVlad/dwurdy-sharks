package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.display.LandMineDisplayItem;
import net.mcreator.sharks.block.model.LandMineDisplayModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LandMineDisplayItemRenderer extends GeoItemRenderer<LandMineDisplayItem> {
   public LandMineDisplayItemRenderer() {
      super(new LandMineDisplayModel());
   }

   public RenderType getRenderType(LandMineDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
