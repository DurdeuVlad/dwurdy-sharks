package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.MegalodonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MegalodonModel extends GeoModel<MegalodonEntity> {
   public ResourceLocation getAnimationResource(MegalodonEntity entity) {
      return ResourceLocation.parse("benssharks:animations/megalodon.animation.json");
   }

   public ResourceLocation getModelResource(MegalodonEntity entity) {
      return ResourceLocation.parse("benssharks:geo/megalodon.geo.json");
   }

   public ResourceLocation getTextureResource(MegalodonEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
