package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class MaelstromItem extends Item {
   public MaelstromItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.RARE));
   }
}
