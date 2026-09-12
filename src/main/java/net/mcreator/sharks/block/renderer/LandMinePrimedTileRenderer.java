package net.mcreator.sharks.block.renderer;

import net.mcreator.sharks.block.entity.LandMinePrimedTileEntity;
import net.mcreator.sharks.block.model.LandMinePrimedBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LandMinePrimedTileRenderer extends GeoBlockRenderer<LandMinePrimedTileEntity> {
   public LandMinePrimedTileRenderer() {
      super(new LandMinePrimedBlockModel());
   }

   public RenderType getRenderType(LandMinePrimedTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
