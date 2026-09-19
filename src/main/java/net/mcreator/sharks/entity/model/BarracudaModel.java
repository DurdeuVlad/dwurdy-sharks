package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BarracudaModel extends GeoModel<BarracudaEntity> {
   public ResourceLocation getAnimationResource(BarracudaEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/barracuda.animation.json");
   }

   public ResourceLocation getModelResource(BarracudaEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/barracuda.geo.json");
   }

   public ResourceLocation getTextureResource(BarracudaEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
