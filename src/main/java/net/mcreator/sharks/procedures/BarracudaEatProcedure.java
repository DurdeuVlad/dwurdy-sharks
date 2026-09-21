package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class BarracudaEatProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      Entity entity = event.getEntity();
      if (!(entity instanceof BarracudaEntity)) {
         return;
      }
      if ((entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!(entity instanceof BarracudaEntity)) {
         return;
      }
      DroppedFoodProcedure.execute(world, x, y, z, entity,
         stack -> stack.getItem() != BenssharksModItems.RAW_BARRACUDA.get()
            && stack.getItem() != BenssharksModItems.COOKED_BARRACUDA.get(),
         0.0F);
   }
}
