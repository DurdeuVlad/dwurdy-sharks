package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksConfig;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public final class SharkDespawnProcedure {
   private static final int CHECK_INTERVAL_TICKS = 40;

   private SharkDespawnProcedure() {
   }

   @SubscribeEvent
   public static void onEntityTick(EntityTickEvent.Pre event) {
      if (event.getEntity().level().isClientSide()
         || !(event.getEntity() instanceof Mob mob)
         || (mob.tickCount + mob.getId()) % CHECK_INTERVAL_TICKS != 0
         || !DwurdySharksConfig.isModEntity(mob.getType())
         || mob.isPersistenceRequired()
         || mob.hasCustomName()
         || (mob instanceof TamableAnimal tamable && tamable.isTame())) {
         return;
      }
      int distance = DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.get();
      if (distance <= 0) {
         return;
      }
      if (mob.level().getNearestPlayer(mob, distance) == null) {
         mob.discard();
      }
   }
}
