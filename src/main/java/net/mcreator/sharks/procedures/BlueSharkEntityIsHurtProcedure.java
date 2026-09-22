package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.DwurdySharksMod;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class BlueSharkEntityIsHurtProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity().level(), event.getEntity());
      }
   }

   public static void execute(LevelAccessor world, Entity entity) {
      execute(null, world, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof BlueSharkEntity && entity.isInWaterOrBubble()) {
            if (entity instanceof BlueSharkEntity) {
               ((BlueSharkEntity)entity).setAnimation("sprint");
            }

            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, true, false));
            }

            DwurdySharksMod.queueServerWork(600, () -> {

               if (entity instanceof BlueSharkEntity) {
                  ((BlueSharkEntity)entity).setAnimation("empty");
               }
            });
         }
      }
   }
}
