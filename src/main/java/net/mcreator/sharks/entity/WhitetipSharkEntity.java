package net.mcreator.sharks.entity;

import javax.annotation.Nullable;
import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.procedures.SharkAttackBoatProcedure;
import net.mcreator.sharks.procedures.StopRidingBoatProcedure;
import net.mcreator.sharks.procedures.SharkBleedProcedure;
import net.mcreator.sharks.procedures.EatDroppedItemProcedure;
import net.mcreator.sharks.procedures.AggressiveSharksProcedureProcedure;
import net.mcreator.sharks.procedures.WhitetipSharkEntityIsHurtProcedure;
import net.mcreator.sharks.procedures.WhitetipSharkOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.WhitetipSharkOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.WhitetipSharkPlayerCollidesWithThisEntityProcedure;
import net.mcreator.sharks.procedures.WhitetipSharkThisEntityKillsAnotherOneProcedure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController.State;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WhitetipSharkEntity extends PathfinderMob implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(WhitetipSharkEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(WhitetipSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(WhitetipSharkEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public WhitetipSharkEntity(EntityType<WhitetipSharkEntity> type, Level world) {
      super(type, world);
      this.xpReward = 3;
      this.setNoAi(false);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (WhitetipSharkEntity.this.isInWater()) {
               WhitetipSharkEntity.this.setDeltaMovement(WhitetipSharkEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !WhitetipSharkEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - WhitetipSharkEntity.this.getX();
               double dy = this.wantedY - WhitetipSharkEntity.this.getY();
               double dz = this.wantedZ - WhitetipSharkEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * WhitetipSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               WhitetipSharkEntity.this.setYRot(this.rotlerp(WhitetipSharkEntity.this.getYRot(), f, 10.0F));
               WhitetipSharkEntity.this.yBodyRot = WhitetipSharkEntity.this.getYRot();
               WhitetipSharkEntity.this.yHeadRot = WhitetipSharkEntity.this.getYRot();
               if (WhitetipSharkEntity.this.isInWater()) {
                  WhitetipSharkEntity.this.setSpeed((float)WhitetipSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  WhitetipSharkEntity.this.setXRot(this.rotlerp(WhitetipSharkEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(WhitetipSharkEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  WhitetipSharkEntity.this.setZza(f3 * f1);
                  WhitetipSharkEntity.this.setYya((float)(f1 * dy));
               } else {
                  WhitetipSharkEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               WhitetipSharkEntity.this.setSpeed(0.0F);
               WhitetipSharkEntity.this.setYya(0.0F);
               WhitetipSharkEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "whitetip");
   }

   public void setTexture(String texture) {
      this.entityData.set(TEXTURE, texture);
   }

   public String getTexture() {
      return (String)this.entityData.get(TEXTURE);
   }

   protected PathNavigation createNavigation(Level world) {
      return new WaterBoundPathNavigation(this, world);
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector
         .addGoal(
            1,
            new MeleeAttackGoal(this, 15.0, false) {
               protected boolean canPerformAttack(LivingEntity entity) {
                  return this.isTimeToAttack()
                     && this.mob.distanceToSqr(entity) < this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth()
                     && this.mob.getSensing().hasLineOfSight(entity);
               }
            }
         );
      this.targetSelector.addGoal(4, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
         e -> e.getType().is(DwurdySharksEntityTypeTags.WHITETIP_SHARK_PREY)));
      this.targetSelector.addGoal(24, new NearestAttackableTargetGoal(this, Player.class, true, true) {
         public boolean canUse() {
            return super.canUse() && AggressiveSharksProcedureProcedure.execute(WhitetipSharkEntity.this.level());
         }

         public boolean canContinueToUse() {
            return super.canContinueToUse() && AggressiveSharksProcedureProcedure.execute(WhitetipSharkEntity.this.level());
         }
      });
      this.goalSelector.addGoal(25, new LookAtPlayerGoal(this, BlacktipReefSharkEntity.class, 32.0F));
      this.goalSelector.addGoal(26, new LookAtPlayerGoal(this, BonnetheadSharkEntity.class, 32.0F));
      this.goalSelector.addGoal(27, new LookAtPlayerGoal(this, WaterAnimal.class, 32.0F));
      this.goalSelector.addGoal(28, new AvoidEntityGoal(this, Dolphin.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(29, new AvoidEntityGoal(this, ShrakEntity.class, 4.0F, 16.0, 16.0));
      this.goalSelector.addGoal(30, new AvoidEntityGoal(this, TigerSharkEntity.class, 4.0F, 16.0, 16.0));
      this.goalSelector.addGoal(31, new AvoidEntityGoal(this, BullSharkEntity.class, 4.0F, 16.0, 16.0));
      this.goalSelector.addGoal(32, new AvoidEntityGoal(this, PilotFishEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(33, new AvoidEntityGoal(this, RemoraEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(34, new AvoidEntityGoal(this, WaterAnimal.class, 32.0F, 1.0, 1.0));
      this.goalSelector.addGoal(35, new RandomSwimmingGoal(this, 1.0, 40));
   }

   protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float f) {
      return super.getPassengerAttachmentPoint(entity, dimensions, f).add(0.0, 0.5, 0.0);
   }

   public SoundEvent getAmbientSound() {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.tropical_fish.ambient"));
   }

   public SoundEvent getHurtSound(DamageSource ds) {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.tropical_fish.hurt"));
   }

   public SoundEvent getDeathSound() {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.tropical_fish.death"));
   }

   public boolean hurt(DamageSource source, float amount) {
      WhitetipSharkEntityIsHurtProcedure.execute(this.level(), this);
      Entity immediatesourceentity = source.getDirectEntity();
      return super.hurt(source, amount);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      WhitetipSharkOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
      return retval;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putString("Texture", this.getTexture());
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Texture")) {
         this.setTexture(compound.getString("Texture"));
      }
   }

   public void awardKillScore(Entity entity, int score, DamageSource damageSource) {
      super.awardKillScore(entity, score, damageSource);
      WhitetipSharkThisEntityKillsAnotherOneProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), entity, this);
   }

   public void baseTick() {
      super.baseTick();
      WhitetipSharkOnEntityTickUpdateProcedure.execute(this.level(), this);
      EatDroppedItemProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
      SharkBleedProcedure.execute(this.level(), this);
      SharkAttackBoatProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
      StopRidingBoatProcedure.execute(this);
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(1.2F);
   }

   public void playerTouch(Player sourceentity) {
      super.playerTouch(sourceentity);
      WhitetipSharkPlayerCollidesWithThisEntityProcedure.execute(sourceentity);
   }

   public boolean canDrownInFluidType(FluidType type) {
      return false;
   }

   public boolean checkSpawnObstruction(LevelReader world) {
      return world.isUnobstructed(this);
   }

   public boolean isPushedByFluid() {
      return false;
   }

   public void aiStep() {
      super.aiStep();
      this.updateSwingTime();
   }

   public static void init(RegisterSpawnPlacementsEvent event) {
      event.register(
         (EntityType)DwurdySharksModEntities.WHITETIP_SHARK.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && (!DwurdySharksConfig.OCEAN_ONLY.get() || world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS)),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 1.0);
      builder = builder.add(Attributes.MAX_HEALTH, 20.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 8.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.1);
      return builder.add(NeoForgeMod.SWIM_SPEED, 1.0);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (this.animationprocedure.equals("empty")) {
         if (this.isInWaterOrBubble()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
         } else {
            return this.isSprinting()
               ? event.setAndContinue(RawAnimation.begin().thenLoop("sprint"))
               : event.setAndContinue(RawAnimation.begin().thenLoop("land"));
         }
      } else {
         return PlayState.STOP;
      }
   }

   private PlayState attackingPredicate(AnimationState event) {
      double d1 = this.getX() - this.xOld;
      double d0 = this.getZ() - this.zOld;
      float velocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      if (this.getAttackAnim(event.getPartialTick()) > 0.0F && !this.swinging) {
         this.swinging = true;
         this.lastSwing = this.level().getGameTime();
      }

      if (this.swinging && this.lastSwing + 7L <= this.level().getGameTime()) {
         this.swinging = false;
      }

      if (this.swinging && event.getController().getAnimationState() == State.STOPPED) {
         event.getController().forceAnimationReset();
         return event.setAndContinue(RawAnimation.begin().thenPlay("bite"));
      } else {
         return PlayState.CONTINUE;
      }
   }

   private PlayState procedurePredicate(AnimationState event) {
      if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == State.STOPPED
         || !this.animationprocedure.equals(this.prevAnim) && !this.animationprocedure.equals("empty")) {
         if (!this.animationprocedure.equals(this.prevAnim)) {
            event.getController().forceAnimationReset();
         }

         event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
         if (event.getController().getAnimationState() == State.STOPPED) {
            this.animationprocedure = "empty";
            event.getController().forceAnimationReset();
         }
      } else if (this.animationprocedure.equals("empty")) {
         this.prevAnim = "empty";
         return PlayState.STOP;
      }

      this.prevAnim = this.animationprocedure;
      return PlayState.CONTINUE;
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 20) {
         this.remove(RemovalReason.KILLED);
         this.dropExperience(this);
      }
   }

   public String getSyncedAnimation() {
      return (String)this.entityData.get(ANIMATION);
   }

   public void setAnimation(String animation) {
      this.entityData.set(ANIMATION, animation);
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController(this, "movement", 4, this::movementPredicate));
      data.add(new AnimationController(this, "attacking", 4, this::attackingPredicate));
      data.add(new AnimationController(this, "procedure", 4, this::procedurePredicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
