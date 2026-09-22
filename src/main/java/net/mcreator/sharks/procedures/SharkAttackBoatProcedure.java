package net.mcreator.sharks.procedures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.LevelAccessor;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;

/**
 * behavior.sharksAttackBoats: non-tamed large sharks seek out occupied boats and ram them until
 * they break. The wide-area scan for pathing runs on a staggered 20-tick cadence, but a boat
 * already in reach is rammed every tick — vanilla boat damage decays 1/tick, so slow hits can
 * never accumulate enough damage to break it. The passenger requirement keeps empty boats
 * (harbor decorations) safe, and the tameness exemption keeps pets from wrecking their owner's
 * boat. Named/persistent sharks stay eligible on purpose — they are the deliberate border
 * markers this feature is for.
 */
public final class SharkAttackBoatProcedure {
   private static final int CHECK_INTERVAL_TICKS = 20;
   private static final double RAM_DISTANCE = 2.5;
   private static final double RAM_DISTANCE_SQR = RAM_DISTANCE * RAM_DISTANCE;

   private SharkAttackBoatProcedure() {
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!DwurdySharksConfig.SHARKS_ATTACK_BOATS.get() || !(entity instanceof Mob mob)) {
         return;
      }
      checkBoatAttack(world, mob, (entity.tickCount + entity.getId()) % CHECK_INTERVAL_TICKS == 0);
   }

   public static void checkBoatAttack(LevelAccessor world, Mob mob) {
      checkBoatAttack(world, mob, true);
   }

   public static void checkBoatAttack(LevelAccessor world, Mob mob, boolean wideScan) {
      if (!mob.getType().is(DwurdySharksEntityTypeTags.LARGE_SHARKS)
         || (mob instanceof TamableAnimal tamable && tamable.isTame())
         || !inOrOnWater(world, mob)) {
         return;
      }
      double box = wideScan ? mob.getAttributeValue(Attributes.FOLLOW_RANGE) : RAM_DISTANCE + 1.0;
      List<Boat> boats = world.getEntitiesOfClass(Boat.class, mob.getBoundingBox().inflate(box),
         b -> !b.getPassengers().isEmpty());
      Boat nearest = null;
      double bestDist = Double.MAX_VALUE;
      for (Boat b : boats) {
         double d = b.distanceToSqr(mob);
         if (d < bestDist) {
            bestDist = d;
            nearest = b;
         }
      }
      if (nearest == null) {
         return;
      }
      if (mob.distanceToSqr(nearest) <= RAM_DISTANCE_SQR) {
         float damage = mob.getAttribute(Attributes.ATTACK_DAMAGE) != null
            ? Math.max(2.0F, (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.25F)
            : 2.0F;
         nearest.hurt(mob.damageSources().mobAttack(mob), damage);
      } else if (wideScan) {
         mob.getNavigation().moveTo(nearest.getX(), nearest.getY(), nearest.getZ(), 1.0);
      }
   }

   /**
    * Ramming a floating boat puts the shark's bounding box on or just above the waterline, where
    * {@code isInWaterOrBubble} (a bounding-box intersection) reads false. Treat a shark as
    * waterborne when its box touches water or water sits within ~1 block under the box bottom.
    */
   private static boolean inOrOnWater(LevelAccessor world, Mob mob) {
      if (mob.isInWaterOrBubble()) {
         return true;
      }
      double bottomY = mob.getBoundingBox().minY;
      return world.getFluidState(BlockPos.containing(mob.getX(), bottomY - 0.01, mob.getZ())).is(FluidTags.WATER)
         || world.getFluidState(BlockPos.containing(mob.getX(), bottomY - 1.01, mob.getZ())).is(FluidTags.WATER);
   }
}
