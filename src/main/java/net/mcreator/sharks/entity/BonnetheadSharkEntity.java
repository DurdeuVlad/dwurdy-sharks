package net.mcreator.sharks.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.procedures.AggressiveSharksProcedureProcedure;
import net.mcreator.sharks.procedures.BonnetheadSharkEntityIsHurtProcedure;
import net.mcreator.sharks.procedures.BonnetheadSharkOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.BonnetheadSharkOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.BonnetheadSharkRightClickedOnEntityProcedure;
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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
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

public class BonnetheadSharkEntity extends Animal implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<Integer> DATA_health = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<String> DATA_nametag = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<Integer> DATA_oxygen = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<String> DATA_uuid = SynchedEntityData.defineId(BonnetheadSharkEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public BonnetheadSharkEntity(EntityType<BonnetheadSharkEntity> type, Level world) {
      super(type, world);
      this.xpReward = 3;
      this.setNoAi(false);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (BonnetheadSharkEntity.this.isInWater()) {
               BonnetheadSharkEntity.this.setDeltaMovement(BonnetheadSharkEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !BonnetheadSharkEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - BonnetheadSharkEntity.this.getX();
               double dy = this.wantedY - BonnetheadSharkEntity.this.getY();
               double dz = this.wantedZ - BonnetheadSharkEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * BonnetheadSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               BonnetheadSharkEntity.this.setYRot(this.rotlerp(BonnetheadSharkEntity.this.getYRot(), f, 10.0F));
               BonnetheadSharkEntity.this.yBodyRot = BonnetheadSharkEntity.this.getYRot();
               BonnetheadSharkEntity.this.yHeadRot = BonnetheadSharkEntity.this.getYRot();
               if (BonnetheadSharkEntity.this.isInWater()) {
                  BonnetheadSharkEntity.this.setSpeed((float)BonnetheadSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  BonnetheadSharkEntity.this.setXRot(this.rotlerp(BonnetheadSharkEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(BonnetheadSharkEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  BonnetheadSharkEntity.this.setZza(f3 * f1);
                  BonnetheadSharkEntity.this.setYya((float)(f1 * dy));
               } else {
                  BonnetheadSharkEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               BonnetheadSharkEntity.this.setSpeed(0.0F);
               BonnetheadSharkEntity.this.setYya(0.0F);
               BonnetheadSharkEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "bonnet");
      builder.define(DATA_health, 8);
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
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0));
      this.goalSelector
         .addGoal(
            4,
            new MeleeAttackGoal(this, 15.0, false) {
               protected boolean canPerformAttack(LivingEntity entity) {
                  return this.isTimeToAttack()
                     && this.mob.distanceToSqr(entity) < this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth()
                     && this.mob.getSensing().hasLineOfSight(entity);
               }
            }
         );
      this.goalSelector.addGoal(5, new PanicGoal(this, 1.0));
      this.targetSelector.addGoal(8, new NearestAttackableTargetGoal(this, CookiecutterSharkEntity.class, true, true));
      this.targetSelector.addGoal(9, new NearestAttackableTargetGoal(this, Frog.class, true, true));
      this.targetSelector.addGoal(10, new NearestAttackableTargetGoal(this, Tadpole.class, true, true));
      this.targetSelector.addGoal(11, new NearestAttackableTargetGoal(this, Chicken.class, true, true));
      this.targetSelector.addGoal(12, new NearestAttackableTargetGoal(this, Player.class, true, true) {
         public boolean canUse() {
            return super.canUse() && AggressiveSharksProcedureProcedure.execute(BonnetheadSharkEntity.this.level());
         }

         public boolean canContinueToUse() {
            return super.canContinueToUse() && AggressiveSharksProcedureProcedure.execute(BonnetheadSharkEntity.this.level());
         }
      });
      this.goalSelector.addGoal(12, new LeapAtTargetGoal(this, 0.5F));
      this.goalSelector.addGoal(13, new TemptGoal(this, 1.0, Ingredient.of(new ItemLike[]{Blocks.SEAGRASS.asItem()}), false));
      this.goalSelector.addGoal(14, new RemoveBlockGoal(Blocks.TALL_SEAGRASS, this, 1.0, 32));
      this.goalSelector.addGoal(16, new LookAtPlayerGoal(this, BlacktipReefSharkEntity.class, 8.0F));
      this.goalSelector.addGoal(17, new LookAtPlayerGoal(this, WaterAnimal.class, 32.0F));
      this.goalSelector.addGoal(18, new AvoidEntityGoal(this, MegalodonEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(19, new AvoidEntityGoal(this, ElderGuardian.class, 64.0F, 16.0, 16.0));
      this.goalSelector.addGoal(20, new AvoidEntityGoal(this, Guardian.class, 64.0F, 16.0, 16.0));
      this.goalSelector.addGoal(21, new AvoidEntityGoal(this, ShrakEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(22, new AvoidEntityGoal(this, TigerSharkEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(23, new AvoidEntityGoal(this, MakoSharkEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(24, new AvoidEntityGoal(this, BlueSharkEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(25, new AvoidEntityGoal(this, BullSharkEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(26, new AvoidEntityGoal(this, LemonSharkEntity.class, 4.0F, 16.0, 16.0));
      this.goalSelector.addGoal(27, new AvoidEntityGoal(this, AxodileEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(28, new AvoidEntityGoal(this, Dolphin.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(29, new AvoidEntityGoal(this, RemoraEntity.class, 16.0F, 16.0, 16.0));
      this.goalSelector.addGoal(30, new AvoidEntityGoal(this, WaterAnimal.class, 16.0F, 1.0, 1.0));
      this.goalSelector.addGoal(31, new RandomSwimmingGoal(this, 1.0, 40));
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
      BonnetheadSharkEntityIsHurtProcedure.execute(this.level(), this);
      Entity immediatesourceentity = source.getDirectEntity();
      return super.hurt(source, amount);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      BonnetheadSharkOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
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
      BonnetheadSharkRightClickedOnEntityProcedure.execute(world, x, y, z, this, sourceentity);
      return retval;
   }

   public void baseTick() {
      super.baseTick();
      BonnetheadSharkOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
      this.refreshDimensions();
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(0.8F);
   }

   public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
      BonnetheadSharkEntity retval = (BonnetheadSharkEntity)((EntityType)BenssharksModEntities.BONNETHEAD_SHARK.get()).create(serverWorld);
      retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null);
      return retval;
   }

   public boolean isFood(ItemStack stack) {
      return List.of(Blocks.SEAGRASS.asItem()).contains(stack.getItem());
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
         (EntityType)BenssharksModEntities.BONNETHEAD_SHARK.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 1.25);
      builder = builder.add(Attributes.MAX_HEALTH, 8.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
      return builder.add(NeoForgeMod.SWIM_SPEED, 1.25);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (this.animationprocedure.equals("empty")) {
         if (this.isInWaterOrBubble()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
         } else {
            return this.isSprinting()
               ? event.setAndContinue(RawAnimation.begin().thenLoop("sprint2"))
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
      data.add(new AnimationController(this, "movement", 2, this::movementPredicate));
      data.add(new AnimationController(this, "attacking", 2, this::attackingPredicate));
      data.add(new AnimationController(this, "procedure", 2, this::procedurePredicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
