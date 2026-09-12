package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class AxoscuteItem extends Item {
   public AxoscuteItem() {
      super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
   }
}
