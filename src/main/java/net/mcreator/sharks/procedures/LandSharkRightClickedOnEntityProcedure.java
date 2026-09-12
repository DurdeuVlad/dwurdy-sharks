package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;

@EventBusSubscriber
public class LandSharkRightClickedOnEntityProcedure {
   @SubscribeEvent
   public static void onEntityTamed(AnimalTameEvent event) {
      execute(event, event.getAnimal(), event.getTamer());
   }

   public static void execute(Entity entity, Entity sourceentity) {
      execute(null, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         boolean Sitting = false;
         double MovementSpeed = 0.0;
         if (entity instanceof TamableAnimal _tamIsTamedBy && sourceentity instanceof LivingEntity _livEnt && _tamIsTamedBy.isOwnedBy(_livEnt)) {
            entity.getPersistentData().putBoolean("Sitting", !entity.getPersistentData().getBoolean("Sitting"));
            entity.setShiftKeyDown(true);
            if (entity.getPersistentData().getBoolean("Sitting")) {
               entity.setShiftKeyDown(false);
            }
         }
      }
   }
}
