package net.mcreator.sharks.item.model;

import net.mcreator.sharks.item.SpetumItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpetumItemModel extends GeoModel<SpetumItem> {
   public ResourceLocation getAnimationResource(SpetumItem animatable) {
      return ResourceLocation.parse("dwurdysharks:animations/spetum.animation.json");
   }

   public ResourceLocation getModelResource(SpetumItem animatable) {
      return ResourceLocation.parse("dwurdysharks:geo/spetum.geo.json");
   }

   public ResourceLocation getTextureResource(SpetumItem animatable) {
      return ResourceLocation.parse("dwurdysharks:textures/item/spetum.png");
   }
}
