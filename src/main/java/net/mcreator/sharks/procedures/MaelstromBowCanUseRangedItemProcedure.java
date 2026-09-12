package net.mcreator.sharks.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MaelstromBowCanUseRangedItemProcedure {
   public static boolean execute(Entity entity) {
      return entity == null
         ? false
         : entity instanceof Player _plr && _plr.getAbilities().instabuild
            || entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(Items.ARROW));
   }
}
