package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class DryoutProcedure {
   private static final String DRY_TICKS_TAG = "dwurdysharks_dry_ticks";

   private DryoutProcedure() {
   }

   public static void dryTick(Entity entity, int delayTicks, int durationTicks) {
      if (entity instanceof LivingEntity living && !entity.level().isClientSide()) {
         CompoundTag data = living.getPersistentData();
         int dryTicks = data.getInt(DRY_TICKS_TAG) + 1;
         data.putInt(DRY_TICKS_TAG, dryTicks);
         if (dryTicks >= delayTicks && !living.hasEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT)) {
            living.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.DRYOUT_EFFECT, durationTicks, 0, true, false));
         }
      }
   }

   public static void wetTick(Entity entity) {
      if (entity instanceof LivingEntity living) {
         living.getPersistentData().remove(DRY_TICKS_TAG);
         living.removeEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT);
      }
   }
}
