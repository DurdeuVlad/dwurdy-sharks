package net.mcreator.sharks.client;

import java.util.List;
import net.mcreator.sharks.DwurdySharksMod;
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

@EventBusSubscriber(modid = DwurdySharksMod.MODID, value = Dist.CLIENT)
public class EffectDescriptionTooltipHandler {
   @SubscribeEvent
   public static void onGatherEffectTooltips(GatherEffectScreenTooltipsEvent event) {
      addDescription(event.getEffectInstance(), event.getTooltip());
   }

   @SubscribeEvent
   public static void onItemTooltip(ItemTooltipEvent event) {
      PotionContents contents = event.getItemStack().get(DataComponents.POTION_CONTENTS);
      if (contents == null) {
         return;
      }

      for (MobEffectInstance instance : contents.getAllEffects()) {
         addDescription(instance, event.getToolTip());
      }
   }

   private static void addDescription(MobEffectInstance instance, List<Component> tooltip) {
      String key = instance.getDescriptionId() + ".description";
      if (key.startsWith("effect." + DwurdySharksMod.MODID + ".") && Language.getInstance().has(key)) {
         tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
      }
   }
}
