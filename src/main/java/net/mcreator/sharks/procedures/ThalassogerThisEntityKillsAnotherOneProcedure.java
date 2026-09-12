package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.ThalassogerEntity;
import net.minecraft.world.entity.Entity;

public class ThalassogerThisEntityKillsAnotherOneProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof ThalassogerEntity) {
            ((ThalassogerEntity)entity).setAnimation("empty");
         }
      }
   }
}
