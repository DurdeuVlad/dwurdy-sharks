package net.mcreator.sharks.procedures;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;

public class MaelstromBowRangedItemShootsProjectileProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!(entity instanceof Player _plr && _plr.getAbilities().instabuild)) {
            if (entity instanceof Player _player) {
               ItemStack _stktoremove = new ItemStack(Items.ARROW);
               _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.GLOW_SQUID_INK, entity.getX(), entity.getY(), entity.getZ(), 3, 0.25, 0.25, 0.25, 0.25);
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.GLOW_SQUID_INK, entity.getX(), entity.getY(), entity.getZ(), 3, 1.0, 1.0, 1.0, 0.5);
            }
         }
      }
   }
}
