package net.mcreator.sharks.network;

import net.mcreator.sharks.BenssharksMod;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedData.Factory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class BenssharksModVariables {
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(Keys.ATTACHMENT_TYPES, "dwurdysharks");

   @SubscribeEvent
   public static void init(FMLCommonSetupEvent event) {
      BenssharksMod.addNetworkMessage(
         BenssharksModVariables.SavedDataSyncMessage.TYPE,
         BenssharksModVariables.SavedDataSyncMessage.STREAM_CODEC,
         BenssharksModVariables.SavedDataSyncMessage::handleData
      );
   }

   @EventBusSubscriber
   public static class EventBusVariableHandlers {
      @SubscribeEvent
      public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
         if (event.getEntity() instanceof ServerPlayer player) {
            SavedData mapdata = BenssharksModVariables.MapVariables.get(event.getEntity().level());
            SavedData worlddata = BenssharksModVariables.WorldVariables.get(event.getEntity().level());
            if (mapdata != null) {
               PacketDistributor.sendToPlayer(player, new BenssharksModVariables.SavedDataSyncMessage(0, mapdata), new CustomPacketPayload[0]);
            }

            if (worlddata != null) {
               PacketDistributor.sendToPlayer(player, new BenssharksModVariables.SavedDataSyncMessage(1, worlddata), new CustomPacketPayload[0]);
            }
         }
      }

      @SubscribeEvent
      public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
         if (event.getEntity() instanceof ServerPlayer player) {
            SavedData worlddata = BenssharksModVariables.WorldVariables.get(event.getEntity().level());
            if (worlddata != null) {
               PacketDistributor.sendToPlayer(player, new BenssharksModVariables.SavedDataSyncMessage(1, worlddata), new CustomPacketPayload[0]);
            }
         }
      }
   }

   public static class MapVariables extends SavedData {
      public static final String DATA_NAME = "benssharks_mapvars";
      public double SwimSpeed = 0.0;
      public double KBRes = 0.0;
      public boolean Sitting = false;
      static BenssharksModVariables.MapVariables clientSide = new BenssharksModVariables.MapVariables();

      public static BenssharksModVariables.MapVariables load(CompoundTag tag, Provider lookupProvider) {
         BenssharksModVariables.MapVariables data = new BenssharksModVariables.MapVariables();
         data.read(tag, lookupProvider);
         return data;
      }

      public void read(CompoundTag nbt, Provider lookupProvider) {
         this.SwimSpeed = nbt.getDouble("SwimSpeed");
         this.KBRes = nbt.getDouble("KBRes");
         this.Sitting = nbt.getBoolean("Sitting");
      }

      public CompoundTag save(CompoundTag nbt, Provider lookupProvider) {
         nbt.putDouble("SwimSpeed", this.SwimSpeed);
         nbt.putDouble("KBRes", this.KBRes);
         nbt.putBoolean("Sitting", this.Sitting);
         return nbt;
      }

      public void syncData(LevelAccessor world) {
         this.setDirty();
         if (world instanceof Level && !world.isClientSide()) {
            PacketDistributor.sendToAllPlayers(new BenssharksModVariables.SavedDataSyncMessage(0, this), new CustomPacketPayload[0]);
         }
      }

      public static BenssharksModVariables.MapVariables get(LevelAccessor world) {
         return world instanceof ServerLevelAccessor serverLevelAcc
            ? (BenssharksModVariables.MapVariables)serverLevelAcc.getLevel()
               .getServer()
               .getLevel(Level.OVERWORLD)
               .getDataStorage()
               .computeIfAbsent(new Factory<>(BenssharksModVariables.MapVariables::new, BenssharksModVariables.MapVariables::load, null), "benssharks_mapvars")
            : clientSide;
      }
   }

   public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
      public static final Type<BenssharksModVariables.SavedDataSyncMessage> TYPE = new Type(
         ResourceLocation.fromNamespaceAndPath("dwurdysharks", "saved_data_sync")
      );
      public static final StreamCodec<RegistryFriendlyByteBuf, BenssharksModVariables.SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of(
         (buffer, message) -> {
            buffer.writeInt(message.dataType);
            if (message.data != null) {
               buffer.writeNbt(message.data.save(new CompoundTag(), buffer.registryAccess()));
            }
         }, buffer -> {
            int dataType = buffer.readInt();
            CompoundTag nbt = buffer.readNbt();
            SavedData data = null;
            if (nbt != null) {
               data = (SavedData)(dataType == 0 ? new BenssharksModVariables.MapVariables() : new BenssharksModVariables.WorldVariables());
               if (data instanceof BenssharksModVariables.MapVariables mapVariables) {
                  mapVariables.read(nbt, buffer.registryAccess());
               } else if (data instanceof BenssharksModVariables.WorldVariables worldVariables) {
                  worldVariables.read(nbt, buffer.registryAccess());
               }
            }

            return new BenssharksModVariables.SavedDataSyncMessage(dataType, data);
         }
      );

      public Type<BenssharksModVariables.SavedDataSyncMessage> type() {
         return TYPE;
      }

      public static void handleData(BenssharksModVariables.SavedDataSyncMessage message, IPayloadContext context) {
         if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
            context.enqueueWork(
                  () -> {
                     if (message.dataType == 0) {
                        BenssharksModVariables.MapVariables.clientSide
                           .read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
                     } else {
                        BenssharksModVariables.WorldVariables.clientSide
                           .read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
                     }
                  }
               )
               .exceptionally(e -> {
                  context.connection().disconnect(Component.literal(e.getMessage()));
                  return null;
               });
         }
      }
   }

   public static class WorldVariables extends SavedData {
      public static final String DATA_NAME = "benssharks_worldvars";
      public BlockState REMORABUCKETPLAYERRIGHTCLICKEDONENTITY = Blocks.AIR.defaultBlockState();
      static BenssharksModVariables.WorldVariables clientSide = new BenssharksModVariables.WorldVariables();

      public static BenssharksModVariables.WorldVariables load(CompoundTag tag, Provider lookupProvider) {
         BenssharksModVariables.WorldVariables data = new BenssharksModVariables.WorldVariables();
         data.read(tag, lookupProvider);
         return data;
      }

      public void read(CompoundTag nbt, Provider lookupProvider) {
         this.REMORABUCKETPLAYERRIGHTCLICKEDONENTITY = NbtUtils.readBlockState(
            lookupProvider.lookupOrThrow(BuiltInRegistries.BLOCK.key()), nbt.getCompound("REMORABUCKETPLAYERRIGHTCLICKEDONENTITY")
         );
      }

      public CompoundTag save(CompoundTag nbt, Provider lookupProvider) {
         nbt.put("REMORABUCKETPLAYERRIGHTCLICKEDONENTITY", NbtUtils.writeBlockState(this.REMORABUCKETPLAYERRIGHTCLICKEDONENTITY));
         return nbt;
      }

      public void syncData(LevelAccessor world) {
         this.setDirty();
         if (world instanceof ServerLevel level) {
            PacketDistributor.sendToPlayersInDimension(level, new BenssharksModVariables.SavedDataSyncMessage(1, this), new CustomPacketPayload[0]);
         }
      }

      public static BenssharksModVariables.WorldVariables get(LevelAccessor world) {
         return world instanceof ServerLevel level
            ? (BenssharksModVariables.WorldVariables)level.getDataStorage()
               .computeIfAbsent(new Factory<>(BenssharksModVariables.WorldVariables::new, BenssharksModVariables.WorldVariables::load, null), "benssharks_worldvars")
            : clientSide;
      }
   }
}
