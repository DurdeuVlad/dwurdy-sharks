package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.RemoraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RemoraModel extends GeoModel<RemoraEntity> {
   public ResourceLocation getAnimationResource(RemoraEntity entity) {
      return ResourceLocation.parse("benssharks:animations/remora.animation.json");
   }

   public ResourceLocation getModelResource(RemoraEntity entity) {
      return ResourceLocation.parse("benssharks:geo/remora.geo.json");
   }

   public ResourceLocation getTextureResource(RemoraEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
