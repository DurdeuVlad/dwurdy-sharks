package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.entity.LandMineTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LandMineBlockModel extends GeoModel<LandMineTileEntity> {
   public ResourceLocation getAnimationResource(LandMineTileEntity animatable) {
      return ResourceLocation.parse("dwurdysharks:animations/landmine.animation.json");
   }

   public ResourceLocation getModelResource(LandMineTileEntity animatable) {
      return ResourceLocation.parse("dwurdysharks:geo/landmine.geo.json");
   }

   public ResourceLocation getTextureResource(LandMineTileEntity animatable) {
      return ResourceLocation.parse("dwurdysharks:textures/block/landminedefault.png");
   }
}
