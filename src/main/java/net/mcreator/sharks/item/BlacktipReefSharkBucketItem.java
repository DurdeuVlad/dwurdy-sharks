package net.mcreator.sharks.item;

import net.mcreator.sharks.procedures.BlacktipReefSharkBucketRightclickedOnBlockProcedure;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;

public class BlacktipReefSharkBucketItem extends Item {
   public BlacktipReefSharkBucketItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }

   public InteractionResult useOn(UseOnContext context) {
      super.useOn(context);
      BlacktipReefSharkBucketRightclickedOnBlockProcedure.execute(
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
