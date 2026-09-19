package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.display.SharkPlushieDisplayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SharkPlushieDisplayModel extends GeoModel<SharkPlushieDisplayItem> {
   public ResourceLocation getAnimationResource(SharkPlushieDisplayItem animatable) {
      return ResourceLocation.parse("dwurdysharks:animations/blahaj.animation.json");
   }

   public ResourceLocation getModelResource(SharkPlushieDisplayItem animatable) {
      return ResourceLocation.parse("dwurdysharks:geo/blahaj.geo.json");
   }

   public ResourceLocation getTextureResource(SharkPlushieDisplayItem entity) {
      return ResourceLocation.parse("dwurdysharks:textures/block/blahaj.png");
   }
}
