package net.mcreator.sharks.block.model;

import net.mcreator.sharks.block.entity.SharkPlushieTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SharkPlushieBlockModel extends GeoModel<SharkPlushieTileEntity> {
   public ResourceLocation getAnimationResource(SharkPlushieTileEntity animatable) {
      return ResourceLocation.parse("benssharks:animations/blahaj.animation.json");
   }

   public ResourceLocation getModelResource(SharkPlushieTileEntity animatable) {
      return ResourceLocation.parse("benssharks:geo/blahaj.geo.json");
   }

   public ResourceLocation getTextureResource(SharkPlushieTileEntity animatable) {
      return ResourceLocation.parse("benssharks:textures/block/blahaj.png");
   }
}
