package net.mcreator.sharks.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.procedures.AggressiveSharksProcedureProcedure;
import net.mcreator.sharks.procedures.IfTamedProcedure;
import net.mcreator.sharks.procedures.NurseSharkEntityIsHurtProcedure;
import net.mcreator.sharks.procedures.NurseSharkItIsStruckByLightningProcedure;
import net.mcreator.sharks.procedures.NurseSharkOnEntityTickUpdateProcedure;
import net.mcreator.sharks.procedures.NurseSharkOnInitialEntitySpawnProcedure;
import net.mcreator.sharks.procedures.NurseSharkPlayerCollidesWithThisEntityProcedure;
import net.mcreator.sharks.procedures.NurseSharkRightClickedOnEntityProcedure;
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
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
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
import net.neoforged.neoforge.event.EventHooks;
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

public class NurseSharkEntity extends TamableAnimal implements GeoEntity {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(NurseSharkEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(NurseSharkEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(NurseSharkEntity.class, EntityDataSerializers.STRING);
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   String prevAnim = "empty";

   public NurseSharkEntity(EntityType<NurseSharkEntity> type, Level world) {
      super(type, world);
      this.xpReward = 3;
      this.setNoAi(false);
      this.setPathfindingMalus(PathType.WATER, 0.0F);
      this.moveControl = new MoveControl(this) {
         public void tick() {
            if (NurseSharkEntity.this.isInWater()) {
               NurseSharkEntity.this.setDeltaMovement(NurseSharkEntity.this.getDeltaMovement().add(0.0, 0.005, 0.0));
            }

            if (this.operation == Operation.MOVE_TO && !NurseSharkEntity.this.getNavigation().isDone()) {
               double dx = this.wantedX - NurseSharkEntity.this.getX();
               double dy = this.wantedY - NurseSharkEntity.this.getY();
               double dz = this.wantedZ - NurseSharkEntity.this.getZ();
               float f = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               float f1 = (float)(this.speedModifier * NurseSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
               NurseSharkEntity.this.setYRot(this.rotlerp(NurseSharkEntity.this.getYRot(), f, 10.0F));
               NurseSharkEntity.this.yBodyRot = NurseSharkEntity.this.getYRot();
               NurseSharkEntity.this.yHeadRot = NurseSharkEntity.this.getYRot();
               if (NurseSharkEntity.this.isInWater()) {
                  NurseSharkEntity.this.setSpeed((float)NurseSharkEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                  float f2 = -((float)(Mth.atan2(dy, (float)Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI)));
                  f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                  NurseSharkEntity.this.setXRot(this.rotlerp(NurseSharkEntity.this.getXRot(), f2, 5.0F));
                  float f3 = Mth.cos(NurseSharkEntity.this.getXRot() * (float) (Math.PI / 180.0));
                  NurseSharkEntity.this.setZza(f3 * f1);
                  NurseSharkEntity.this.setYya((float)(f1 * dy));
               } else {
                  NurseSharkEntity.this.setSpeed(f1 * 0.05F);
               }
            } else {
               NurseSharkEntity.this.setSpeed(0.0F);
               NurseSharkEntity.this.setYya(0.0F);
               NurseSharkEntity.this.setZza(0.0F);
            }
         }
      };
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SHOOT, false);
      builder.define(ANIMATION, "undefined");
      builder.define(TEXTURE, "nurse");
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
      this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(new ItemLike[]{(ItemLike)BenssharksModItems.FISH_BUCKET.get()}), false));
      this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this) {
         public boolean canUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canUse() && IfTamedProcedure.execute(entity);
         }

         public boolean canContinueToUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canContinueToUse() && IfTamedProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(3, new OwnerHurtByTargetGoal(this) {
         public boolean canUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canUse() && IfTamedProcedure.execute(entity);
         }

         public boolean canContinueToUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canContinueToUse() && IfTamedProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
      this.goalSelector.addGoal(5, new FollowParentGoal(this, 0.8));
      this.goalSelector
         .addGoal(
            6,
            new MeleeAttackGoal(this, 1.2, false) {
               protected boolean canPerformAttack(LivingEntity entity) {
                  return this.isTimeToAttack()
                     && this.mob.distanceToSqr(entity) < this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth()
                     && this.mob.getSensing().hasLineOfSight(entity);
               }
            }
         );
      this.targetSelector.addGoal(7, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(8, new RandomSwimmingGoal(this, 1.0, 40));
      this.targetSelector.addGoal(9, new NearestAttackableTargetGoal(this, CookiecutterSharkEntity.class, true, false) {
         public boolean canUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canUse() && IfTamedProcedure.execute(entity);
         }

         public boolean canContinueToUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canContinueToUse() && IfTamedProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(10, new NearestAttackableTargetGoal(this, Squid.class, true, false) {
         public boolean canUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canUse() && IfTamedProcedure.execute(entity);
         }

         public boolean canContinueToUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canContinueToUse() && IfTamedProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(11, new NearestAttackableTargetGoal(this, GlowSquid.class, true, false) {
         public boolean canUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canUse() && IfTamedProcedure.execute(entity);
         }

         public boolean canContinueToUse() {
            double x = NurseSharkEntity.this.getX();
            double y = NurseSharkEntity.this.getY();
            double z = NurseSharkEntity.this.getZ();
            Entity entity = NurseSharkEntity.this;
            Level world = NurseSharkEntity.this.level();
            return super.canContinueToUse() && IfTamedProcedure.execute(entity);
         }
      });
      this.targetSelector
         .addGoal(
            12,
            new NearestAttackableTargetGoal<>(this, Player.class, 10, true, true, target -> !NurseSharkEntity.this.isOwnedBy(target)) {
               public boolean canUse() {
                  return super.canUse() && AggressiveSharksProcedureProcedure.execute(NurseSharkEntity.this.level());
               }

               public boolean canContinueToUse() {
                  return super.canContinueToUse() && AggressiveSharksProcedureProcedure.execute(NurseSharkEntity.this.level());
               }
            }
         );
      this.goalSelector.addGoal(12, new PanicGoal(this, 1.2));
      this.goalSelector.addGoal(13, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, WaterAnimal.class, 128.0F));
      this.goalSelector.addGoal(16, new AvoidEntityGoal(this, MegalodonEntity.class, 32.0F, 1.0, 1.2));
      this.goalSelector.addGoal(17, new AvoidEntityGoal(this, Dolphin.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(18, new AvoidEntityGoal(this, ShrakEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(19, new AvoidEntityGoal(this, TigerSharkEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(20, new AvoidEntityGoal(this, MakoSharkEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(21, new AvoidEntityGoal(this, RemoraEntity.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(22, new AvoidEntityGoal(this, Drowned.class, 16.0F, 1.0, 1.2));
      this.goalSelector.addGoal(23, new AvoidEntityGoal(this, AxodileEntity.class, 16.0F, 1.0, 1.2));
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return !this.isTame();
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

   public void thunderHit(ServerLevel serverWorld, LightningBolt lightningBolt) {
      super.thunderHit(serverWorld, lightningBolt);
      NurseSharkItIsStruckByLightningProcedure.execute();
   }

   public boolean hurt(DamageSource source, float amount) {
      NurseSharkEntityIsHurtProcedure.execute(this.level(), this);
      Entity immediatesourceentity = source.getDirectEntity();
      return super.hurt(source, amount);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
      NurseSharkOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
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
      Item item = itemstack.getItem();
      if (itemstack.getItem() instanceof SpawnEggItem) {
         retval = super.mobInteract(sourceentity, hand);
      } else if (this.level().isClientSide()) {
         retval = (!this.isTame() || !this.isOwnedBy(sourceentity)) && !this.isFood(itemstack)
            ? InteractionResult.PASS
            : InteractionResult.sidedSuccess(this.level().isClientSide());
      } else if (this.isTame()) {
         if (this.isOwnedBy(sourceentity)) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
               this.usePlayerItem(sourceentity, hand, itemstack);
               FoodProperties foodproperties = itemstack.getFoodProperties(this);
               float nutrition = foodproperties != null ? foodproperties.nutrition() : 1.0F;
               this.heal(nutrition);
               retval = InteractionResult.sidedSuccess(this.level().isClientSide());
            } else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
               this.usePlayerItem(sourceentity, hand, itemstack);
               this.heal(4.0F);
               retval = InteractionResult.sidedSuccess(this.level().isClientSide());
            } else {
               retval = super.mobInteract(sourceentity, hand);
            }
         }
      } else if (this.isFood(itemstack)) {
         this.usePlayerItem(sourceentity, hand, itemstack);
         if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, sourceentity)) {
            this.tame(sourceentity);
            this.level().broadcastEntityEvent(this, (byte)7);
         } else {
            this.level().broadcastEntityEvent(this, (byte)6);
         }

         retval = InteractionResult.sidedSuccess(this.level().isClientSide());
      } else {
         retval = super.mobInteract(sourceentity, hand);
      }

      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Level world = this.level();
      NurseSharkRightClickedOnEntityProcedure.execute(world, x, y, z, this, sourceentity);
      return retval;
   }

   public void baseTick() {
      super.baseTick();
      NurseSharkOnEntityTickUpdateProcedure.execute(this.level(), this);
   }

   public EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).scale(1.05F);
   }

   public void playerTouch(Player sourceentity) {
      super.playerTouch(sourceentity);
      NurseSharkPlayerCollidesWithThisEntityProcedure.execute(this, sourceentity);
   }

   public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
      NurseSharkEntity retval = (NurseSharkEntity)((EntityType)BenssharksModEntities.NURSE_SHARK.get()).create(serverWorld);
      retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null);
      return retval;
   }

   public boolean isFood(ItemStack stack) {
      return List.of(Items.COD).contains(stack.getItem());
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
         (EntityType)BenssharksModEntities.NURSE_SHARK.get(),
         SpawnPlacementTypes.IN_WATER,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)
            && world.getBiome(pos).is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS),
         net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
      );
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.9);
      builder = builder.add(Attributes.MAX_HEALTH, 20.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 6.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 32.0);
      builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
      return builder.add(NeoForgeMod.SWIM_SPEED, 0.9);
   }

   private PlayState movementPredicate(AnimationState event) {
      if (!this.animationprocedure.equals("empty")) {
         return PlayState.STOP;
      } else if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F) || !(event.getLimbSwingAmount() < 0.15F)) && !this.isSprinting()) {
         return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
      } else if (this.isInWaterOrBubble()) {
         return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
      } else {
         return this.isSprinting()
            ? event.setAndContinue(RawAnimation.begin().thenLoop("sprint"))
            : event.setAndContinue(RawAnimation.begin().thenLoop("land"));
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
