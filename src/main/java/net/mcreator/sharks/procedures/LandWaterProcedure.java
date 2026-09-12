package net.mcreator.sharks.procedures;

import net.minecraft.world.entity.Entity;

public class LandWaterProcedure {
   public static boolean execute(Entity entity) {
      return entity == null ? false : entity.isInWaterOrBubble();
   }
}
