package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class EatDroppedItemProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      Entity entity = event.getEntity();
      if (!entity.getType().is(DwurdySharksEntityTypeTags.DROPPED_FOOD_EATERS)) {
         return;
      }
      if ((entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity == null || !entity.getType().is(DwurdySharksEntityTypeTags.DROPPED_FOOD_EATERS)) {
         return;
      }
      DroppedFoodProcedure.execute(world, x, y, z, entity, stack -> true, 2.0F);
   }
}
