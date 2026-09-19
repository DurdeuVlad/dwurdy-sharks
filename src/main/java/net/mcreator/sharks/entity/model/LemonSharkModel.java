package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.LemonSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LemonSharkModel extends GeoModel<LemonSharkEntity> {
   public ResourceLocation getAnimationResource(LemonSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/lemon.animation.json");
   }

   public ResourceLocation getModelResource(LemonSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/lemon.geo.json");
   }

   public ResourceLocation getTextureResource(LemonSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
