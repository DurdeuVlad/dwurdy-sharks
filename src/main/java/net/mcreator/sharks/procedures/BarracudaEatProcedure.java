package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class BarracudaEatProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity == null || (entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      if (!(entity instanceof BarracudaEntity)) {
         return;
      }
      DroppedFoodProcedure.execute(world, x, y, z, entity,
         stack -> stack.getItem() != BenssharksModItems.RAW_BARRACUDA.get()
            && stack.getItem() != BenssharksModItems.COOKED_BARRACUDA.get(),
         0.0F);
   }
}
