package net.mcreator.sharks.procedures;

import net.mcreator.sharks.entity.CookiecutterSharkEntity;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

public class ParasiteOnEffectActiveTickProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!(entity instanceof CookiecutterSharkEntity)
            && !(entity instanceof Player _plr && _plr.getAbilities().instabuild)
            && !(entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(DwurdySharksModMobEffects.SEALING))) {
            entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC_KILL)), 0.125F);
         }
      }
   }
}
