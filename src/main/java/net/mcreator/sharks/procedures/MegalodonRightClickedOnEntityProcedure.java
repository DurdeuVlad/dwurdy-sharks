package net.mcreator.sharks.procedures;

import net.mcreator.sharks.DwurdySharksMod;
import net.mcreator.sharks.entity.MegalodonEntity;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class MegalodonRightClickedOnEntityProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof Player && entity instanceof MegalodonEntity) {
            if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == DwurdySharksModItems.FISH_BUCKET.get()) {
               if (!entity.getPersistentData().getBoolean("canBeMilked")) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.swing(InteractionHand.MAIN_HAND, true);
                  }

                  if (sourceentity instanceof LivingEntity _entity) {
                     _entity.swing(InteractionHand.MAIN_HAND, true);
                  }

                  if (sourceentity instanceof LivingEntity _entity) {
                     ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                     _setstack.setCount(1);
                     _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                     if (_entity instanceof Player _player) {
                        _player.getInventory().setChanged();
                     }
                  }

                  entity.getPersistentData().putBoolean("canBeMilked", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (sourceentity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.JAWS, 6000, 0));
                  }

                  if (sourceentity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FRENZY, 6000, 2));
                  }

                  if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, true, false));
                  }

                  if (world instanceof ServerLevel _levelx) {
                     _levelx.sendParticles(ParticleTypes.CRIT, x, y, z, 5, 1.0, 1.0, 1.0, 1.0);
                  }

                  DwurdySharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
               }
            } else if ((sourceentity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY).getItem()
                  == DwurdySharksModItems.FISH_BUCKET.get()
               && !entity.getPersistentData().getBoolean("canBeMilked")) {
               if (entity instanceof LivingEntity _entityx) {
                  _entityx.swing(InteractionHand.MAIN_HAND, true);
               }

               if (sourceentity instanceof LivingEntity _entityx) {
                  _entityx.swing(InteractionHand.OFF_HAND, true);
               }

               if (sourceentity instanceof LivingEntity _entityx) {
                  ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                  _setstack.setCount(1);
                  _entityx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().setChanged();
                  }
               }

               entity.getPersistentData().putBoolean("canBeMilked", true);
               if (world instanceof Level _levelx) {
                  if (!_levelx.isClientSide()) {
                     _levelx.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelx.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (sourceentity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.JAWS, 6000, 0));
               }

               if (sourceentity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FRENZY, 6000, 2));
               }

               if (entity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, true, false));
               }

               if (world instanceof ServerLevel _levelxx) {
                  _levelxx.sendParticles(ParticleTypes.CRIT, x, y, z, 5, 1.0, 1.0, 1.0, 1.0);
               }

               DwurdySharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
            }

            if ((sourceentity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY).getItem()
               == DwurdySharksModItems.EGG_CAPSULE.get()) {
               if (!entity.getPersistentData().getBoolean("canBeMilked")) {
                  if (entity instanceof LivingEntity _entityxx) {
                     _entityxx.swing(InteractionHand.MAIN_HAND, true);
                  }

                  if (sourceentity instanceof LivingEntity _entityxx) {
                     _entityxx.swing(InteractionHand.MAIN_HAND, true);
                  }

                  if (sourceentity instanceof Player _player) {
                     ItemStack _stktoremove;
                     ItemStack var69 = _stktoremove = sourceentity instanceof LivingEntity _livEntxx ? _livEntxx.getMainHandItem() : ItemStack.EMPTY;
                     _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                  }

                  entity.getPersistentData().putBoolean("canBeMilked", true);
                  if (world instanceof Level _levelxx) {
                     if (!_levelxx.isClientSide()) {
                        _levelxx.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _levelxx.playLocalSound(
                           x,
                           y,
                           z,
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _levelxxx) {
                     if (!_levelxxx.isClientSide()) {
                        _levelxxx.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.zombie_villager.cure")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _levelxxx.playLocalSound(
                           x,
                           y,
                           z,
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.zombie_villager.cure")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (sourceentity instanceof ServerPlayer _player) {
                     AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("dwurdysharks:land_shark_achievement"));
                     if (_adv != null) {
                        AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                        if (!_ap.isDone()) {
                           for (String criteria : _ap.getRemainingCriteria()) {
                              _player.getAdvancements().award(_adv, criteria);
                           }
                        }
                     }
                  }

                  if (entity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                     _entityxx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FERTILIZED, 13000, 0, true, false));
                  }

                  DwurdySharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
               }
            } else if ((sourceentity instanceof LivingEntity _livEntxx ? _livEntxx.getOffhandItem() : ItemStack.EMPTY).getItem()
                  == DwurdySharksModItems.EGG_CAPSULE.get()
               && !entity.getPersistentData().getBoolean("canBeMilked")) {
               if (entity instanceof LivingEntity _entityxx) {
                  _entityxx.swing(InteractionHand.MAIN_HAND, true);
               }

               if (sourceentity instanceof LivingEntity _entityxx) {
                  _entityxx.swing(InteractionHand.OFF_HAND, true);
               }

               if (sourceentity instanceof Player _playerx) {
                  ItemStack _stktoremove;
                  ItemStack var68 = _stktoremove = sourceentity instanceof LivingEntity _livEntxxx ? _livEntxxx.getOffhandItem() : ItemStack.EMPTY;
                  _playerx.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _playerx.inventoryMenu.getCraftSlots());
               }

               entity.getPersistentData().putBoolean("canBeMilked", true);
               if (world instanceof Level _levelxxxx) {
                  if (!_levelxxxx.isClientSide()) {
                     _levelxxxx.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelxxxx.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.ravager.attack")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (world instanceof Level _levelxxxxx) {
                  if (!_levelxxxxx.isClientSide()) {
                     _levelxxxxx.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.zombie_villager.cure")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelxxxxx.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.zombie_villager.cure")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (sourceentity instanceof ServerPlayer _playerx) {
                  AdvancementHolder _adv = _playerx.server.getAdvancements().get(ResourceLocation.parse("dwurdysharks:land_shark_achievement"));
                  if (_adv != null) {
                     AdvancementProgress _ap = _playerx.getAdvancements().getOrStartProgress(_adv);
                     if (!_ap.isDone()) {
                        for (String criteria : _ap.getRemainingCriteria()) {
                           _playerx.getAdvancements().award(_adv, criteria);
                        }
                     }
                  }
               }

               if (entity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.FERTILIZED, 13000, 0, true, false));
               }

               DwurdySharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
            }

            if ((sourceentity instanceof LivingEntity _livEntxxx ? _livEntxxx.getMainHandItem() : ItemStack.EMPTY).getItem()
                  != DwurdySharksModItems.FISH_BUCKET.get()
               && (sourceentity instanceof LivingEntity _livEntxx ? _livEntxx.getMainHandItem() : ItemStack.EMPTY).getItem()
                  != DwurdySharksModItems.EGG_CAPSULE.get()) {
               if ((
                     (sourceentity instanceof LivingEntity _livEntxxxxx ? _livEntxxxxx.getOffhandItem() : ItemStack.EMPTY).getItem()
                           == DwurdySharksModItems.FISH_BUCKET.get()
                        || (sourceentity instanceof LivingEntity _livEntxxxx ? _livEntxxxx.getOffhandItem() : ItemStack.EMPTY).getItem()
                           == DwurdySharksModItems.EGG_CAPSULE.get()
                  )
                  && sourceentity instanceof Player _playerxx
                  && !_playerxx.level().isClientSide()) {
                  _playerxx.displayClientMessage(Component.literal("Feeding on Cooldown"), true);
               }
            } else if (sourceentity instanceof Player _playerxx && !_playerxx.level().isClientSide()) {
               _playerxx.displayClientMessage(Component.literal("Feeding on Cooldown"), true);
            }
         }
      }
   }
}
