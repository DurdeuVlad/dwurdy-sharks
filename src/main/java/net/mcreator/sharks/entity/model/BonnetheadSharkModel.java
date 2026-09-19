package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BonnetheadSharkModel extends GeoModel<BonnetheadSharkEntity> {
   public ResourceLocation getAnimationResource(BonnetheadSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/bonnethead_shark.animation.json");
   }

   public ResourceLocation getModelResource(BonnetheadSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/bonnethead_shark.geo.json");
   }

   public ResourceLocation getTextureResource(BonnetheadSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
