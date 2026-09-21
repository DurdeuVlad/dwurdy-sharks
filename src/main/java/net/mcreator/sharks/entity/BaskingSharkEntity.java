package net.mcreator.sharks.entity;

import javax.annotation.Nullable;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.procedures.BaskingSharkEntityIsHurtProcedure;
import net.mcreator.sharks.procedures.BaskingSharkOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.BaskingSharkOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.BaskingSharkRightClickedOnEntityProcedure;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
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

public class BaskingSharkEntity extends PathfinderMob implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(BaskingSharkEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(BaskingSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(BaskingSharkEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public BaskingSharkEntity(EntityType<BaskingSharkEntity> type, Level world) {
      super(type, world);
      this.xpReward = 3;
      this.setNoAi(false);
            this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (BaskingSharkEntity.this.isInWater()) {
               BaskingSharkEntity.this.setDeltaMovement(BaskingSharkEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !BaskingSharkEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - BaskingSharkEntity.this.getX();
               double dy = this.wantedY - BaskingSharkEntity.this.getY();
               double dz = this.wantedZ - BaskingSharkEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * BaskingSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               BaskingSharkEntity.this.setYRot(this.rotlerp(BaskingSharkEntity.this.getYRot(), f, 10.0F));
               BaskingSharkEntity.this.yBodyRot = BaskingSharkEntity.this.getYRot();
               BaskingSharkEntity.this.yHeadRot = BaskingSharkEntity.this.getYRot();
               if (BaskingSharkEntity.this.isInWater()) {
                  BaskingSharkEntity.this.setSpeed((float)BaskingSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  BaskingSharkEntity.this.setXRot(this.rotlerp(BaskingSharkEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(BaskingSharkEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  BaskingSharkEntity.this.setZza(f3 * f1);
                  BaskingSharkEntity.this.setYya((float)(f1 * dy));
               } else {
                  BaskingSharkEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               BaskingSharkEntity.this.setSpeed(0.0F);
               BaskingSharkEntity.this.setYya(0.0F);
               BaskingSharkEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "baskingshark");
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
      this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(new ItemLike[]{(ItemLike)BenssharksModItems.KRILL_BUCKET.get()}), false));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
         e -> e.getType().is(DwurdySharksEntityTypeTags.BASKING_SHARK_PREY)));
      this.goalSelector.addGoal(4, new AvoidEntityGoal(this, MegalodonEntity.class, 32.0F, 1.0, 1.2));
      this.goalSelector.addGoal(5, new AvoidEntityGoal(this, RemoraEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(6, new AvoidEntityGoal(this, CookiecutterSharkEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1.0, 40));
      this.goalSelector.addGoal(8, new PanicGoal(this, 1.2));
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
      BaskingSharkEntityIsHurtProcedure.execute(this.level(), this);
      Entity immediatesourceentity = source.getDirectEntity();
      return super.hurt(source, amount);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      BaskingSharkOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
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

   public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
      ItemStack itemstack = sourceentity.getItemInHand(hand);
      InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
      super.mobInteract(sourceentity, hand);
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Level world = this.level();
      BaskingSharkRightClickedOnEntityProcedure.execute(world, x, y, z, this, sourceentity);
      return retval;
   }

   public void baseTick() {
      super.baseTick();
      BaskingSharkOnEntityTickUpdateProcedure.execute(this.level(), this);
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(1.9F);
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
         (EntityType)BenssharksModEntities.BASKING_SHARK.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.8);
      builder = builder.add(Attributes.MAX_HEALTH, 40.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 0.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 64.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 3.0);
      return builder.add(NeoForgeMod.SWIM_SPEED, 0.8);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (this.animationprocedure.equals("empty")) {
         if (this.isInWaterOrBubble()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
         } else {
            return this.isSprinting()
               ? event.setAndContinue(RawAnimation.begin().thenLoop("sprint"))
               : event.setAndContinue(RawAnimation.begin().thenLoop("land"));
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
