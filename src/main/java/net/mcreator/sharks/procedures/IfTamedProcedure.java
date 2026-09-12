package net.mcreator.sharks.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;

public class IfTamedProcedure {
   public static boolean execute(Entity entity) {
      if (entity == null) {
         return false;
      } else {
         boolean attack = false;
         boolean ignore = false;
         return !((entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) instanceof TamableAnimal _tamEnt && _tamEnt.isTame());
      }
   }
}
