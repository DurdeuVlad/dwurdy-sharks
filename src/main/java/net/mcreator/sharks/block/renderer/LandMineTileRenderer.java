package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.entity.LandMineTileEntity;
import net.mcreator.sharks.block.model.LandMineBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LandMineTileRenderer extends GeoBlockRenderer<LandMineTileEntity> {
   public LandMineTileRenderer() {
      super(new LandMineBlockModel());
   }

   public RenderType getRenderType(LandMineTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
