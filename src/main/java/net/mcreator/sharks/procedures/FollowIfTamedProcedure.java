package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class FollowIfTamedProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      execute(null, world, x, y, z, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if ((entity instanceof TamableAnimal _tamEntx ? _tamEntx.getOwner() : null) instanceof Player
            && (entity instanceof NurseSharkEntity || entity instanceof BonnetheadSharkEntity || entity instanceof BlacktipReefSharkEntity)
            && entity instanceof TamableAnimal _tamIsTamedBy
            && (entity instanceof TamableAnimal _tamEnt ? _tamEnt.getOwner() : null) instanceof LivingEntity _livEnt
            && _tamIsTamedBy.isOwnedBy(_livEnt)
            && !world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 64.0, 64.0, 64.0), e -> true).isEmpty()) {
            double _ownerX = _livEnt.getX();
            double _ownerY = _livEnt.getY();
            double _ownerZ = _livEnt.getZ();
            if (world.getFluidState(BlockPos.containing(_ownerX, _ownerY, _ownerZ)).is(FluidTags.WATER)
               || world.getFluidState(BlockPos.containing(_ownerX, _ownerY - 1.0, _ownerZ)).is(FluidTags.WATER)) {
               if (entity instanceof Mob _entity) {
                  _entity.getNavigation().moveTo(_ownerX, _ownerY, _ownerZ, 1.0);
               }

               if (entity.isInWaterOrBubble()) {
                  if (entity instanceof ShrakEntity) {
                     ((ShrakEntity)entity).setAnimation("sprint");
                  }

                  if (entity instanceof LivingEntity _livingEntity17 && _livingEntity17.getAttributes().hasAttribute(NeoForgeMod.SWIM_SPEED)) {
                     _livingEntity17.getAttribute(NeoForgeMod.SWIM_SPEED).setBaseValue(1.25);
                  }
               }
            }
         }
      }
   }
}
