package net.mcreator.sharks.init;

import net.mcreator.sharks.client.model.ModelJaggedArmorJava;
import net.mcreator.sharks.client.model.ModelSeekingArrow;
import net.mcreator.sharks.client.model.Modelunknown;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;

@EventBusSubscriber(
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class DwurdySharksModModels {
   @SubscribeEvent
   public static void registerLayerDefinitions(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(ModelSeekingArrow.LAYER_LOCATION, ModelSeekingArrow::createBodyLayer);
      event.registerLayerDefinition(ModelJaggedArmorJava.LAYER_LOCATION, ModelJaggedArmorJava::createBodyLayer);
      event.registerLayerDefinition(Modelunknown.LAYER_LOCATION, Modelunknown::createBodyLayer);
   }
}
