package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.RemoraEntity;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class RemoraRightClickedOnEntityProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         double velocityY = 0.0;
         double posX = 0.0;
         double posY = 0.0;
         double posZ = 0.0;
         double health = 0.0;
         double velocityX = 0.0;
         double pitch = 0.0;
         double velocityZ = 0.0;
         double oxygen = 0.0;
         double yaw = 0.0;
         String nametag = "";
         String uuid = "";
         if (entity instanceof RemoraEntity && sourceentity instanceof Player) {
            if (entity instanceof LivingEntity _livEnt) {
               double var10000 = _livEnt.getHealth();
            } else {
               double var73 = -1.0;
            }

            oxygen = entity.getAirSupply();
            nametag = entity.getDisplayName().getString();
            uuid = entity.getStringUUID();
            if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.WATER_BUCKET) {
               if (sourceentity instanceof LivingEntity _entity) {
                  _entity.swing(InteractionHand.MAIN_HAND, true);
               }

               if (sourceentity instanceof LivingEntity _entity) {
                  ItemStack _setstack = new ItemStack((ItemLike)DwurdySharksModItems.REMORA_BUCKET.get()).copy();
                  _setstack.setCount(1);
                  _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                  if (_entity instanceof Player _player) {
                     _player.getInventory().setChanged();
                  }
               }

               (sourceentity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY)
                  .set(DataComponents.CUSTOM_NAME, Component.literal(entity.getDisplayName().getString()));
               String _tagName = "health";
               double _tagValue;
               double var76 = _tagValue = entity instanceof LivingEntity _livEntxx ? _livEntxx.getHealth() : -1.0;
               CustomData.update(
                  DataComponents.CUSTOM_DATA,
                  sourceentity instanceof LivingEntity _livEntxxx ? _livEntxxx.getMainHandItem() : ItemStack.EMPTY,
                  tag -> tag.putDouble("health", _tagValue)
               );
               _tagName = "nametag";
               String _tagValuex = entity.getDisplayName().getString();
               CustomData.update(
                  DataComponents.CUSTOM_DATA,
                  sourceentity instanceof LivingEntity _livEntxxxx ? _livEntxxxx.getMainHandItem() : ItemStack.EMPTY,
                  tag -> tag.putString("nametag", _tagValuex)
               );
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.fill_fish")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.fill_fish")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }

               if (sourceentity instanceof ServerPlayer _player) {
                  AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tactical_fishing"));
                  if (_adv != null) {
                     AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                     if (!_ap.isDone()) {
                        for (String criteria : _ap.getRemainingCriteria()) {
                           _player.getAdvancements().award(_adv, criteria);
                        }
                     }
                  }
               }
            } else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == Items.WATER_BUCKET) {
               if (sourceentity instanceof LivingEntity _entityx) {
                  _entityx.swing(InteractionHand.OFF_HAND, true);
               }

               if (sourceentity instanceof LivingEntity _entityx) {
                  ItemStack _setstack = new ItemStack((ItemLike)DwurdySharksModItems.REMORA_BUCKET.get()).copy();
                  _setstack.setCount(1);
                  _entityx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                  if (_entityx instanceof Player _playerx) {
                     _playerx.getInventory().setChanged();
                  }
               }

               (sourceentity instanceof LivingEntity _livEntxxxxx ? _livEntxxxxx.getOffhandItem() : ItemStack.EMPTY)
                  .set(DataComponents.CUSTOM_NAME, Component.literal(entity.getDisplayName().getString()));
               String _tagNamex = "health";
               double _tagValuexx;
               double var79 = _tagValuexx = entity instanceof LivingEntity _livEntxxxxxx ? _livEntxxxxxx.getHealth() : -1.0;
               CustomData.update(
                  DataComponents.CUSTOM_DATA,
                  sourceentity instanceof LivingEntity _livEntxxxxxxx ? _livEntxxxxxxx.getOffhandItem() : ItemStack.EMPTY,
                  tag -> tag.putDouble("health", _tagValuexx)
               );
               _tagNamex = "nametag";
               String _tagValuexxx = entity.getDisplayName().getString();
               CustomData.update(
                  DataComponents.CUSTOM_DATA,
                  sourceentity instanceof LivingEntity _livEntxxxxxxxx ? _livEntxxxxxxxx.getOffhandItem() : ItemStack.EMPTY,
                  tag -> tag.putString("nametag", _tagValuexxx)
               );
               if (world instanceof Level _levelx) {
                  if (!_levelx.isClientSide()) {
                     _levelx.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.fill_fish")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelx.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.fill_fish")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }

               if (sourceentity instanceof ServerPlayer _playerx) {
                  AdvancementHolder _adv = _playerx.server.getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tactical_fishing"));
                  if (_adv != null) {
                     AdvancementProgress _ap = _playerx.getAdvancements().getOrStartProgress(_adv);
                     if (!_ap.isDone()) {
                        for (String criteria : _ap.getRemainingCriteria()) {
                           _playerx.getAdvancements().award(_adv, criteria);
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
