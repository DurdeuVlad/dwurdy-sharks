package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class EggCapsuleItem extends Item {
   public EggCapsuleItem() {
      super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
   }
}
