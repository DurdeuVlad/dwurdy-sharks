package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.display.LandMinePrimedDisplayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LandMinePrimedDisplayModel extends GeoModel<LandMinePrimedDisplayItem> {
   public ResourceLocation getAnimationResource(LandMinePrimedDisplayItem animatable) {
      return ResourceLocation.parse("benssharks:animations/landmineprimed.animation.json");
   }

   public ResourceLocation getModelResource(LandMinePrimedDisplayItem animatable) {
      return ResourceLocation.parse("benssharks:geo/landmineprimed.geo.json");
   }

   public ResourceLocation getTextureResource(LandMinePrimedDisplayItem entity) {
      return ResourceLocation.parse("benssharks:textures/block/landminedefault.png");
   }
}
