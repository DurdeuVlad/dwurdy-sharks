package net.mcreator.sharks.procedures;

import java.util.Comparator;
import javax.annotation.Nullable;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class BarracudaEatProcedure {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
   }

   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      execute(null, world, x, y, z, entity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof BarracudaEntity
            && !world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true).isEmpty()) {
            Entity _itemEnt = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true)
               .stream()
               .sorted((new Object() {
                  Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                     return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                  }
               }).compareDistOf(x, y, z))
               .findFirst()
               .orElse(null);
            if ((_itemEnt instanceof ItemEntity _itemEntx ? _itemEntx.getItem() : ItemStack.EMPTY).has(DataComponents.FOOD)) {
               _itemEnt = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true).stream().sorted((new Object() {
                  Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                     return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                  }
               }).compareDistOf(x, y, z)).findFirst().orElse(null);
               if ((_itemEnt instanceof ItemEntity _itemEntxx ? _itemEntxx.getItem() : ItemStack.EMPTY).getItem() != BenssharksModItems.RAW_BARRACUDA.get()) {
                  _itemEnt = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true)
                     .stream()
                     .sorted((new Object() {
                        Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                           return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                        }
                     }).compareDistOf(x, y, z))
                     .findFirst()
                     .orElse(null);
                  if ((_itemEnt instanceof ItemEntity _itemEntxxx ? _itemEntxxx.getItem() : ItemStack.EMPTY).getItem()
                     != BenssharksModItems.COOKED_BARRACUDA.get()) {
                     if (entity instanceof Mob _entity) {
                        _entity.getNavigation()
                           .moveTo(
                              world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true)
                                 .stream()
                                 .sorted((new Object() {
                                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                       return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                    }
                                 }).compareDistOf(x, y, z))
                                 .findFirst()
                                 .orElse(null)
                                 .getX(),
                              world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true)
                                 .stream()
                                 .sorted((new Object() {
                                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                       return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                    }
                                 }).compareDistOf(x, y, z))
                                 .findFirst()
                                 .orElse(null)
                                 .getY(),
                              world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 8.0, 8.0, 8.0), e -> true)
                                 .stream()
                                 .sorted((new Object() {
                                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                       return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                    }
                                 }).compareDistOf(x, y, z))
                                 .findFirst()
                                 .orElse(null)
                                 .getZ(),
                              1.0
                           );
                     }

                     if (!world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true).isEmpty()) {
                        Entity _level = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                           .stream()
                           .sorted((new Object() {
                              Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                 return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                              }
                           }).compareDistOf(x, y, z))
                           .findFirst()
                           .orElse(null);
                        if ((_level instanceof ItemEntity _itemEntxxxx ? _itemEntxxxx.getItem() : ItemStack.EMPTY).has(DataComponents.FOOD)) {
                           _level = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                              .stream()
                              .sorted((new Object() {
                                 Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                    return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                 }
                              }).compareDistOf(x, y, z))
                              .findFirst()
                              .orElse(null);
                           if ((_level instanceof ItemEntity _itemEntxxxxx ? _itemEntxxxxx.getItem() : ItemStack.EMPTY).getItem()
                              != BenssharksModItems.RAW_BARRACUDA.get()) {
                              _level = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                                 .stream()
                                 .sorted((new Object() {
                                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                       return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                    }
                                 }).compareDistOf(x, y, z))
                                 .findFirst()
                                 .orElse(null);
                              if ((_level instanceof ItemEntity _itemEntxxxxxx ? _itemEntxxxxxx.getItem() : ItemStack.EMPTY).getItem()
                                 != BenssharksModItems.COOKED_BARRACUDA.get()) {
                                 Entity _entity = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                                    .stream()
                                    .sorted((new Object() {
                                       Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                          return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                       }
                                    }).compareDistOf(x, y, z))
                                    .findFirst()
                                    .orElse(null);
                                 if ((_entity instanceof ItemEntity _itemEntxxxxxxx ? _itemEntxxxxxxx.getItem() : ItemStack.EMPTY).getItem()
                                    == Items.ENCHANTED_GOLDEN_APPLE) {
                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3, true, true));
                                    }

                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1, true, true));
                                    }

                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0, true, true));
                                    }

                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0, true, true));
                                    }
                                 }

                                 _entity = world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                                    .stream()
                                    .sorted((new Object() {
                                       Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                          return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                       }
                                    }).compareDistOf(x, y, z))
                                    .findFirst()
                                    .orElse(null);
                                 if ((_entity instanceof ItemEntity _itemEntxxxxxxx ? _itemEntxxxxxxx.getItem() : ItemStack.EMPTY).getItem()
                                    == Items.GOLDEN_APPLE) {
                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0, true, true));
                                    }

                                    if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                                       _entityx.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, true));
                                    }
                                 }

                                 if (entity instanceof LivingEntity _entityx) {
                                    _entityx.swing(InteractionHand.MAIN_HAND, true);
                                 }

                                 if (!world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                                    .stream()
                                    .sorted((new Object() {
                                       Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                          return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                       }
                                    }).compareDistOf(x, y, z))
                                    .findFirst()
                                    .orElse(null)
                                    .level()
                                    .isClientSide()) {
                                    world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(x, y, z), 2.5, 2.5, 2.5), e -> true)
                                       .stream()
                                       .sorted((new Object() {
                                          Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                             return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                          }
                                       }).compareDistOf(x, y, z))
                                       .findFirst()
                                       .orElse(null)
                                       .discard();
                                 }

                                 if (world instanceof Level _levelx) {
                                    if (!_levelx.isClientSide()) {
                                       _levelx.playSound(
                                          null,
                                          BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.eat")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F
                                       );
                                    } else {
                                       _levelx.playLocalSound(
                                          entity.getX(),
                                          entity.getY(),
                                          entity.getZ(),
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.eat")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F,
                                          false
                                       );
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
