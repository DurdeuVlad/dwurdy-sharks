package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WhaleSharkModel extends GeoModel<WhaleSharkEntity> {
   public ResourceLocation getAnimationResource(WhaleSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/whaleshark.animation.json");
   }

   public ResourceLocation getModelResource(WhaleSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/whaleshark.geo.json");
   }

   public ResourceLocation getTextureResource(WhaleSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
