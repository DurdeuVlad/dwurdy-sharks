package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.AxodileEntity;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.mcreator.sharks.entity.LemonSharkEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.entity.PilotFishEntity;
import net.mcreator.sharks.entity.RemoraEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class BarracudaSprintProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      execute(null, world, x, y, z, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof BarracudaEntity && entity.isInWaterOrBubble()) {
            if (!world.getEntitiesOfClass(MakoSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 12.0, 12.0, 12.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(PilotFishEntity.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(TropicalFish.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(Pufferfish.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(Cod.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(Salmon.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(Chicken.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(TigerSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(BullSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(AxodileEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(WhitetipSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(GreaterAxodileEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(LemonSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(GlowSquid.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(Squid.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(ShrakEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || !world.getEntitiesOfClass(BlueSharkEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()
               || (entity instanceof Mob _mobEntxx ? _mobEntxx.getTarget() : null) instanceof Player
                  && !world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
                  && !((entity instanceof Mob _mobEntx ? _mobEntx.getTarget() : null) instanceof Player _plr && _plr.getAbilities().instabuild)
                  && (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null).isInWaterOrBubble()) {
               if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                  _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.FRENZY, 60, 2, true, false));
               }

               if (entity instanceof BarracudaEntity) {
                  ((BarracudaEntity)entity).setAnimation("sprint");
               }

               BenssharksMod.queueServerWork(60, () -> {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.removeEffect(BenssharksModMobEffects.FRENZY);
                  }
               });
            } else {
               if (entity instanceof BarracudaEntity) {
                  ((BarracudaEntity)entity).setAnimation("empty");
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.removeEffect(BenssharksModMobEffects.FRENZY);
               }
            }
         }
      }
   }
}
