package net.mcreator.sharks.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class SeekingArrowProjectileHitsPlayerProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity immediatesourceentity) {
      if (entity != null && immediatesourceentity != null) {
         if (world instanceof ServerLevel _level) {
            _level.sendParticles(
               ParticleTypes.GLOW_SQUID_INK,
               immediatesourceentity.getX(),
               immediatesourceentity.getY(),
               immediatesourceentity.getZ(),
               10,
               0.25,
               0.25,
               0.25,
               0.25
            );
         }

         if (world instanceof ServerLevel _level) {
            _level.sendParticles(
               ParticleTypes.GLOW_SQUID_INK, immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ(), 10, 1.0, 1.0, 1.0, 0.5
            );
         }

         if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
               _level.playSound(
                  null,
                  BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.attack.target")),
                  SoundSource.HOSTILE,
                  2.0F,
                  1.0F
               );
            } else {
               _level.playLocalSound(
                  immediatesourceentity.getX(),
                  immediatesourceentity.getY(),
                  immediatesourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.attack.target")),
                  SoundSource.HOSTILE,
                  2.0F,
                  1.0F,
                  false
               );
            }
         }

         if (world instanceof Level _levelx) {
            if (!_levelx.isClientSide()) {
               _levelx.playSound(
                  null,
                  BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.firework_rocket.large_blast")),
                  SoundSource.HOSTILE,
                  1.0F,
                  1.0F
               );
            } else {
               _levelx.playLocalSound(
                  immediatesourceentity.getX(),
                  immediatesourceentity.getY(),
                  immediatesourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.firework_rocket.large_blast")),
                  SoundSource.HOSTILE,
                  1.0F,
                  1.0F,
                  false
               );
            }
         }

         if (world instanceof Level _levelxx) {
            if (!_levelxx.isClientSide()) {
               _levelxx.playSound(
                  null,
                  BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")),
                  SoundSource.HOSTILE,
                  1.0F,
                  2.0F
               );
            } else {
               _levelxx.playLocalSound(
                  immediatesourceentity.getX(),
                  immediatesourceentity.getY(),
                  immediatesourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")),
                  SoundSource.HOSTILE,
                  1.0F,
                  2.0F,
                  false
               );
            }
         }

         if (!immediatesourceentity.level().isClientSide()) {
            immediatesourceentity.discard();
         }

         if (!(entity instanceof Player _plr && _plr.getAbilities().instabuild)) {
            if (entity instanceof LivingEntity _livEnt22
               && _livEnt22.isBlocking()
               && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
               if (world instanceof ServerLevel _levelxxx) {
                  (entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(3, _levelxxx, null, _stkprov -> {});
               }

               if (world instanceof Level _levelxxx) {
                  if (!_levelxxx.isClientSide()) {
                     _levelxxx.playSound(
                        null,
                        BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.shield.block")),
                        SoundSource.HOSTILE,
                        0.5F,
                        1.0F
                     );
                  } else {
                     _levelxxx.playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.shield.block")),
                        SoundSource.HOSTILE,
                        0.5F,
                        1.0F,
                        false
                     );
                  }
               }
            } else if (entity instanceof LivingEntity _livEnt31
               && _livEnt31.isBlocking()
               && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
               if (world instanceof ServerLevel _levelxxxx) {
                  (entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY).hurtAndBreak(3, _levelxxxx, null, _stkprov -> {});
               }

               if (world instanceof Level _levelxxxx) {
                  if (!_levelxxxx.isClientSide()) {
                     _levelxxxx.playSound(
                        null,
                        BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.shield.block")),
                        SoundSource.HOSTILE,
                        0.5F,
                        1.0F
                     );
                  } else {
                     _levelxxxx.playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.shield.block")),
                        SoundSource.HOSTILE,
                        0.5F,
                        1.0F,
                        false
                     );
                  }
               }
            }
         }
      }
   }
}
