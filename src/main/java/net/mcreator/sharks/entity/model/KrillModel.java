package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.KrillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KrillModel extends GeoModel<KrillEntity> {
   public ResourceLocation getAnimationResource(KrillEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/krill.animation.json");
   }

   public ResourceLocation getModelResource(KrillEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/krill.geo.json");
   }

   public ResourceLocation getTextureResource(KrillEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
