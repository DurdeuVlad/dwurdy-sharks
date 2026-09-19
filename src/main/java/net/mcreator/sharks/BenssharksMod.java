package net.mcreator.sharks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.mcreator.sharks.init.BenssharksModBlockEntities;
import net.mcreator.sharks.init.BenssharksModBlocks;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.BenssharksModGameRules;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.mcreator.sharks.init.BenssharksModPotions;
import net.mcreator.sharks.init.BenssharksModSounds;
import net.mcreator.sharks.init.BenssharksModTabs;
import net.mcreator.sharks.network.BenssharksModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("dwurdysharks")
public class BenssharksMod {
   public static final Logger LOGGER = LogManager.getLogger(BenssharksMod.class);
   public static final String MODID = "dwurdysharks";
   private static boolean networkingRegistered = false;
   private static final Map<Type<?>, BenssharksMod.NetworkMessage<?>> MESSAGES = new HashMap<>();
   private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

   public BenssharksMod(IEventBus modEventBus) {
      NeoForge.EVENT_BUS.register(this);
      modEventBus.addListener(this::registerNetworking);
      BenssharksModSounds.REGISTRY.register(modEventBus);
      BenssharksModBlocks.REGISTRY.register(modEventBus);
      BenssharksModBlockEntities.REGISTRY.register(modEventBus);
      BenssharksModItems.REGISTRY.register(modEventBus);
      BenssharksModEntities.REGISTRY.register(modEventBus);
      BenssharksModTabs.REGISTRY.register(modEventBus);
      BenssharksModVariables.ATTACHMENT_TYPES.register(modEventBus);
      BenssharksModPotions.REGISTRY.register(modEventBus);
      BenssharksModMobEffects.REGISTRY.register(modEventBus);
      BenssharksModGameRules.register();
   }

   public static <T extends CustomPacketPayload> void addNetworkMessage(
      Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, IPayloadHandler<T> handler
   ) {
      if (networkingRegistered) {
         throw new IllegalStateException("Cannot register new network messages after networking has been registered");
      } else {
         MESSAGES.put(id, new BenssharksMod.NetworkMessage(reader, handler));
      }
   }

   private void registerNetworking(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("dwurdysharks");
      MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional((Type) id, (StreamCodec) networkMessage.reader(), (IPayloadHandler) networkMessage.handler()));
      networkingRegistered = true;
   }

   public static void queueServerWork(int tick, Runnable action) {
      if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
         workQueue.add(new Tuple(action, tick));
      }
   }

   @SubscribeEvent
   public void tick(Post event) {
      List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
      workQueue.forEach(work -> {
         work.setB((Integer)work.getB() - 1);
         if ((Integer)work.getB() == 0) {
            actions.add((Tuple<Runnable, Integer>)work);
         }
      });
      actions.forEach(e -> ((Runnable)e.getA()).run());
      workQueue.removeAll(actions);
   }

   private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? super RegistryFriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
   }
}
