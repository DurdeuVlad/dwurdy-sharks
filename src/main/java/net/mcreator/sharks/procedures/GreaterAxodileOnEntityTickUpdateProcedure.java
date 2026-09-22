package net.mcreator.sharks.procedures;

import java.util.Comparator;
import net.mcreator.sharks.DwurdySharksMod;
import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.mcreator.sharks.entity.RollParticleEntity;
import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GreaterAxodileOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         double Chain = 0.0;
         double ChainWait = 0.0;
         double vx = 0.0;
         double vy = 0.0;
         double vz = 0.0;
         double dis = 0.0;
         if (entity.onGround()) {
            if ((entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) != null) {
               entity.getPersistentData().putDouble("IA", entity.getPersistentData().getDouble("IA") + 1.0);
            }

            if (entity.getPersistentData().getDouble("IA") == 20.0) {
               Chain = 5.0;

               for (int index0 = 0; index0 < (int)Chain; index0++) {
                  DwurdySharksMod.queueServerWork((int)ChainWait, () -> {
                     if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                        _entityx.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.SEALING, 20, 0, true, false));
                     }
                  });
                  ChainWait += 3.0;
               }
            }

            if (entity.getPersistentData().getDouble("IA") == 60.0 && entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.SEALING, 20, 0, true, false));
            }

            if (entity.getPersistentData().getDouble("IA") == 180.0) {
               if (entity.isInWaterOrBubble() && !entity.onGround()) {
                  entity.stopRiding();
                  if (entity instanceof GreaterAxodileEntity) {
                     ((GreaterAxodileEntity)entity).setAnimation("empty");
                  }
               } else {
                  if (entity instanceof GreaterAxodileEntity) {
                     ((GreaterAxodileEntity)entity).setAnimation("hideshell");
                  }

                  if (entity.onGround()) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.ROLL_PARTICLE.get())
                           .spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
                        if (entityToSpawn != null) {
                           entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                        }
                     }

                     entity.startRiding(
                        world.getEntitiesOfClass(
                              RollParticleEntity.class, AABB.ofSize(new Vec3(entity.getX(), entity.getY(), entity.getZ()), 4.0, 4.0, 4.0), e -> true
                           )
                           .stream()
                           .sorted((new Object() {
                              Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                 return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                              }
                           }).compareDistOf(entity.getX(), entity.getY(), entity.getZ()))
                           .findFirst()
                           .orElse(null)
                     );
                     if (entity instanceof GreaterAxodileEntity) {
                        ((GreaterAxodileEntity)entity).setAnimation("roll");
                     }

                     DwurdySharksMod.queueServerWork(300, () -> {
                        if (entity instanceof GreaterAxodileEntity) {
                           ((GreaterAxodileEntity)entity).setAnimation("empty");
                        }

                        entity.stopRiding();
                     });
                  }
               }
            }

            if (entity.getPersistentData().getDouble("IA") == 230.0) {
               if ((entity.isInWaterOrBubble() || entity.onGround()) && entity.isPassenger() && entity.getVehicle() instanceof RollParticleEntity) {
                  entity.stopRiding();
               }

               if (entity instanceof GreaterAxodileEntity) {
                  ((GreaterAxodileEntity)entity).setAnimation("empty");
               }

               if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                  _entity.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.SEALING, 20, 0, true, false));
               }
            }

            if (entity.getPersistentData().getDouble("IA") == 300.0 && entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.SEALING, 20, 0, true, false));
            }

            if (entity.getPersistentData().getDouble("IA") == 360.0) {
               entity.getPersistentData().putDouble("IA", 0.0);
            }
         }

         if (entity.isInWaterOrBubble() && entity instanceof GreaterAxodileEntity) {
            ((GreaterAxodileEntity)entity).setAnimation("empty");
         }

         if (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 0.5, entity.getZ()))
            .is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))) {
            world.destroyBlock(BlockPos.containing(entity.getX(), entity.getY() + 0.5, entity.getZ()), false);
         }

         if (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.0, entity.getZ()))
            .is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))) {
            world.destroyBlock(BlockPos.containing(entity.getX(), entity.getY() + 1.0, entity.getZ()), false);
         }

         if (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ()))
            .is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))) {
            world.destroyBlock(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ()), false);
         }

         if (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2.0, entity.getZ()))
            .is(BlockTags.create(ResourceLocation.parse("minecraft:ice")))) {
            world.destroyBlock(BlockPos.containing(entity.getX(), entity.getY() + 2.0, entity.getZ()), false);
         }
      }
   }
}
