package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.ThalassogerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ThalassogerModel extends GeoModel<ThalassogerEntity> {
   public ResourceLocation getAnimationResource(ThalassogerEntity entity) {
      return ResourceLocation.parse("benssharks:animations/thalassoger.animation.json");
   }

   public ResourceLocation getModelResource(ThalassogerEntity entity) {
      return ResourceLocation.parse("benssharks:geo/thalassoger.geo.json");
   }

   public ResourceLocation getTextureResource(ThalassogerEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(ThalassogerEntity animatable, long instanceId, AnimationState animationState) {
      GeoBone head = this.getAnimationProcessor().getBone("head");
      if (head != null) {
         EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
         head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
         head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
      }
   }
}
