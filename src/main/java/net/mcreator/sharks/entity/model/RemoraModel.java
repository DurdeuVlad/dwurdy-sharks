package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.RemoraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RemoraModel extends GeoModel<RemoraEntity> {
   public ResourceLocation getAnimationResource(RemoraEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/remora.animation.json");
   }

   public ResourceLocation getModelResource(RemoraEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/remora.geo.json");
   }

   public ResourceLocation getTextureResource(RemoraEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
