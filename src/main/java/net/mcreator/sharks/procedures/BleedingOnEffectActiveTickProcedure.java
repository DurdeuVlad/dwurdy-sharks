package net.mcreator.sharks.procedures;

import java.util.Comparator;
import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.CookiecutterSharkEntity;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BleedingOnEffectActiveTickProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if ((entity instanceof LivingEntity _entGetArmorxxx ? _entGetArmorxxx.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem()
               != Items.CHAINMAIL_HELMET
            && (entity instanceof LivingEntity _entGetArmorxx ? _entGetArmorxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem()
               != Items.CHAINMAIL_CHESTPLATE
            && (entity instanceof LivingEntity _entGetArmorx ? _entGetArmorx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
               != Items.CHAINMAIL_LEGGINGS
            && (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
               != Items.CHAINMAIL_BOOTS
            && entity instanceof LivingEntity _livEnt8
            && _livEnt8.hasEffect(BenssharksModMobEffects.BLEEDING)
            && !(entity instanceof Player _plr && _plr.getAbilities().instabuild)) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
               .stream()
               .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
               .toList()) {
               if ((
                     entityiterator instanceof ShrakEntity
                        || entityiterator instanceof TigerSharkEntity
                        || entityiterator instanceof BlueSharkEntity
                        || entityiterator instanceof MakoSharkEntity
                        || entityiterator instanceof BonnetheadSharkEntity
                        || entityiterator instanceof BlacktipReefSharkEntity
                        || entityiterator instanceof BullSharkEntity
                        || entityiterator instanceof MegalodonEntity
                        || entityiterator instanceof LemonSharkEntity
                        || entityiterator instanceof CookiecutterSharkEntity
                        || entityiterator instanceof LandSharkEntity
                        || entityiterator instanceof GreenlandSharkEntity
                        || entityiterator instanceof WhitetipSharkEntity
                  )
                  && !(entityiterator instanceof TamableAnimal _tamEnt && _tamEnt.isTame())
                  && entityiterator != entity) {
                  if (!((entityiterator instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) instanceof LivingEntity)) {
                     if (entityiterator instanceof Mob _entity && entity instanceof LivingEntity _ent) {
                        _entity.setTarget(_ent);
                     }
                  } else if (Math.sqrt(
                           Math.pow(entityiterator.getX() - entity.getX(), 2.0)
                              + Math.pow(entityiterator.getY() - entity.getY(), 2.0)
                              + Math.pow(entityiterator.getZ() - entity.getZ(), 2.0)
                        )
                        < Math.sqrt(
                           Math.pow(entityiterator.getX() - (entityiterator instanceof Mob _mobEntxxx ? _mobEntxxx.getTarget() : null).getX(), 2.0)
                              + Math.pow(entityiterator.getY() - (entityiterator instanceof Mob _mobEntxx ? _mobEntxx.getTarget() : null).getY(), 2.0)
                              + Math.pow(entityiterator.getZ() - (entityiterator instanceof Mob _mobEntx ? _mobEntx.getTarget() : null).getZ(), 2.0)
                        )
                     && entityiterator instanceof Mob _entity
                     && entity instanceof LivingEntity _ent) {
                     _entity.setTarget(_ent);
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("bleed")) {
               entity.getPersistentData().putBoolean("bleed", true);
               entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC_KILL)), 1.0F);
               BenssharksMod.queueServerWork(60, () -> entity.getPersistentData().putBoolean("bleed", false));
            }
         }
      }
   }
}
