package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.AxodileEntity;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.mcreator.sharks.entity.LandSharkEntity;
import net.mcreator.sharks.entity.LemonSharkEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.entity.MegalodonEntity;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class SharkAttackSoundProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity().level(), event.getSource().getEntity());
      }
   }

   public static void execute(LevelAccessor world, Entity sourceentity) {
      execute(null, world, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity sourceentity) {
      if (sourceentity != null) {
         if (sourceentity instanceof AxodileEntity && world instanceof Level _level) {
            if (!_level.isClientSide()) {
               _level.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:axodile.bite")),
                  SoundSource.NEUTRAL,
                  0.45F,
                  1.0F
               );
            } else {
               _level.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:axodile.bite")),
                  SoundSource.NEUTRAL,
                  0.45F,
                  1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof GreaterAxodileEntity && world instanceof Level _levelx) {
            if (!_levelx.isClientSide()) {
               _levelx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:axodile.bite")),
                  SoundSource.NEUTRAL,
                  0.6F,
                  -1.0F
               );
            } else {
               _levelx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:axodile.bite")),
                  SoundSource.NEUTRAL,
                  0.6F,
                  -1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof BlacktipReefSharkEntity && world instanceof Level _levelxx) {
            if (!_levelxx.isClientSide()) {
               _levelxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F
               );
            } else {
               _levelxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof BlueSharkEntity && world instanceof Level _levelxxx) {
            if (!_levelxxx.isClientSide()) {
               _levelxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  -1.0F
               );
            } else {
               _levelxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  -1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof BonnetheadSharkEntity && world instanceof Level _levelxxxx) {
            if (!_levelxxxx.isClientSide()) {
               _levelxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F
               );
            } else {
               _levelxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof BullSharkEntity && world instanceof Level _levelxxxxx) {
            if (!_levelxxxxx.isClientSide()) {
               _levelxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  1.1F
               );
            } else {
               _levelxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  1.1F,
                  false
               );
            }
         }

         if (sourceentity instanceof LandSharkEntity && world instanceof Level _levelxxxxxx) {
            if (!_levelxxxxxx.isClientSide()) {
               _levelxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  3.0F
               );
            } else {
               _levelxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  3.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof LemonSharkEntity && world instanceof Level _levelxxxxxxx) {
            if (!_levelxxxxxxx.isClientSide()) {
               _levelxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  2.0F
               );
            } else {
               _levelxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  2.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof MakoSharkEntity && world instanceof Level _levelxxxxxxxx) {
            if (!_levelxxxxxxxx.isClientSide()) {
               _levelxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  1.1F
               );
            } else {
               _levelxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  1.1F,
                  false
               );
            }
         }

         if (sourceentity instanceof MegalodonEntity && world instanceof Level _levelxxxxxxxxx) {
            if (!_levelxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  1.0F,
                  -5.0F
               );
            } else {
               _levelxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  1.0F,
                  -5.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof NurseSharkEntity && world instanceof Level _levelxxxxxxxxxx) {
            if (!_levelxxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F
               );
            } else {
               _levelxxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof ShrakEntity && world instanceof Level _levelxxxxxxxxxxx) {
            if (!_levelxxxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  -0.5F
               );
            } else {
               _levelxxxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  -0.5F,
                  false
               );
            }
         }

         if (sourceentity instanceof TigerSharkEntity && world instanceof Level _levelxxxxxxxxxxxx) {
            if (!_levelxxxxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  -0.1F
               );
            } else {
               _levelxxxxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  -0.1F,
                  false
               );
            }
         }

         if (sourceentity instanceof GreenlandSharkEntity && world instanceof Level _levelxxxxxxxxxxxxx) {
            if (!_levelxxxxxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  -1.0F
               );
            } else {
               _levelxxxxxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.fox.bite")),
                  SoundSource.NEUTRAL,
                  0.5F,
                  -1.0F,
                  false
               );
            }
         }

         if (sourceentity instanceof WhitetipSharkEntity && world instanceof Level _levelxxxxxxxxxxxxxx) {
            if (!_levelxxxxxxxxxxxxxx.isClientSide()) {
               _levelxxxxxxxxxxxxxx.playSound(
                  null,
                  BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  2.0F
               );
            } else {
               _levelxxxxxxxxxxxxxx.playLocalSound(
                  sourceentity.getX(),
                  sourceentity.getY(),
                  sourceentity.getZ(),
                  (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("dwurdysharks:shark.chomp")),
                  SoundSource.NEUTRAL,
                  0.7F,
                  2.0F,
                  false
               );
            }
         }
      }
   }
}
