package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.CookiecutterSharkEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class CookieGlobalProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity(), event.getSource().getEntity());
      }
   }

   public static void execute(Entity entity, Entity sourceentity) {
      execute(null, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (entity instanceof LivingEntity
            && !(entity instanceof CookiecutterSharkEntity)
            && !(entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(BenssharksModMobEffects.PARASITE))
            && sourceentity instanceof CookiecutterSharkEntity
            && !(
               entity instanceof LivingEntity _livEnt4
                  && _livEnt4.hasEffect(BenssharksModMobEffects.PARASITE)
                  && entity instanceof LivingEntity _livEnt5
                  && _livEnt5.hasEffect(BenssharksModMobEffects.SEALING)
            )) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.PARASITE, 200, 0, true, false));
            }

            if (!sourceentity.level().isClientSide()) {
               sourceentity.discard();
            }
         }
      }
   }
}
