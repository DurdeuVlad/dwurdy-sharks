package net.mcreator.sharks.item;

import net.mcreator.sharks.procedures.CookiecutterSharkBucketRightclickedOnBlockProcedure;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;

public class CookiecutterSharkBucketItem extends Item {
   public CookiecutterSharkBucketItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }

   public boolean hasCraftingRemainingItem(ItemStack stack) {
      return true;
   }

   public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
      return new ItemStack(Items.WATER_BUCKET);
   }

   public InteractionResult useOn(UseOnContext context) {
      super.useOn(context);
      CookiecutterSharkBucketRightclickedOnBlockProcedure.execute(
         context.getLevel(),
         context.getClickedPos().getX(),
         context.getClickedPos().getY(),
         context.getClickedPos().getZ(),
         context.getClickedFace(),
         context.getPlayer(),
         context.getItemInHand()
      );
      return InteractionResult.SUCCESS;
   }
}
