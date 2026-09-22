package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DwurdySharksModTabs {
   public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dwurdysharks");
   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DWURDY_SHARKS = REGISTRY.register(
      "dwurdy_sharks",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("item_group.dwurdysharks.dwurdy_sharks"))
         .icon(() -> new ItemStack((ItemLike)DwurdySharksModBlocks.SHARK_PLUSH_BLOCK.get()))
         .displayItems((parameters, tabData) -> {
            tabData.accept((ItemLike)DwurdySharksModItems.AXODILE_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BASKING_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BARRACUDA_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BLACKTIP_REEF_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BLUE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BONNETHEAD_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BULL_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.COOKIECUTTER_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.GREATER_AXODILE_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.GREATWHITESHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.GREENLAND_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.KRILL_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.LAND_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.LEMON_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MAKO_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MEGALODON_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.NURSE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.TIGER_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.PILOT_FISH_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.REMORA_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.THALASSOGER_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.WHALE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.WHITETIP_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.AXOLEATHER.get());
            tabData.accept((ItemLike)DwurdySharksModItems.AXOSCUTE.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BARRACUDA_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BLACKTIP_REEF_SHARK_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.BONNETHEAD_SHARK_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.COOKIECUTTER_SHARK_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.PILOT_FISH_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.REMORA_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.CELLOPHANE_NOODLES.get());
            tabData.accept((ItemLike)DwurdySharksModItems.COOKIECUTTER_SHARK_LIVE.get());
            tabData.accept((ItemLike)DwurdySharksModItems.FISH_BROTH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.FISH_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.JAGGED_HELMET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.JAGGED_CHESTPLATE.get());
            tabData.accept((ItemLike)DwurdySharksModItems.JAGGED_LEGGINGS.get());
            tabData.accept((ItemLike)DwurdySharksModItems.JAGGED_BOOTS.get());
            tabData.accept((ItemLike)DwurdySharksModItems.KRILL_ITEM.get());
            tabData.accept((ItemLike)DwurdySharksModItems.KRILL_BUCKET.get());
            tabData.accept((ItemLike)DwurdySharksModItems.KRILL_CAKE.get());
            tabData.accept((ItemLike)DwurdySharksModItems.KRILL_NOODLES.get());
            tabData.accept(((Block)DwurdySharksModBlocks.LAND_MINE.get()).asItem());
            tabData.accept((ItemLike)DwurdySharksModItems.DENT.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MAELSTROM.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MAELSTROM_BOW.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SHARK_TOOTH_CLUB.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SPETUM.get());
            tabData.accept((ItemLike)DwurdySharksModItems.RAW_BARRACUDA.get());
            tabData.accept((ItemLike)DwurdySharksModItems.COOKED_BARRACUDA.get());
            tabData.accept((ItemLike)DwurdySharksModItems.RAW_PILOT_FISH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.COOKED_PILOT_FISH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SHARK_FIN.get());
            tabData.accept(((Block)DwurdySharksModBlocks.SHARK_PLUSH_BLOCK.get()).asItem());
            tabData.accept((ItemLike)DwurdySharksModItems.SHARK_TOOTH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MEGALODON_TOOTH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SHARK_FIN_SOUP.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SOUP_SHARKFIN.get());
            tabData.accept((ItemLike)DwurdySharksModItems.STARCH.get());
            tabData.accept((ItemLike)DwurdySharksModItems.SUCKER.get());
            tabData.accept((ItemLike)DwurdySharksModItems.MUTATED_EGG.get());
            tabData.accept((ItemLike)DwurdySharksModItems.EGG_CAPSULE.get());
         })
         .build()
   );
}
