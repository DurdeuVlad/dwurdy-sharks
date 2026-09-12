package net.mcreator.sharks.entity;

import net.mcreator.sharks.procedures.RollParticleOnEntityTickUpdateProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
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
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController.State;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RollParticleEntity extends PathfinderMob implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(RollParticleEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(RollParticleEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(RollParticleEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public RollParticleEntity(EntityType<RollParticleEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
         }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "rollparticle");
   }

   public void setTexture(String texture) {
      this.entityData.set(TEXTURE, texture);
   }

   public String getTexture() {
      return (String)this.entityData.get(TEXTURE);
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new AvoidEntityGoal(this, GreaterAxodileEntity.class, 8.0F, 1.0, 1.0));
      this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5, false) {
         protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && this.mob.distanceToSqr(entity) < 16.0 && this.mob.getSensing().hasLineOfSight(entity);
         }
      });
      this.goalSelector.addGoal(3, new AvoidEntityGoal(this, MegalodonEntity.class, 32.0F, 1.0, 1.2));
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, CookiecutterSharkEntity.class, true, true));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, ElderGuardian.class, true, true));
      this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Guardian.class, true, true));
      this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, Warden.class, true, true));
      this.targetSelector.addGoal(8, new NearestAttackableTargetGoal(this, WhaleSharkEntity.class, true, true));
      this.targetSelector.addGoal(9, new NearestAttackableTargetGoal(this, BaskingSharkEntity.class, true, true));
      this.targetSelector.addGoal(10, new NearestAttackableTargetGoal(this, ShrakEntity.class, true, true));
      this.targetSelector.addGoal(11, new NearestAttackableTargetGoal(this, TigerSharkEntity.class, true, true));
      this.targetSelector.addGoal(12, new NearestAttackableTargetGoal(this, Ravager.class, true, true));
      this.targetSelector.addGoal(13, new NearestAttackableTargetGoal(this, IronGolem.class, true, true));
      this.targetSelector.addGoal(14, new NearestAttackableTargetGoal(this, Hoglin.class, true, true));
      this.targetSelector.addGoal(15, new NearestAttackableTargetGoal(this, PolarBear.class, true, true));
      this.targetSelector.addGoal(16, new NearestAttackableTargetGoal(this, Panda.class, true, true));
      this.targetSelector.addGoal(17, new NearestAttackableTargetGoal(this, MakoSharkEntity.class, true, true));
      this.targetSelector.addGoal(18, new NearestAttackableTargetGoal(this, BlueSharkEntity.class, true, true));
      this.targetSelector.addGoal(19, new NearestAttackableTargetGoal(this, Dolphin.class, true, true));
      this.targetSelector.addGoal(20, new NearestAttackableTargetGoal(this, BullSharkEntity.class, true, true));
      this.targetSelector.addGoal(21, new NearestAttackableTargetGoal(this, WhitetipSharkEntity.class, true, true));
      this.targetSelector.addGoal(22, new NearestAttackableTargetGoal(this, LemonSharkEntity.class, true, true));
      this.targetSelector.addGoal(23, new NearestAttackableTargetGoal(this, NurseSharkEntity.class, true, true));
      this.targetSelector.addGoal(24, new NearestAttackableTargetGoal(this, BarracudaEntity.class, true, true));
      this.targetSelector.addGoal(25, new NearestAttackableTargetGoal(this, Camel.class, true, true));
      this.targetSelector.addGoal(26, new NearestAttackableTargetGoal(this, Llama.class, true, true));
      this.targetSelector.addGoal(27, new NearestAttackableTargetGoal(this, TraderLlama.class, true, true));
      this.targetSelector.addGoal(28, new NearestAttackableTargetGoal(this, Horse.class, true, true));
      this.targetSelector.addGoal(29, new NearestAttackableTargetGoal(this, ZombieHorse.class, true, true));
      this.targetSelector.addGoal(30, new NearestAttackableTargetGoal(this, SkeletonHorse.class, true, true));
      this.targetSelector.addGoal(31, new NearestAttackableTargetGoal(this, Donkey.class, true, true));
      this.targetSelector.addGoal(32, new NearestAttackableTargetGoal(this, Mule.class, true, true));
      this.targetSelector.addGoal(33, new NearestAttackableTargetGoal(this, Cow.class, true, true));
      this.targetSelector.addGoal(34, new NearestAttackableTargetGoal(this, Sheep.class, true, true));
      this.targetSelector.addGoal(35, new NearestAttackableTargetGoal(this, Goat.class, true, true));
      this.targetSelector.addGoal(36, new NearestAttackableTargetGoal(this, Pig.class, true, true));
      this.targetSelector.addGoal(37, new NearestAttackableTargetGoal(this, Zombie.class, true, true));
      this.targetSelector.addGoal(38, new NearestAttackableTargetGoal(this, ZombieVillager.class, true, true));
      this.targetSelector.addGoal(39, new NearestAttackableTargetGoal(this, Drowned.class, true, true));
      this.targetSelector.addGoal(40, new NearestAttackableTargetGoal(this, WanderingTrader.class, true, true));
      this.targetSelector.addGoal(41, new NearestAttackableTargetGoal(this, Pillager.class, true, true));
      this.targetSelector.addGoal(42, new NearestAttackableTargetGoal(this, Evoker.class, true, true));
      this.targetSelector.addGoal(43, new NearestAttackableTargetGoal(this, ThalassogerEntity.class, true, true));
      this.targetSelector.addGoal(44, new NearestAttackableTargetGoal(this, Vindicator.class, true, true));
      this.targetSelector.addGoal(45, new NearestAttackableTargetGoal(this, Witch.class, true, true));
      this.targetSelector.addGoal(46, new NearestAttackableTargetGoal(this, Player.class, true, true));
      this.targetSelector.addGoal(47, new NearestAttackableTargetGoal(this, Villager.class, true, true));
   }

   public void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound((SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.minecart.riding")), 0.15F, 1.0F);
   }

   public SoundEvent getHurtSound(DamageSource ds) {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.minecart.riding"));
   }

   public SoundEvent getDeathSound() {
      return (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.minecart.riding"));
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source.is(DamageTypes.IN_FIRE)) {
         return false;
      } else if (source.getDirectEntity() instanceof AbstractArrow) {
         return false;
      } else if (source.getDirectEntity() instanceof Player) {
         return false;
      } else if (source.getDirectEntity() instanceof ThrownPotion
         || source.getDirectEntity() instanceof AreaEffectCloud
         || source.typeHolder().is(NeoForgeMod.POISON_DAMAGE)) {
         return false;
      } else if (source.is(DamageTypes.FALL)) {
         return false;
      } else if (source.is(DamageTypes.CACTUS)) {
         return false;
      } else if (source.is(DamageTypes.DROWN)) {
         return false;
      } else if (source.is(DamageTypes.LIGHTNING_BOLT)) {
         return false;
      } else if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
         return false;
      } else if (source.is(DamageTypes.TRIDENT)) {
         return false;
      } else if (source.is(DamageTypes.FALLING_ANVIL)) {
         return false;
      } else if (source.is(DamageTypes.DRAGON_BREATH)) {
         return false;
      } else {
         return !source.is(DamageTypes.WITHER) && !source.is(DamageTypes.WITHER_SKULL) ? super.hurt(source, amount) : false;
      }
   }

   public boolean ignoreExplosion(Explosion explosion) {
      return true;
   }

   public boolean fireImmune() {
      return true;
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

   public void baseTick() {
      super.baseTick();
      RollParticleOnEntityTickUpdateProcedure.execute(this.level(), this);
      this.refreshDimensions();
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(0.1F);
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
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 0.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 0.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 64.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 4.6);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 4.0);
      return builder.add(Attributes.ATTACK_KNOCKBACK, 2.5);
   }

   private PlayState movementPredicate(AnimationState event) {
      return this.animationprocedure.equals("empty") ? event.setAndContinue(RawAnimation.begin().thenLoop("roll")) : PlayState.STOP;
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
      if (this.deathTime == 0) {
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
