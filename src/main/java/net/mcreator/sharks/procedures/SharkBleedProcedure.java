package net.mcreator.sharks.procedures;

import net.mcreator.sharks.DwurdySharksMod;
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
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;

public class SharkBleedProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
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
            && _livEnt13.hasEffect(DwurdySharksModMobEffects.BLEEDING)
            && !entity.getPersistentData().getBoolean("bleed")) {
            entity.getPersistentData().putBoolean("bleed", true);
            entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC_KILL)), 1.0F);
            DwurdySharksMod.queueServerWork(60, () -> entity.getPersistentData().putBoolean("bleed", false));
         }
      }
   }
}
