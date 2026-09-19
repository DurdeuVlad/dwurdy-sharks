package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaskingSharkModel extends GeoModel<BaskingSharkEntity> {
   public ResourceLocation getAnimationResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/baskingshark.animation.json");
   }

   public ResourceLocation getModelResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/baskingshark.geo.json");
   }

   public ResourceLocation getTextureResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
