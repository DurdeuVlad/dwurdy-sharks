package net.mcreator.sharks.procedures;

import java.util.Comparator;
import javax.annotation.Nullable;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class IfWearingArmorProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      if (!(event.getEntity() instanceof LivingEntity)) {
         return;
      }
      execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      execute(null, world, x, y, z, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if ((
               (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.HEAD)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.CHAINMAIL_HELMET
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.HEAD)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.IRON_HELMET
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.HEAD)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.GOLDEN_HELMET
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.HEAD)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.DIAMOND_HELMET
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.HEAD)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.NETHERITE_HELMET
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxxx
                           ? _entGetArmorxxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.CHEST)
                           : ItemStack.EMPTY)
                        .getItem()
                     == Items.CHAINMAIL_CHESTPLATE
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxxx ? _entGetArmorxxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
                        .getItem()
                     == Items.IRON_CHESTPLATE
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxxx ? _entGetArmorxxxxxxxxxxxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
                        .getItem()
                     == Items.GOLDEN_CHESTPLATE
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxxx ? _entGetArmorxxxxxxxxxxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
                        .getItem()
                     == Items.DIAMOND_CHESTPLATE
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxxx ? _entGetArmorxxxxxxxxxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
                        .getItem()
                     == Items.NETHERITE_CHESTPLATE
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxxx ? _entGetArmorxxxxxxxxx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
                        .getItem()
                     == Items.CHAINMAIL_LEGGINGS
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxxx ? _entGetArmorxxxxxxxx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
                     == Items.IRON_LEGGINGS
                  || (entity instanceof LivingEntity _entGetArmorxxxxxxx ? _entGetArmorxxxxxxx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
                     == Items.GOLDEN_LEGGINGS
                  || (entity instanceof LivingEntity _entGetArmorxxxxxx ? _entGetArmorxxxxxx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
                     == Items.DIAMOND_LEGGINGS
                  || (entity instanceof LivingEntity _entGetArmorxxxxx ? _entGetArmorxxxxx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
                     == Items.NETHERITE_LEGGINGS
                  || (entity instanceof LivingEntity _entGetArmorxxxx ? _entGetArmorxxxx.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                     == Items.CHAINMAIL_BOOTS
                  || (entity instanceof LivingEntity _entGetArmorxxx ? _entGetArmorxxx.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                     == Items.IRON_BOOTS
                  || (entity instanceof LivingEntity _entGetArmorxx ? _entGetArmorxx.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                     == Items.GOLDEN_BOOTS
                  || (entity instanceof LivingEntity _entGetArmorx ? _entGetArmorx.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                     == Items.DIAMOND_BOOTS
                  || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                     == Items.NETHERITE_BOOTS
            )
            && !(entity instanceof Player _plr && _plr.getAbilities().instabuild)
            && entity.isInWaterOrBubble()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
               .stream()
               .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
               .toList()) {
               if (entityiterator instanceof BarracudaEntity && entityiterator != entity) {
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
         }
      }
   }
}
