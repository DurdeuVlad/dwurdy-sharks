package net.mcreator.sharks.item;

import net.mcreator.sharks.entity.SeekingArrowEntity;
import net.mcreator.sharks.procedures.MaelstromBowCanUseRangedItemProcedure;
import net.mcreator.sharks.procedures.MaelstromBowEntitySwingsItemProcedure;
import net.mcreator.sharks.procedures.MaelstromBowRangedItemShootsProjectileProcedure;
import net.mcreator.sharks.procedures.MaelstromBowRightclickedProcedure;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class MaelstromBowItem extends Item {
   public MaelstromBowItem() {
      super(new Properties().durability(500).rarity(Rarity.RARE));
   }

   public UseAnim getUseAnimation(ItemStack itemstack) {
      return UseAnim.BOW;
   }

   public int getEnchantmentValue() {
      return 10;
   }

   public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
      return 99999;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
      InteractionResultHolder<ItemStack> ar = InteractionResultHolder.fail(entity.getItemInHand(hand));
      if (MaelstromBowCanUseRangedItemProcedure.execute(entity) && (entity.getAbilities().instabuild || this.findAmmo(entity) != ItemStack.EMPTY)) {
         ar = InteractionResultHolder.success(entity.getItemInHand(hand));
         entity.startUsingItem(hand);
      }

      MaelstromBowRightclickedProcedure.execute(world, entity);
      return ar;
   }

   public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
      boolean retval = super.onEntitySwing(itemstack, entity);
      MaelstromBowEntitySwingsItemProcedure.execute();
      return retval;
   }

   public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
      if (!world.isClientSide() && entity instanceof ServerPlayer player) {
         float pullingPower = BowItem.getPowerForTime(this.getUseDuration(itemstack, player) - time);
         if (pullingPower < 0.1) {
            return;
         }

         ItemStack stack = this.findAmmo(player);
         if (player.getAbilities().instabuild || stack != ItemStack.EMPTY) {
            SeekingArrowEntity projectile = SeekingArrowEntity.shoot(world, entity, world.getRandom(), pullingPower);
            itemstack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
            if (player.getAbilities().instabuild) {
               projectile.pickup = Pickup.CREATIVE_ONLY;
            } else if (stack.isDamageableItem()) {
               if (world instanceof ServerLevel serverLevel) {
                  stack.hurtAndBreak(1, serverLevel, player, _stkprov -> {});
               }
            } else {
               stack.shrink(1);
            }

            MaelstromBowRangedItemShootsProjectileProcedure.execute(world, entity);
         }
      }
   }

   private ItemStack findAmmo(Player player) {
      ItemStack stack = ProjectileWeaponItem.getHeldProjectile(player, e -> e.getItem() == SeekingArrowEntity.PROJECTILE_ITEM.getItem());
      if (stack == ItemStack.EMPTY) {
         for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack teststack = (ItemStack)player.getInventory().items.get(i);
            if (teststack != null && teststack.getItem() == SeekingArrowEntity.PROJECTILE_ITEM.getItem()) {
               stack = teststack;
               break;
            }
         }
      }

      return stack;
   }
}
