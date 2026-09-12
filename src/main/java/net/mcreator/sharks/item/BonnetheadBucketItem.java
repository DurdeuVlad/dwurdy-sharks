package net.mcreator.sharks.item;

import net.mcreator.sharks.procedures.BonnetheadBucketRightclickedOnBlockProcedure;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;

public class BonnetheadBucketItem extends Item {
   public BonnetheadBucketItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }

   public InteractionResult useOn(UseOnContext context) {
      super.useOn(context);
      BonnetheadBucketRightclickedOnBlockProcedure.execute(
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
