package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BlacktipReefSharkModel extends GeoModel<BlacktipReefSharkEntity> {
   public ResourceLocation getAnimationResource(BlacktipReefSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/blacktip_reef_shark.animation.json");
   }

   public ResourceLocation getModelResource(BlacktipReefSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/blacktip_reef_shark.geo.json");
   }

   public ResourceLocation getTextureResource(BlacktipReefSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
