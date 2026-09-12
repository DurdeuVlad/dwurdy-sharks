package net.mcreator.sharks.init;

import java.util.List;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber
public class BenssharksModTrades {
   @SubscribeEvent
   public static void registerTrades(VillagerTradesEvent event) {
      if (event.getType() == VillagerProfession.SHEPHERD) {
         ((List)event.getTrades().get(4))
            .add(new BasicItemListing(new ItemStack(Items.EMERALD, 29), new ItemStack((ItemLike)BenssharksModBlocks.SHARK_PLUSH_BLOCK.get()), 10, 10, 0.05F));
      }
   }
}
