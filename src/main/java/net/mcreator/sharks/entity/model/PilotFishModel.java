package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.PilotFishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PilotFishModel extends GeoModel<PilotFishEntity> {
   public ResourceLocation getAnimationResource(PilotFishEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/pilot_fish.animation.json");
   }

   public ResourceLocation getModelResource(PilotFishEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/pilot_fish.geo.json");
   }

   public ResourceLocation getTextureResource(PilotFishEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
