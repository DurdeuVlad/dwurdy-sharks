package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.display.SharkPlushieDisplayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SharkPlushieDisplayModel extends GeoModel<SharkPlushieDisplayItem> {
   public ResourceLocation getAnimationResource(SharkPlushieDisplayItem animatable) {
      return ResourceLocation.parse("benssharks:animations/blahaj.animation.json");
   }

   public ResourceLocation getModelResource(SharkPlushieDisplayItem animatable) {
      return ResourceLocation.parse("benssharks:geo/blahaj.geo.json");
   }

   public ResourceLocation getTextureResource(SharkPlushieDisplayItem entity) {
      return ResourceLocation.parse("benssharks:textures/block/blahaj.png");
   }
}
