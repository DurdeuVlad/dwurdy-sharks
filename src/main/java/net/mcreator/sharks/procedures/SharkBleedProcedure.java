package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.mcreator.sharks.entity.LandSharkEntity;
import net.mcreator.sharks.entity.LemonSharkEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.entity.MegalodonEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;
import software.bernie.geckolib.animatable.GeoEntity;

@EventBusSubscriber
public class SharkBleedProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      if (!(event.getEntity() instanceof GeoEntity)) {
         return;
      }
      execute(event, event.getEntity().level(), event.getEntity());
   }

   public static void execute(LevelAccessor world, Entity entity) {
      execute(null, world, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
      if (entity != null) {
         if ((
               entity instanceof ShrakEntity
                  || entity instanceof TigerSharkEntity
                  || entity instanceof BlueSharkEntity
                  || entity instanceof MakoSharkEntity
                  || entity instanceof BonnetheadSharkEntity
                  || entity instanceof BlacktipReefSharkEntity
                  || entity instanceof BullSharkEntity
                  || entity instanceof MegalodonEntity
                  || entity instanceof LemonSharkEntity
                  || entity instanceof BaskingSharkEntity
                  || entity instanceof GreenlandSharkEntity
                  || entity instanceof WhitetipSharkEntity
                  || entity instanceof LandSharkEntity
            )
            && entity instanceof LivingEntity _livEnt13
            && _livEnt13.hasEffect(BenssharksModMobEffects.BLEEDING)
            && !entity.getPersistentData().getBoolean("bleed")) {
            entity.getPersistentData().putBoolean("bleed", true);
            entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC_KILL)), 1.0F);
            BenssharksMod.queueServerWork(60, () -> entity.getPersistentData().putBoolean("bleed", false));
         }
      }
   }
}
