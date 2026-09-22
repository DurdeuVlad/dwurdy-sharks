package net.mcreator.sharks.procedures;

import java.util.List;
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
 * they break. Checked on a staggered 20-tick cadence from each entity's baseTick; the passenger
 * requirement keeps empty boats (harbor decorations) safe, and the tameness exemption keeps pets
 * from wrecking their owner's boat. Named/persistent sharks stay eligible on purpose — they are
 * the deliberate border markers this feature is for.
 */
public final class SharkAttackBoatProcedure {
   private static final int CHECK_INTERVAL_TICKS = 20;
   private static final double RAM_DISTANCE_SQR = 2.5 * 2.5;

   private SharkAttackBoatProcedure() {
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (!DwurdySharksConfig.SHARKS_ATTACK_BOATS.get()
         || !(entity instanceof Mob mob)
         || (entity.tickCount + entity.getId()) % CHECK_INTERVAL_TICKS != 0) {
         return;
      }
      checkBoatAttack(world, mob);
   }

   public static void checkBoatAttack(LevelAccessor world, Mob mob) {
      if (!mob.getType().is(DwurdySharksEntityTypeTags.LARGE_SHARKS)
         || (mob instanceof TamableAnimal tamable && tamable.isTame())
         || !mob.isInWaterOrBubble()) {
         return;
      }
      double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
      List<Boat> boats = world.getEntitiesOfClass(Boat.class, mob.getBoundingBox().inflate(range),
         b -> !b.getPassengers().isEmpty());
      if (boats.isEmpty()) {
         return;
      }
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
      } else {
         mob.getNavigation().moveTo(nearest.getX(), nearest.getY(), nearest.getZ(), 1.0);
      }
   }
}
