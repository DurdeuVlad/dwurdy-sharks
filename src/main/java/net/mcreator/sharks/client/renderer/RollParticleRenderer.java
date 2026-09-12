package net.mcreator.sharks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.sharks.entity.RollParticleEntity;
import net.mcreator.sharks.entity.layer.RollParticleLayer;
import net.mcreator.sharks.entity.model.RollParticleModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RollParticleRenderer extends GeoEntityRenderer<RollParticleEntity> {
   public RollParticleRenderer(Context renderManager) {
      super(renderManager, new RollParticleModel());
      this.shadowRadius = 0.1F;
      this.addRenderLayer(new RollParticleLayer(this));
   }

   public RenderType getRenderType(RollParticleEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }

   public void preRender(
      PoseStack poseStack,
      RollParticleEntity entity,
      BakedGeoModel model,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      boolean isReRender,
      float partialTick,
      int packedLight,
      int packedOverlay,
      int color
   ) {
      float scale = 0.1F;
      this.scaleHeight = scale;
      this.scaleWidth = scale;
      super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
   }
}
