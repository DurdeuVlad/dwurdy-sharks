package net.mcreator.sharks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class KrillBucketItem extends Item {
   public KrillBucketItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }
}
