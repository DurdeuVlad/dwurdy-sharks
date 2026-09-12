package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class SharkFinItem extends Item {
   public SharkFinItem() {
      super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
   }
}
