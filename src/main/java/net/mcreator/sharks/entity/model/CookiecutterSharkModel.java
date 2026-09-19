package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.CookiecutterSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CookiecutterSharkModel extends GeoModel<CookiecutterSharkEntity> {
   public ResourceLocation getAnimationResource(CookiecutterSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/cookiecutter.animation.json");
   }

   public ResourceLocation getModelResource(CookiecutterSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/cookiecutter.geo.json");
   }

   public ResourceLocation getTextureResource(CookiecutterSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
