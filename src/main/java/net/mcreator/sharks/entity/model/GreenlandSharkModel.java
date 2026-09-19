package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GreenlandSharkModel extends GeoModel<GreenlandSharkEntity> {
   public ResourceLocation getAnimationResource(GreenlandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/greenlandshark.animation.json");
   }

   public ResourceLocation getModelResource(GreenlandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/greenlandshark.geo.json");
   }

   public ResourceLocation getTextureResource(GreenlandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
