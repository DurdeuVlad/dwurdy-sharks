package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class MutatedEggItem extends Item {
   public MutatedEggItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.RARE));
   }
}
