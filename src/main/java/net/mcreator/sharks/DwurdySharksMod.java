package net.mcreator.sharks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.mcreator.sharks.init.DwurdySharksModBlockEntities;
import net.mcreator.sharks.init.DwurdySharksModBlocks;
import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksModGameRules;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.mcreator.sharks.init.DwurdySharksModPotions;
import net.mcreator.sharks.init.DwurdySharksModSounds;
import net.mcreator.sharks.init.DwurdySharksModTabs;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.network.DwurdySharksModVariables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("dwurdysharks")
public class DwurdySharksMod {
   public static final Logger LOGGER = LogManager.getLogger(DwurdySharksMod.class);
   public static final String MODID = "dwurdysharks";
   private static boolean networkingRegistered = false;
   private static final Map<Type<?>, DwurdySharksMod.NetworkMessage<?>> MESSAGES = new HashMap<>();
   private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

   public DwurdySharksMod(IEventBus modEventBus, ModContainer modContainer) {
      NeoForge.EVENT_BUS.register(this);
      modEventBus.addListener(this::registerNetworking);
      modContainer.registerConfig(ModConfig.Type.SERVER, DwurdySharksConfig.SPEC);
      DwurdySharksModSounds.REGISTRY.register(modEventBus);
      DwurdySharksModBlocks.REGISTRY.register(modEventBus);
      DwurdySharksModBlockEntities.REGISTRY.register(modEventBus);
      DwurdySharksModItems.REGISTRY.register(modEventBus);
      DwurdySharksModEntities.REGISTRY.register(modEventBus);
      DwurdySharksModTabs.REGISTRY.register(modEventBus);
      DwurdySharksModVariables.ATTACHMENT_TYPES.register(modEventBus);
      DwurdySharksModPotions.REGISTRY.register(modEventBus);
      DwurdySharksModMobEffects.REGISTRY.register(modEventBus);
      DwurdySharksModGameRules.register();
   }

   public static <T extends CustomPacketPayload> void addNetworkMessage(
      Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, IPayloadHandler<T> handler
   ) {
      if (networkingRegistered) {
         throw new IllegalStateException("Cannot register new network messages after networking has been registered");
      } else {
         MESSAGES.put(id, new DwurdySharksMod.NetworkMessage(reader, handler));
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

   public static int getPendingServerWork() {
      return workQueue.size();
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
