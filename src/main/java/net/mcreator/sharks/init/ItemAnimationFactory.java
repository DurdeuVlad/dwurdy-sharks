package net.mcreator.sharks.init;

import net.mcreator.sharks.item.SpetumItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Post;
import software.bernie.geckolib.animatable.GeoItem;

@EventBusSubscriber
public class ItemAnimationFactory {
   @SubscribeEvent
   public static void animatedItems(Post event) {
      String animation = "";
      ItemStack mainhandItem = event.getEntity().getMainHandItem().copy();
      ItemStack offhandItem = event.getEntity().getOffhandItem().copy();
      if (mainhandItem.getItem() instanceof GeoItem || offhandItem.getItem() instanceof GeoItem) {
         if (mainhandItem.getItem() instanceof SpetumItem animatable) {
            animation = ((CustomData)mainhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag().getString("geckoAnim");
            if (!animation.isEmpty()) {
               CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getMainHandItem(), tag -> tag.putString("geckoAnim", ""));
               if (event.getEntity().level().isClientSide()) {
                  ((SpetumItem)event.getEntity().getMainHandItem().getItem()).animationprocedure = animation;
               }
            }
         }

         if (offhandItem.getItem() instanceof SpetumItem animatablex) {
            animation = ((CustomData)offhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag().getString("geckoAnim");
            if (!animation.isEmpty()) {
               CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getOffhandItem(), tag -> tag.putString("geckoAnim", ""));
               if (event.getEntity().level().isClientSide()) {
                  ((SpetumItem)event.getEntity().getOffhandItem().getItem()).animationprocedure = animation;
               }
            }
         }
      }
   }
}
