package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.NurseSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NurseSharkModel extends GeoModel<NurseSharkEntity> {
   public ResourceLocation getAnimationResource(NurseSharkEntity entity) {
      return ResourceLocation.parse("benssharks:animations/nurse_shark.animation.json");
   }

   public ResourceLocation getModelResource(NurseSharkEntity entity) {
      return ResourceLocation.parse("benssharks:geo/nurse_shark.geo.json");
   }

   public ResourceLocation getTextureResource(NurseSharkEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
