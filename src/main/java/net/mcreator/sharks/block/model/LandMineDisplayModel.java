package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.display.LandMineDisplayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LandMineDisplayModel extends GeoModel<LandMineDisplayItem> {
   public ResourceLocation getAnimationResource(LandMineDisplayItem animatable) {
      return ResourceLocation.parse("dwurdysharks:animations/landmine.animation.json");
   }

   public ResourceLocation getModelResource(LandMineDisplayItem animatable) {
      return ResourceLocation.parse("dwurdysharks:geo/landmine.geo.json");
   }

   public ResourceLocation getTextureResource(LandMineDisplayItem entity) {
      return ResourceLocation.parse("dwurdysharks:textures/block/landminedefault.png");
   }
}
