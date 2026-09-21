package net.mcreator.sharks.entity;

import javax.annotation.Nullable;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.procedures.RemoraOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.RemoraOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.RemoraPlayerCollidesWithThisEntityProcedure;
import net.mcreator.sharks.procedures.RemoraRightClickedOnEntityProcedure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.PathType;
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

public class RemoraEntity extends PathfinderMob implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<Integer> DATA_health = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<String> DATA_nametag = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<Integer> DATA_oxygen = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<String> DATA_uuid = SynchedEntityData.defineId(RemoraEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public RemoraEntity(EntityType<RemoraEntity> type, Level world) {
      super(type, world);
      this.xpReward = 1;
      this.setNoAi(false);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (RemoraEntity.this.isInWater()) {
               RemoraEntity.this.setDeltaMovement(RemoraEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !RemoraEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - RemoraEntity.this.getX();
               double dy = this.wantedY - RemoraEntity.this.getY();
               double dz = this.wantedZ - RemoraEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * RemoraEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               RemoraEntity.this.setYRot(this.rotlerp(RemoraEntity.this.getYRot(), f, 10.0F));
               RemoraEntity.this.yBodyRot = RemoraEntity.this.getYRot();
               RemoraEntity.this.yHeadRot = RemoraEntity.this.getYRot();
               if (RemoraEntity.this.isInWater()) {
                  RemoraEntity.this.setSpeed((float)RemoraEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  RemoraEntity.this.setXRot(this.rotlerp(RemoraEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(RemoraEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  RemoraEntity.this.setZza(f3 * f1);
                  RemoraEntity.this.setYya((float)(f1 * dy));
               } else {
                  RemoraEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               RemoraEntity.this.setSpeed(0.0F);
               RemoraEntity.this.setYya(0.0F);
               RemoraEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "remora");
      builder.define(DATA_health, 4);
      builder.define(DATA_nametag, "");
      builder.define(DATA_oxygen, 0);
      builder.define(DATA_uuid, "");
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
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
         e -> e.getType().is(DwurdySharksEntityTypeTags.REMORA_TARGETS)));
      this.goalSelector.addGoal(15, new MeleeAttackGoal(this, 1.2, false) {
         protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && this.mob.distanceToSqr(entity) < 0.0 && this.mob.getSensing().hasLineOfSight(entity);
         }
      });
      this.goalSelector.addGoal(17, new RandomSwimmingGoal(this, 1.0, 40));
      this.goalSelector.addGoal(19, new LookAtPlayerGoal(this, ShrakEntity.class, 6.0F));
      this.goalSelector.addGoal(20, new LookAtPlayerGoal(this, TigerSharkEntity.class, 6.0F));
      this.goalSelector.addGoal(21, new LookAtPlayerGoal(this, MakoSharkEntity.class, 6.0F));
      this.goalSelector.addGoal(22, new LookAtPlayerGoal(this, BlueSharkEntity.class, 6.0F));
      this.goalSelector.addGoal(23, new LookAtPlayerGoal(this, NurseSharkEntity.class, 6.0F));
      this.goalSelector.addGoal(24, new LookAtPlayerGoal(this, Turtle.class, 6.0F));
      this.goalSelector.addGoal(25, new LookAtPlayerGoal(this, Dolphin.class, 6.0F));
      this.goalSelector.addGoal(26, new PanicGoal(this, 1.2));
   }

   protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
      super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
      this.spawnAtLocation(new ItemStack((ItemLike)BenssharksModItems.SUCKER.get()));
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

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      RemoraOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
      return retval;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putString("Texture", this.getTexture());
      compound.putInt("Datahealth", (Integer)this.entityData.get(DATA_health));
      compound.putString("Datanametag", (String)this.entityData.get(DATA_nametag));
      compound.putInt("Dataoxygen", (Integer)this.entityData.get(DATA_oxygen));
      compound.putString("Datauuid", (String)this.entityData.get(DATA_uuid));
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Texture")) {
         this.setTexture(compound.getString("Texture"));
      }

      if (compound.contains("Datahealth")) {
         this.entityData.set(DATA_health, compound.getInt("Datahealth"));
      }

      if (compound.contains("Datanametag")) {
         this.entityData.set(DATA_nametag, compound.getString("Datanametag"));
      }

      if (compound.contains("Dataoxygen")) {
         this.entityData.set(DATA_oxygen, compound.getInt("Dataoxygen"));
      }

      if (compound.contains("Datauuid")) {
         this.entityData.set(DATA_uuid, compound.getString("Datauuid"));
      }
   }

   public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
      ItemStack itemstack = sourceentity.getItemInHand(hand);
      InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
      super.mobInteract(sourceentity, hand);
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Level world = this.level();
      RemoraRightClickedOnEntityProcedure.execute(world, x, y, z, this, sourceentity);
      return retval;
   }

   public void baseTick() {
      super.baseTick();
      RemoraOnEntityTickUpdateProcedure.execute(this.level(), this);
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(1.5F);
   }

   public void playerTouch(Player sourceentity) {
      super.playerTouch(sourceentity);
      RemoraPlayerCollidesWithThisEntityProcedure.execute();
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

   public boolean isPushable() {
      return false;
   }

   protected void doPush(Entity entityIn) {
   }

   protected void pushEntities() {
   }

   public void aiStep() {
      super.aiStep();
      this.updateSwingTime();
   }

   public static void init(RegisterSpawnPlacementsEvent event) {
      event.register(
         (EntityType)BenssharksModEntities.REMORA.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && (!DwurdySharksConfig.OCEAN_ONLY.get() || world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS)),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 1.05);
      builder = builder.add(Attributes.MAX_HEALTH, 4.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 0.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
      return builder.add(NeoForgeMod.SWIM_SPEED, 1.05);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (this.animationprocedure.equals("empty")) {
         if (this.isInWaterOrBubble()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
         } else if (this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
         } else if (this.isVehicle() && event.isMoving()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
         } else {
            return this.isAggressive() && event.isMoving() && !this.isVehicle()
               ? event.setAndContinue(RawAnimation.begin().thenLoop("swim"))
               : event.setAndContinue(RawAnimation.begin().thenLoop("flop"));
         }
      } else {
         return PlayState.STOP;
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
      data.add(new AnimationController(this, "procedure", 4, this::procedurePredicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
