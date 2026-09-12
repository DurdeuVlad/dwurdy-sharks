package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.CookiecutterSharkEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class ParasiteApplyProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
      }
   }

   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      execute(null, world, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!(entity instanceof CookiecutterSharkEntity)) {
            if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                  != BenssharksModItems.COOKIECUTTER_SHARK_LIVE.get()
               || entity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(BenssharksModMobEffects.PARASITE)
               || entity instanceof LivingEntity _livEnt4 && _livEnt4.hasEffect(BenssharksModMobEffects.SEALING)) {
               if ((sourceentity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY).getItem()
                     == BenssharksModItems.COOKIECUTTER_SHARK_LIVE.get()
                  && !(entity instanceof LivingEntity _livEnt13 && _livEnt13.hasEffect(BenssharksModMobEffects.PARASITE))
                  && !(entity instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(BenssharksModMobEffects.SEALING))) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.beehive.enter")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.beehive.enter")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (sourceentity instanceof LivingEntity _entity) {
                     ItemStack _setstack = new ItemStack(Blocks.AIR).copy();
                     _setstack.setCount(1);
                     _entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                     if (_entity instanceof Player _player) {
                        _player.getInventory().setChanged();
                     }
                  }

                  if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                     _entityx.addEffect(new MobEffectInstance(BenssharksModMobEffects.PARASITE, 200, 0, true, false));
                  }
               }
            } else {
               if (world instanceof Level _levelx) {
                  if (!_levelx.isClientSide()) {
                     _levelx.playSound(
                        null,
                        BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.beehive.enter")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _levelx.playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.beehive.enter")),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (sourceentity instanceof LivingEntity _entityx) {
                  ItemStack _setstack = new ItemStack(Blocks.AIR).copy();
                  _setstack.setCount(1);
                  _entityx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().setChanged();
                  }
               }

               if (entity instanceof LivingEntity _entityxx && !_entityxx.level().isClientSide()) {
                  _entityxx.addEffect(new MobEffectInstance(BenssharksModMobEffects.PARASITE, 200, 0, true, false));
               }
            }
         }
      }
   }
}
