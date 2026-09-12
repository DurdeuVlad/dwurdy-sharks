package net.mcreator.sharks.procedures;

import java.util.Comparator;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SeekingArrowWhileProjectileFlyingTickProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
      if (entity != null && immediatesourceentity != null) {
         double dis = 0.0;
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
            .stream()
            .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
            .toList()) {
            if (world instanceof ServerLevel _level) {
               _level.sendParticles(
                  ParticleTypes.GLOW_SQUID_INK,
                  immediatesourceentity.getX(),
                  immediatesourceentity.getY(),
                  immediatesourceentity.getZ(),
                  1,
                  0.01,
                  0.01,
                  0.01,
                  0.0
               );
            }

            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(immediatesourceentity.getX(), immediatesourceentity.getY(), immediatesourceentity.getZ()),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.deactivate")),
                     SoundSource.NEUTRAL,
                     0.025F,
                     2.0F
                  );
               } else {
                  _level.playLocalSound(
                     immediatesourceentity.getX(),
                     immediatesourceentity.getY(),
                     immediatesourceentity.getZ(),
                     (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.conduit.deactivate")),
                     SoundSource.NEUTRAL,
                     0.025F,
                     2.0F,
                     false
                  );
               }
            }

            if (world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).stream().sorted((new Object() {
                  Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                     return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                  }
               }).compareDistOf(x, y, z)).findFirst().orElse(null) == entityiterator
               && entityiterator != entity
               && entityiterator != immediatesourceentity
               && !(entityiterator instanceof Player _plr && _plr.getAbilities().instabuild)) {
               dis = Math.sqrt(
                  Math.pow(entityiterator.getX() - immediatesourceentity.getX(), 2.0)
                     + Math.pow(entityiterator.getY() - immediatesourceentity.getY(), 2.0)
                     + Math.pow(entityiterator.getZ() - immediatesourceentity.getZ(), 2.0)
               );
               immediatesourceentity.setDeltaMovement(
                  new Vec3(
                     (entityiterator.getX() - immediatesourceentity.getX()) / dis,
                     (entityiterator.getY() - immediatesourceentity.getY()) / dis,
                     (entityiterator.getZ() - immediatesourceentity.getZ()) / dis
                  )
               );
            }
         }
      }
   }
}
