package net.mcreator.sharks.recipe.brewing;

import java.util.Optional;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.mcreator.sharks.init.DwurdySharksModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber
public class SealingRecipeBrewingRecipe implements IBrewingRecipe {
   @SubscribeEvent
   public static void init(RegisterBrewingRecipesEvent event) {
      event.getBuilder().addRecipe(new SealingRecipeBrewingRecipe());
   }

   public boolean isInput(ItemStack input) {
      Item inputItem = input.getItem();
      Optional<Holder<Potion>> optionalPotion = ((PotionContents)input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
      return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION)
         && optionalPotion.isPresent()
         && optionalPotion.get().is(Potions.AWKWARD);
   }

   public boolean isIngredient(ItemStack ingredient) {
      return Ingredient.of(new ItemStack[]{new ItemStack((ItemLike)DwurdySharksModItems.SUCKER.get())}).test(ingredient);
   }

   public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
      return this.isInput(input) && this.isIngredient(ingredient)
         ? PotionContents.createItemStack(input.getItem(), DwurdySharksModPotions.POTION_OF_SEALING)
         : ItemStack.EMPTY;
   }
}
