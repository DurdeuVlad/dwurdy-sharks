package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.entity.SharkPlushieTileEntity;
import net.mcreator.sharks.block.model.SharkPlushieBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SharkPlushieTileRenderer extends GeoBlockRenderer<SharkPlushieTileEntity> {
   public SharkPlushieTileRenderer() {
      super(new SharkPlushieBlockModel());
   }

   public RenderType getRenderType(SharkPlushieTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
