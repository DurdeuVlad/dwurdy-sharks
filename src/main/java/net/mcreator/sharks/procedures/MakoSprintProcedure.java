package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class MakoSprintProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      Entity entity = event.getEntity();
      if (!(entity instanceof MakoSharkEntity)) {
         return;
      }
      if ((entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!(entity instanceof MakoSharkEntity) || !entity.isInWaterOrBubble()) {
         return;
      }
      if (entity instanceof Mob mob && mob.getTarget() instanceof BarracudaEntity target && target.isAlive()) {
         boolean near = target.getBoundingBox().intersects(AABB.ofSize(new Vec3(x, y, z), 24.0, 24.0, 24.0));
         if (near && entity instanceof LivingEntity living && !living.level().isClientSide()) {
            living.addEffect(new MobEffectInstance(BenssharksModMobEffects.FRENZY, 60, 2, true, false));
         } else if (!near && entity instanceof LivingEntity living) {
            living.removeEffect(BenssharksModMobEffects.FRENZY);
         }
      }

      if (entity instanceof LivingEntity living && living.hasEffect(BenssharksModMobEffects.FRENZY)) {
         ((MakoSharkEntity)entity).setAnimation("sprint");
      } else {
         ((MakoSharkEntity)entity).setAnimation("empty");
      }
   }
}
