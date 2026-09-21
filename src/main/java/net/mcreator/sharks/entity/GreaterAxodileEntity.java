package net.mcreator.sharks.entity;

import javax.annotation.Nullable;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.procedures.GreaterAxodileOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.GreaterAxodileOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.GreaterAxodileThisEntityKillsAnotherOneProcedure;
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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
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

public class GreaterAxodileEntity extends PathfinderMob implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(GreaterAxodileEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(GreaterAxodileEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(GreaterAxodileEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public GreaterAxodileEntity(EntityType<GreaterAxodileEntity> type, Level world) {
      super(type, world);
      this.xpReward = 10;
      this.setNoAi(false);
            this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (GreaterAxodileEntity.this.isInWater()) {
               GreaterAxodileEntity.this.setDeltaMovement(GreaterAxodileEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !GreaterAxodileEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - GreaterAxodileEntity.this.getX();
               double dy = this.wantedY - GreaterAxodileEntity.this.getY();
               double dz = this.wantedZ - GreaterAxodileEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * GreaterAxodileEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               GreaterAxodileEntity.this.setYRot(this.rotlerp(GreaterAxodileEntity.this.getYRot(), f, 10.0F));
               GreaterAxodileEntity.this.yBodyRot = GreaterAxodileEntity.this.getYRot();
               GreaterAxodileEntity.this.yHeadRot = GreaterAxodileEntity.this.getYRot();
               if (GreaterAxodileEntity.this.isInWater()) {
                  GreaterAxodileEntity.this.setSpeed((float)GreaterAxodileEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  GreaterAxodileEntity.this.setXRot(this.rotlerp(GreaterAxodileEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(GreaterAxodileEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  GreaterAxodileEntity.this.setZza(f3 * f1);
                  GreaterAxodileEntity.this.setYya((float)(f1 * dy));
               } else {
                  GreaterAxodileEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               GreaterAxodileEntity.this.setSpeed(0.0F);
               GreaterAxodileEntity.this.setYya(0.0F);
               GreaterAxodileEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "greateraxodile");
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
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false) {
         protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && this.mob.distanceToSqr(entity) < 18.0625 && this.mob.getSensing().hasLineOfSight(entity);
         }
      });
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0, 40));
      this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Turtle.class, 16.0F));
      this.goalSelector.addGoal(6, new AvoidEntityGoal(this, MegalodonEntity.class, 32.0F, 1.0, 1.2));
      this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, CookiecutterSharkEntity.class, true, true));
      this.targetSelector.addGoal(8, new NearestAttackableTargetGoal(this, ElderGuardian.class, true, true));
      this.targetSelector.addGoal(9, new NearestAttackableTargetGoal(this, Guardian.class, true, true));
      this.targetSelector.addGoal(10, new NearestAttackableTargetGoal(this, Warden.class, true, true));
      this.targetSelector.addGoal(11, new NearestAttackableTargetGoal(this, WhaleSharkEntity.class, true, true));
      this.targetSelector.addGoal(12, new NearestAttackableTargetGoal(this, BaskingSharkEntity.class, true, true));
      this.targetSelector.addGoal(13, new NearestAttackableTargetGoal(this, GreenlandSharkEntity.class, true, true));
      this.targetSelector.addGoal(14, new NearestAttackableTargetGoal(this, ShrakEntity.class, true, true));
      this.targetSelector.addGoal(15, new NearestAttackableTargetGoal(this, TigerSharkEntity.class, true, true));
      this.targetSelector.addGoal(16, new NearestAttackableTargetGoal(this, Sniffer.class, true, true));
      this.targetSelector.addGoal(17, new NearestAttackableTargetGoal(this, Ravager.class, true, true));
      this.targetSelector.addGoal(18, new NearestAttackableTargetGoal(this, IronGolem.class, true, true));
      this.targetSelector.addGoal(19, new NearestAttackableTargetGoal(this, Hoglin.class, true, true));
      this.targetSelector.addGoal(20, new NearestAttackableTargetGoal(this, PolarBear.class, true, true));
      this.targetSelector.addGoal(21, new NearestAttackableTargetGoal(this, Panda.class, true, true));
      this.targetSelector.addGoal(22, new NearestAttackableTargetGoal(this, MakoSharkEntity.class, true, true));
      this.targetSelector.addGoal(23, new NearestAttackableTargetGoal(this, BlueSharkEntity.class, true, true));
      this.targetSelector.addGoal(24, new NearestAttackableTargetGoal(this, Dolphin.class, true, true));
      this.targetSelector.addGoal(25, new NearestAttackableTargetGoal(this, BullSharkEntity.class, true, true));
      this.targetSelector.addGoal(26, new NearestAttackableTargetGoal(this, WhitetipSharkEntity.class, true, true));
      this.targetSelector.addGoal(27, new NearestAttackableTargetGoal(this, LemonSharkEntity.class, true, true));
      this.targetSelector.addGoal(28, new NearestAttackableTargetGoal(this, NurseSharkEntity.class, true, true));
      this.targetSelector.addGoal(29, new NearestAttackableTargetGoal(this, BarracudaEntity.class, true, true));
      this.targetSelector.addGoal(30, new NearestAttackableTargetGoal(this, Camel.class, true, true));
      this.targetSelector.addGoal(31, new NearestAttackableTargetGoal(this, Llama.class, true, true));
      this.targetSelector.addGoal(32, new NearestAttackableTargetGoal(this, TraderLlama.class, true, true));
      this.targetSelector.addGoal(33, new NearestAttackableTargetGoal(this, Horse.class, true, true));
      this.targetSelector.addGoal(34, new NearestAttackableTargetGoal(this, ZombieHorse.class, true, true));
      this.targetSelector.addGoal(35, new NearestAttackableTargetGoal(this, SkeletonHorse.class, true, true));
      this.targetSelector.addGoal(36, new NearestAttackableTargetGoal(this, Donkey.class, true, true));
      this.targetSelector.addGoal(37, new NearestAttackableTargetGoal(this, Mule.class, true, true));
      this.targetSelector.addGoal(38, new NearestAttackableTargetGoal(this, Cow.class, true, true));
      this.targetSelector.addGoal(39, new NearestAttackableTargetGoal(this, Sheep.class, true, true));
      this.targetSelector.addGoal(40, new NearestAttackableTargetGoal(this, Goat.class, true, true));
      this.targetSelector.addGoal(41, new NearestAttackableTargetGoal(this, Pig.class, true, true));
      this.targetSelector.addGoal(42, new NearestAttackableTargetGoal(this, Zombie.class, true, true));
      this.targetSelector.addGoal(43, new NearestAttackableTargetGoal(this, ZombieVillager.class, true, true));
      this.targetSelector.addGoal(44, new NearestAttackableTargetGoal(this, Drowned.class, true, true));
      this.targetSelector.addGoal(45, new NearestAttackableTargetGoal(this, WanderingTrader.class, true, true));
      this.targetSelector.addGoal(46, new NearestAttackableTargetGoal(this, Pillager.class, true, true));
      this.targetSelector.addGoal(47, new NearestAttackableTargetGoal(this, Evoker.class, true, true));
      this.targetSelector.addGoal(48, new NearestAttackableTargetGoal(this, ThalassogerEntity.class, true, true));
      this.targetSelector.addGoal(49, new NearestAttackableTargetGoal(this, Vindicator.class, true, true));
      this.targetSelector.addGoal(50, new NearestAttackableTargetGoal(this, Witch.class, true, true));
      this.targetSelector.addGoal(51, new NearestAttackableTargetGoal(this, Player.class, true, true));
      this.targetSelector.addGoal(52, new NearestAttackableTargetGoal(this, Villager.class, true, true));
   }

   public SoundEvent getAmbientSound() {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.turtle.ambient_land"));
   }

   public SoundEvent getHurtSound(DamageSource ds) {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.shulker.hurt_closed"));
   }

   public SoundEvent getDeathSound() {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.shulker.death"));
   }

   public boolean hurt(DamageSource source, float amount) {
      return source.getDirectEntity() instanceof AbstractArrow ? false : super.hurt(source, amount);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      GreaterAxodileOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
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
      GreaterAxodileThisEntityKillsAnotherOneProcedure.execute(this);
   }

   public void baseTick() {
      super.baseTick();
      GreaterAxodileOnEntityTickUpdateProcedure.execute(this.level(), this);
      this.refreshDimensions();
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(3.0F);
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
         (EntityType)BenssharksModEntities.GREATER_AXODILE.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 1.0);
      builder = builder.add(Attributes.MAX_HEALTH, 40.0);
      builder = builder.add(Attributes.ARMOR, 40.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 18.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 1.6);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 5.0);
      return builder.add(NeoForgeMod.SWIM_SPEED, 1.0);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (this.animationprocedure.equals("empty")) {
         if (this.isInWaterOrBubble()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
         } else {
            return this.isVehicle() && event.isMoving()
               ? event.setAndContinue(RawAnimation.begin().thenLoop("roll"))
               : event.setAndContinue(RawAnimation.begin().thenLoop("idle_land"));
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
