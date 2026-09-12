package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.minecraft.world.entity.Entity;

public class GreaterAxodileThisEntityKillsAnotherOneProcedure {
   public static void execute(Entity sourceentity) {
      if (sourceentity != null) {
         if (sourceentity instanceof GreaterAxodileEntity) {
            ((GreaterAxodileEntity)sourceentity).setAnimation("empty");
         }

         sourceentity.stopRiding();
      }
   }
}
