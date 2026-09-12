package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.entity.LandMinePrimedTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LandMinePrimedBlockModel extends GeoModel<LandMinePrimedTileEntity> {
   public ResourceLocation getAnimationResource(LandMinePrimedTileEntity animatable) {
      return ResourceLocation.parse("benssharks:animations/landmineprimed.animation.json");
   }

   public ResourceLocation getModelResource(LandMinePrimedTileEntity animatable) {
      return ResourceLocation.parse("benssharks:geo/landmineprimed.geo.json");
   }

   public ResourceLocation getTextureResource(LandMinePrimedTileEntity animatable) {
      return ResourceLocation.parse("benssharks:textures/block/landminedefault.png");
   }
}
