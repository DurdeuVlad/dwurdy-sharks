package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
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
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class MakoSprintProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      execute(null, world, x, y, z, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof MakoSharkEntity && entity.isInWaterOrBubble()) {
            if ((entity instanceof Mob _mobEntx ? _mobEntx.getTarget() : null) instanceof BarracudaEntity
               && (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null).isAlive()) {
               if (!world.getEntitiesOfClass(BarracudaEntity.class, AABB.ofSize(new Vec3(x, y, z), 24.0, 24.0, 24.0), e -> true).isEmpty()) {
                  if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.FRENZY, 60, 2, true, false));
                  }
               } else if (entity instanceof LivingEntity _entity) {
                  _entity.removeEffect(BenssharksModMobEffects.FRENZY);
               }
            }

            if (entity instanceof LivingEntity _livEnt9 && _livEnt9.hasEffect(BenssharksModMobEffects.FRENZY)) {
               if (entity instanceof MakoSharkEntity) {
                  ((MakoSharkEntity)entity).setAnimation("sprint");
               }
            } else if (entity instanceof MakoSharkEntity) {
               ((MakoSharkEntity)entity).setAnimation("empty");
            }
         }
      }
   }
}
