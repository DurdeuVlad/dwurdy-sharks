package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaskingSharkModel extends GeoModel<BaskingSharkEntity> {
   public ResourceLocation getAnimationResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("benssharks:animations/baskingshark.animation.json");
   }

   public ResourceLocation getModelResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("benssharks:geo/baskingshark.geo.json");
   }

   public ResourceLocation getTextureResource(BaskingSharkEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
