package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.RollParticleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RollParticleModel extends GeoModel<RollParticleEntity> {
   public ResourceLocation getAnimationResource(RollParticleEntity entity) {
      return ResourceLocation.parse("benssharks:animations/roll_particle.animation.json");
   }

   public ResourceLocation getModelResource(RollParticleEntity entity) {
      return ResourceLocation.parse("benssharks:geo/roll_particle.geo.json");
   }

   public ResourceLocation getTextureResource(RollParticleEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(RollParticleEntity animatable, long instanceId, AnimationState animationState) {
      GeoBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
         head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
         head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
      }
   }
}
