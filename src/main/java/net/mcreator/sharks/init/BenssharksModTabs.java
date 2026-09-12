package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BenssharksModTabs {
   public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "benssharks");
   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BENS_SHARKS = REGISTRY.register(
      "bens_sharks",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("item_group.benssharks.bens_sharks"))
         .icon(() -> new ItemStack((ItemLike)BenssharksModBlocks.SHARK_PLUSH_BLOCK.get()))
         .displayItems((parameters, tabData) -> {
            tabData.accept((ItemLike)BenssharksModItems.AXODILE_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BASKING_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BARRACUDA_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BLACKTIP_REEF_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BLUE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BONNETHEAD_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.BULL_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.COOKIECUTTER_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.GREATER_AXODILE_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.GREATWHITESHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.GREENLAND_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.KRILL_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.LAND_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.LEMON_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.MAKO_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.MEGALODON_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.NURSE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.TIGER_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.PILOT_FISH_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.REMORA_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.THALASSOGER_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.WHALE_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.WHITETIP_SHARK_SPAWN_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.AXOLEATHER.get());
            tabData.accept((ItemLike)BenssharksModItems.AXOSCUTE.get());
            tabData.accept((ItemLike)BenssharksModItems.BARRACUDA_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.BLACKTIP_REEF_SHARK_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.BONNETHEAD_SHARK_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.COOKIECUTTER_SHARK_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.PILOT_FISH_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.REMORA_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.CELLOPHANE_NOODLES.get());
            tabData.accept((ItemLike)BenssharksModItems.COOKIECUTTER_SHARK_LIVE.get());
            tabData.accept((ItemLike)BenssharksModItems.FISH_BROTH.get());
            tabData.accept((ItemLike)BenssharksModItems.FISH_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.JAGGED_HELMET.get());
            tabData.accept((ItemLike)BenssharksModItems.JAGGED_CHESTPLATE.get());
            tabData.accept((ItemLike)BenssharksModItems.JAGGED_LEGGINGS.get());
            tabData.accept((ItemLike)BenssharksModItems.JAGGED_BOOTS.get());
            tabData.accept((ItemLike)BenssharksModItems.KRILL_ITEM.get());
            tabData.accept((ItemLike)BenssharksModItems.KRILL_BUCKET.get());
            tabData.accept((ItemLike)BenssharksModItems.KRILL_CAKE.get());
            tabData.accept((ItemLike)BenssharksModItems.KRILL_NOODLES.get());
            tabData.accept(((Block)BenssharksModBlocks.LAND_MINE.get()).asItem());
            tabData.accept((ItemLike)BenssharksModItems.DENT.get());
            tabData.accept((ItemLike)BenssharksModItems.MAELSTROM.get());
            tabData.accept((ItemLike)BenssharksModItems.MAELSTROM_BOW.get());
            tabData.accept((ItemLike)BenssharksModItems.SHARK_TOOTH_CLUB.get());
            tabData.accept((ItemLike)BenssharksModItems.SPETUM.get());
            tabData.accept((ItemLike)BenssharksModItems.RAW_BARRACUDA.get());
            tabData.accept((ItemLike)BenssharksModItems.COOKED_BARRACUDA.get());
            tabData.accept((ItemLike)BenssharksModItems.RAW_PILOT_FISH.get());
            tabData.accept((ItemLike)BenssharksModItems.COOKED_PILOT_FISH.get());
            tabData.accept((ItemLike)BenssharksModItems.SHARK_FIN.get());
            tabData.accept(((Block)BenssharksModBlocks.SHARK_PLUSH_BLOCK.get()).asItem());
            tabData.accept((ItemLike)BenssharksModItems.SHARK_TOOTH.get());
            tabData.accept((ItemLike)BenssharksModItems.MEGALODON_TOOTH.get());
            tabData.accept((ItemLike)BenssharksModItems.SHARK_FIN_SOUP.get());
            tabData.accept((ItemLike)BenssharksModItems.SOUP_SHARKFIN.get());
            tabData.accept((ItemLike)BenssharksModItems.STARCH.get());
            tabData.accept((ItemLike)BenssharksModItems.SUCKER.get());
            tabData.accept((ItemLike)BenssharksModItems.MUTATED_EGG.get());
            tabData.accept((ItemLike)BenssharksModItems.EGG_CAPSULE.get());
         })
         .build()
   );
}
