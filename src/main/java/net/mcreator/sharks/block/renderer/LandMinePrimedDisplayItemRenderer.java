package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.display.LandMinePrimedDisplayItem;
import net.mcreator.sharks.block.model.LandMinePrimedDisplayModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LandMinePrimedDisplayItemRenderer extends GeoItemRenderer<LandMinePrimedDisplayItem> {
   public LandMinePrimedDisplayItemRenderer() {
      super(new LandMinePrimedDisplayModel());
   }

   public RenderType getRenderType(LandMinePrimedDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
