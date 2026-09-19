package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BlueSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BlueSharkModel extends GeoModel<BlueSharkEntity> {
   public ResourceLocation getAnimationResource(BlueSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/blueshark.animation.json");
   }

   public ResourceLocation getModelResource(BlueSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/blueshark.geo.json");
   }

   public ResourceLocation getTextureResource(BlueSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
