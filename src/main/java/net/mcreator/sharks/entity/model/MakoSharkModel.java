package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.MakoSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MakoSharkModel extends GeoModel<MakoSharkEntity> {
   public ResourceLocation getAnimationResource(MakoSharkEntity entity) {
      return ResourceLocation.parse("benssharks:animations/makoshark.animation.json");
   }

   public ResourceLocation getModelResource(MakoSharkEntity entity) {
      return ResourceLocation.parse("benssharks:geo/makoshark.geo.json");
   }

   public ResourceLocation getTextureResource(MakoSharkEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
