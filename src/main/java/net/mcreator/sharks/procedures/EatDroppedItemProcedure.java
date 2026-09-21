package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class EatDroppedItemProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity == null || (entity.tickCount + entity.getId()) % 10 != 0) {
         return;
      }
      if (entity == null || !entity.getType().is(DwurdySharksEntityTypeTags.DROPPED_FOOD_EATERS)) {
         return;
      }
      DroppedFoodProcedure.execute(world, x, y, z, entity, stack -> true, 2.0F);
   }
}
