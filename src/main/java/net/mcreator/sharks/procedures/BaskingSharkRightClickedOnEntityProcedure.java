package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;

@EventBusSubscriber
public class BaskingSharkRightClickedOnEntityProcedure {
   @SubscribeEvent
   public static void onRightClickEntity(EntityInteract event) {
      if (event.getHand() == event.getEntity().getUsedItemHand()) {
         execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getTarget(), event.getEntity());
      }
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      execute(null, world, x, y, z, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof Player && entity instanceof BaskingSharkEntity) {
            if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == BenssharksModItems.KRILL_BUCKET.get()
               )
             {
               if (!entity.getPersistentData().getBoolean("canBeMilked")) {
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
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.panda.bite")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.panda.bite")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (sourceentity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2));
                  }

                  if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, true, false));
                  }

                  if (world instanceof ServerLevel _levelx) {
                     _levelx.sendParticles(ParticleTypes.CRIT, x, y, z, 5, 1.0, 1.0, 1.0, 1.0);
                  }

                  BenssharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
               }
            } else if ((sourceentity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY).getItem()
                  == BenssharksModItems.KRILL_BUCKET.get()
               && !entity.getPersistentData().getBoolean("canBeMilked")) {
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
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.panda.bite")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelx.playLocalSound(
                        x,
                        y,
                        z,
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.panda.bite")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (sourceentity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2));
               }

               if (entity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, true, false));
               }

               if (world instanceof ServerLevel _levelxx) {
                  _levelxx.sendParticles(ParticleTypes.CRIT, x, y, z, 5, 1.0, 1.0, 1.0, 1.0);
               }

               BenssharksMod.queueServerWork(6000, () -> entity.getPersistentData().putBoolean("canBeMilked", false));
            }

            if ((sourceentity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY).getItem()
               == BenssharksModItems.KRILL_BUCKET.get()) {
               if (sourceentity instanceof Player _player && !_player.level().isClientSide()) {
                  _player.displayClientMessage(Component.literal("Feeding on Cooldown"), true);
               }
            } else if ((sourceentity instanceof LivingEntity _livEntxx ? _livEntxx.getOffhandItem() : ItemStack.EMPTY).getItem()
                  == BenssharksModItems.KRILL_BUCKET.get()
               && sourceentity instanceof Player _player
               && !_player.level().isClientSide()) {
               _player.displayClientMessage(Component.literal("Feeding on Cooldown"), true);
            }
         }
      }
   }
}
