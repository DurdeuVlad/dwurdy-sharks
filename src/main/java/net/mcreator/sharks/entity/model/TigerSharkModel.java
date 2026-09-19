package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.TigerSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TigerSharkModel extends GeoModel<TigerSharkEntity> {
   public ResourceLocation getAnimationResource(TigerSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/tiger_shark.animation.json");
   }

   public ResourceLocation getModelResource(TigerSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/tiger_shark.geo.json");
   }

   public ResourceLocation getTextureResource(TigerSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
