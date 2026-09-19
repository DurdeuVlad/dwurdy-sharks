package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.MegalodonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MegalodonModel extends GeoModel<MegalodonEntity> {
   public ResourceLocation getAnimationResource(MegalodonEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/megalodon.animation.json");
   }

   public ResourceLocation getModelResource(MegalodonEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/megalodon.geo.json");
   }

   public ResourceLocation getTextureResource(MegalodonEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }
}
