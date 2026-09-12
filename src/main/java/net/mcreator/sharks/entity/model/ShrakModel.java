package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.ShrakEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShrakModel extends GeoModel<ShrakEntity> {
   public ResourceLocation getAnimationResource(ShrakEntity entity) {
      return ResourceLocation.parse("benssharks:animations/greatwhite.animation.json");
   }

   public ResourceLocation getModelResource(ShrakEntity entity) {
      return ResourceLocation.parse("benssharks:geo/greatwhite.geo.json");
   }

   public ResourceLocation getTextureResource(ShrakEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
