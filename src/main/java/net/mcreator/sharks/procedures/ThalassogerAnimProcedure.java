package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.SeekerSharkProjectileEntity;
import net.mcreator.sharks.entity.ThalassogerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

@EventBusSubscriber
public class ThalassogerAnimProcedure {
   @SubscribeEvent
   public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
      execute(event, event.getEntity().level(), event.getOriginalAboutToBeSetTarget(), event.getEntity());
   }

   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      execute(null, world, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         double fromZ = 0.0;
         double fromX = 0.0;
         double fromY = 0.0;
         BlockState clickedBlock = Blocks.AIR.defaultBlockState();
         if (!world.getEntitiesOfClass(
               SeekerSharkProjectileEntity.class,
               AABB.ofSize(new Vec3(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), 4.0, 4.0, 4.0),
               e -> true
            )
            .isEmpty()) {
            if (entity instanceof ThalassogerEntity animatable) {
               animatable.setTexture("thalassoger_glow");
            }

            if (sourceentity instanceof ThalassogerEntity) {
               ((ThalassogerEntity)sourceentity).setAnimation("shoot");
            }
         } else if (sourceentity instanceof ThalassogerEntity) {
            if (entity instanceof ThalassogerEntity animatable) {
               animatable.setTexture("thalassoger_glow");
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.GLOW_SQUID_INK, sourceentity.getX(), sourceentity.getY() + 1.5, sourceentity.getZ(), 1, 0.0, 0.0, 0.0, 0.075);
            }

            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.ambient.short")),
                     SoundSource.NEUTRAL,
                     2.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     sourceentity.getX(),
                     sourceentity.getY(),
                     sourceentity.getZ(),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.ambient.short")),
                     SoundSource.NEUTRAL,
                     2.0F,
                     1.0F,
                     false
                  );
               }
            }
         }

         if (entity instanceof ThalassogerEntity animatable) {
            animatable.setTexture("thalassoger");
         }
      }
   }
}
