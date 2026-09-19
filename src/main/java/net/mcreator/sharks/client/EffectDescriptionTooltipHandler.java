package net.mcreator.sharks.client;

import net.mcreator.sharks.BenssharksMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = BenssharksMod.MODID, value = Dist.CLIENT)
public class EffectDescriptionTooltipHandler {
   @SubscribeEvent
   public static void onGatherEffectTooltips(GatherEffectScreenTooltipsEvent event) {
      String key = event.getEffectInstance().getDescriptionId() + ".description";
      if (Language.getInstance().has(key)) {
         event.getTooltip().add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
      }
   }

   @SubscribeEvent
   public static void onItemTooltip(ItemTooltipEvent event) {
      PotionContents contents = event.getItemStack().get(DataComponents.POTION_CONTENTS);
      if (contents == null) {
         return;
      }

      for (MobEffectInstance instance : contents.getAllEffects()) {
         String key = instance.getDescriptionId() + ".description";
         if (Language.getInstance().has(key)) {
            event.getToolTip().add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
         }
      }
   }
}
