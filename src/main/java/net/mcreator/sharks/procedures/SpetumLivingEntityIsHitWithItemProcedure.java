package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class SpetumLivingEntityIsHitWithItemProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
      }
   }

   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      execute(null, world, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == DwurdySharksModItems.SPETUM.get()) {
            if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(DwurdySharksModMobEffects.BLEEDING)) {
               if (entity instanceof LivingEntity _livEnt4 && _livEnt4.hasEffect(DwurdySharksModMobEffects.BLEEDING)) {
                  entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 2.5F);
               }
            } else if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(DwurdySharksModMobEffects.BLEEDING, 240, 0, true, false));
            }
         }
      }
   }
}
