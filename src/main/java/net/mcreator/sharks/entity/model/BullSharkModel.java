package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BullSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BullSharkModel extends GeoModel<BullSharkEntity> {
   public ResourceLocation getAnimationResource(BullSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/bull_shark.animation.json");
   }

   public ResourceLocation getModelResource(BullSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/bull_shark.geo.json");
   }

   public ResourceLocation getTextureResource(BullSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
