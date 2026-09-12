package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GreaterAxodileModel extends GeoModel<GreaterAxodileEntity> {
   public ResourceLocation getAnimationResource(GreaterAxodileEntity entity) {
      return ResourceLocation.parse("benssharks:animations/greateraxodile.animation.json");
   }

   public ResourceLocation getModelResource(GreaterAxodileEntity entity) {
      return ResourceLocation.parse("benssharks:geo/greateraxodile.geo.json");
   }

   public ResourceLocation getTextureResource(GreaterAxodileEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(GreaterAxodileEntity animatable, long instanceId, AnimationState animationState) {
      GeoBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
         head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
         head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
      }
   }
}
