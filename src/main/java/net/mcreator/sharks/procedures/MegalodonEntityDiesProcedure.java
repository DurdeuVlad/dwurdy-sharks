package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.MegalodonEntity;
import net.minecraft.world.entity.Entity;

public class MegalodonEntityDiesProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof MegalodonEntity) {
            ((MegalodonEntity)entity).setAnimation("death");
         }
      }
   }
}
