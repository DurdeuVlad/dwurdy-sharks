package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.LandSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LandSharkModel extends GeoModel<LandSharkEntity> {
   public ResourceLocation getAnimationResource(LandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:animations/land_shark.animation.json");
   }

   public ResourceLocation getModelResource(LandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:geo/land_shark.geo.json");
   }

   public ResourceLocation getTextureResource(LandSharkEntity entity) {
      return ResourceLocation.parse("dwurdysharks:textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(LandSharkEntity animatable, long instanceId, AnimationState animationState) {
      GeoBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
         head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
         head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
      }
   }
}
