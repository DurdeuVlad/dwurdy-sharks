package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WhitetipSharkModel extends GeoModel<WhitetipSharkEntity> {
   public ResourceLocation getAnimationResource(WhitetipSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/whitetip.animation.json");
   }

   public ResourceLocation getModelResource(WhitetipSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/whitetip.geo.json");
   }

   public ResourceLocation getTextureResource(WhitetipSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
