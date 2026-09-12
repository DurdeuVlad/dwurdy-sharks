package net.mcreator.sharks.entity.model;

import net.mcreator.sharks.entity.AxodileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AxodileModel extends GeoModel<AxodileEntity> {
   public ResourceLocation getAnimationResource(AxodileEntity entity) {
      return ResourceLocation.parse("benssharks:animations/axodile.animation.json");
   }

   public ResourceLocation getModelResource(AxodileEntity entity) {
      return ResourceLocation.parse("benssharks:geo/axodile.geo.json");
   }

   public ResourceLocation getTextureResource(AxodileEntity entity) {
      return ResourceLocation.parse("benssharks:textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(AxodileEntity animatable, long instanceId, AnimationState animationState) {
      GeoBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
         head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
         head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
      }
   }
}
