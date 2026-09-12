package net.mcreator.sharks.procedures;

import net.minecraft.world.entity.Entity;

public class IfSittingProcedure {
   public static boolean execute(Entity entity) {
      return entity == null ? false : entity.getPersistentData().getBoolean("Sitting");
   }
}
