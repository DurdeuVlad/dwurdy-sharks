package net.mcreator.sharks.procedures;

import java.util.Comparator;
import net.mcreator.sharks.entity.RemoraEntity;
import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RemoraBucketRightclickedOnBlockProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Direction direction, Entity entity, ItemStack itemstack) {
      if (direction != null && entity != null) {
         double health = 0.0;
         double oxygen = 0.0;
         String nametag = "";
         String uuid = "";
         Entity Remora = null;
         if (entity instanceof Player) {
            if (direction != Direction.UP
               || world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() != Blocks.WATER
                  && world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() != Blocks.WATER
                  && world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() != Blocks.BUBBLE_COLUMN
                  && world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() != Blocks.AIR
                  && world.getBlockState(BlockPos.containing(x, y + 1.0, z)).getBlock() != Blocks.CAVE_AIR) {
               if (direction != Direction.DOWN
                  || world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() != Blocks.WATER
                     && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() != Blocks.WATER
                     && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() != Blocks.BUBBLE_COLUMN
                     && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() != Blocks.AIR
                     && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() != Blocks.CAVE_AIR) {
                  if (direction != Direction.NORTH
                     || world.getBlockState(BlockPos.containing(x, y, z - 1.0)).getBlock() != Blocks.WATER
                        && world.getBlockState(BlockPos.containing(x, y, z - 1.0)).getBlock() != Blocks.WATER
                        && world.getBlockState(BlockPos.containing(x, y, z - 1.0)).getBlock() != Blocks.BUBBLE_COLUMN
                        && world.getBlockState(BlockPos.containing(x, y, z - 1.0)).getBlock() != Blocks.AIR
                        && world.getBlockState(BlockPos.containing(x, y, z - 1.0)).getBlock() != Blocks.CAVE_AIR) {
                     if (direction != Direction.EAST
                        || world.getBlockState(BlockPos.containing(x + 1.0, y, z)).getBlock() != Blocks.WATER
                           && world.getBlockState(BlockPos.containing(x + 1.0, y, z)).getBlock() != Blocks.WATER
                           && world.getBlockState(BlockPos.containing(x + 1.0, y, z)).getBlock() != Blocks.BUBBLE_COLUMN
                           && world.getBlockState(BlockPos.containing(x + 1.0, y, z)).getBlock() != Blocks.AIR
                           && world.getBlockState(BlockPos.containing(x + 1.0, y, z)).getBlock() != Blocks.CAVE_AIR) {
                        if (direction != Direction.SOUTH
                           || world.getBlockState(BlockPos.containing(x, y, z + 1.0)).getBlock() != Blocks.WATER
                              && world.getBlockState(BlockPos.containing(x, y, z + 1.0)).getBlock() != Blocks.WATER
                              && world.getBlockState(BlockPos.containing(x, y, z + 1.0)).getBlock() != Blocks.BUBBLE_COLUMN
                              && world.getBlockState(BlockPos.containing(x, y, z + 1.0)).getBlock() != Blocks.AIR
                              && world.getBlockState(BlockPos.containing(x, y, z + 1.0)).getBlock() != Blocks.CAVE_AIR) {
                           if (direction == Direction.WEST
                              && (
                                 world.getBlockState(BlockPos.containing(x - 1.0, y, z)).getBlock() == Blocks.WATER
                                    || world.getBlockState(BlockPos.containing(x - 1.0, y, z)).getBlock() == Blocks.WATER
                                    || world.getBlockState(BlockPos.containing(x - 1.0, y, z)).getBlock() == Blocks.BUBBLE_COLUMN
                                    || world.getBlockState(BlockPos.containing(x - 1.0, y, z)).getBlock() == Blocks.AIR
                                    || world.getBlockState(BlockPos.containing(x - 1.0, y, z)).getBlock() == Blocks.CAVE_AIR
                              )) {
                              if (entity instanceof LivingEntity _entity) {
                                 _entity.swing(InteractionHand.MAIN_HAND, true);
                              }

                              world.setBlock(BlockPos.containing(x - 1.0, y, z), Blocks.WATER.defaultBlockState(), 3);
                              if (!world.isClientSide()) {
                                 if (world instanceof ServerLevel _level) {
                                    Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                                       .spawn(_level, BlockPos.containing(x - 0.5, y, z + 0.5), MobSpawnType.MOB_SUMMONED);
                                    if (entityToSpawn != null) {
                                       entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                                    }
                                 }

                                 if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                                    && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                                    world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                                       .stream()
                                       .sorted((new Object() {
                                          Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                             return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                          }
                                       }).compareDistOf(x, y, z))
                                       .findFirst()
                                       .orElse(null)
                                       .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                                 }

                                 if (Math.random() > 5.0) {
                                    if (world instanceof Level _levelx) {
                                       if (!_levelx.isClientSide()) {
                                          _levelx.playSound(
                                             null,
                                             BlockPos.containing(x - 1.0, y, z),
                                             (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                             SoundSource.NEUTRAL,
                                             1.0F,
                                             1.0F
                                          );
                                       } else {
                                          _levelx.playLocalSound(
                                             x - 1.0,
                                             y,
                                             z,
                                             (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                             SoundSource.NEUTRAL,
                                             1.0F,
                                             1.0F,
                                             false
                                          );
                                       }
                                    }
                                 } else if (world instanceof Level _levelxx) {
                                    if (!_levelxx.isClientSide()) {
                                       _levelxx.playSound(
                                          null,
                                          BlockPos.containing(x - 1.0, y, z),
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          0.9F
                                       );
                                    } else {
                                       _levelxx.playLocalSound(
                                          x - 1.0,
                                          y,
                                          z,
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          0.9F,
                                          false
                                       );
                                    }
                                 }
                              }

                              if (!(new Object() {
                                       public boolean checkGamemode(Entity _ent) {
                                          if (_ent instanceof ServerPlayer _serverPlayer) {
                                             return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                          } else {
                                             return _ent.level().isClientSide() && _ent instanceof Player _player
                                                ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                                   && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                      == GameType.CREATIVE
                                                : false;
                                          }
                                       }
                                    })
                                    .checkGamemode(entity)
                                 && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                                    == DwurdySharksModItems.REMORA_BUCKET.get()) {
                                 if (entity instanceof LivingEntity _entity) {
                                    _entity.swing(InteractionHand.MAIN_HAND, true);
                                 }

                                 if (entity instanceof Player _player) {
                                    ItemStack _stktoremove;
                                    ItemStack var165 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                                    _player.getInventory()
                                       .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                                 }

                                 if (entity instanceof LivingEntity _entity) {
                                    ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                                    _setstack.setCount(1);
                                    _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                                    if (_entity instanceof Player _player) {
                                       _player.getInventory().setChanged();
                                    }
                                 }
                              } else if (!(new Object() {
                                       public boolean checkGamemode(Entity _ent) {
                                          if (_ent instanceof ServerPlayer _serverPlayer) {
                                             return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                          } else {
                                             return _ent.level().isClientSide() && _ent instanceof Player _player
                                                ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                                   && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                      == GameType.CREATIVE
                                                : false;
                                          }
                                       }
                                    })
                                    .checkGamemode(entity)
                                 && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
                                    == DwurdySharksModItems.REMORA_BUCKET.get()) {
                                 if (entity instanceof LivingEntity _entityx) {
                                    _entityx.swing(InteractionHand.OFF_HAND, true);
                                 }

                                 if (entity instanceof Player _player) {
                                    ItemStack _stktoremove;
                                    ItemStack var164 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                                    _player.getInventory()
                                       .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                                 }

                                 if (entity instanceof LivingEntity _entityx) {
                                    ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                                    _setstack.setCount(1);
                                    _entityx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                                    if (_entityx instanceof Player _player) {
                                       _player.getInventory().setChanged();
                                    }
                                 }
                              }
                           }
                        } else {
                           if (entity instanceof LivingEntity _entityxx) {
                              _entityxx.swing(InteractionHand.MAIN_HAND, true);
                           }

                           world.setBlock(BlockPos.containing(x, y, z + 1.0), Blocks.WATER.defaultBlockState(), 3);
                           if (!world.isClientSide()) {
                              if (world instanceof ServerLevel _levelxxx) {
                                 Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                                    .spawn(_levelxxx, BlockPos.containing(x + 0.5, y, z + 1.5), MobSpawnType.MOB_SUMMONED);
                                 if (entityToSpawn != null) {
                                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                                 }
                              }

                              if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                                 && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                                 world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                                    .stream()
                                    .sorted((new Object() {
                                       Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                          return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                       }
                                    }).compareDistOf(x, y, z))
                                    .findFirst()
                                    .orElse(null)
                                    .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                              }

                              if (Math.random() > 5.0) {
                                 if (world instanceof Level _levelxxxx) {
                                    if (!_levelxxxx.isClientSide()) {
                                       _levelxxxx.playSound(
                                          null,
                                          BlockPos.containing(x, y, z + 1.0),
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F
                                       );
                                    } else {
                                       _levelxxxx.playLocalSound(
                                          x,
                                          y,
                                          z + 1.0,
                                          (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F,
                                          false
                                       );
                                    }
                                 }
                              } else if (world instanceof Level _levelxxxxx) {
                                 if (!_levelxxxxx.isClientSide()) {
                                    _levelxxxxx.playSound(
                                       null,
                                       BlockPos.containing(x, y, z + 1.0),
                                       (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       0.9F
                                    );
                                 } else {
                                    _levelxxxxx.playLocalSound(
                                       x,
                                       y,
                                       z + 1.0,
                                       (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       0.9F,
                                       false
                                    );
                                 }
                              }
                           }

                           if (!(new Object() {
                                    public boolean checkGamemode(Entity _ent) {
                                       if (_ent instanceof ServerPlayer _serverPlayer) {
                                          return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                       } else {
                                          return _ent.level().isClientSide() && _ent instanceof Player _player
                                             ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                                && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                   == GameType.CREATIVE
                                             : false;
                                       }
                                    }
                                 })
                                 .checkGamemode(entity)
                              && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                                 == DwurdySharksModItems.REMORA_BUCKET.get()) {
                              if (entity instanceof LivingEntity _entityxx) {
                                 _entityxx.swing(InteractionHand.MAIN_HAND, true);
                              }

                              if (entity instanceof Player _player) {
                                 ItemStack _stktoremove;
                                 ItemStack var161 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                                 _player.getInventory()
                                    .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                              }

                              if (entity instanceof LivingEntity _entityxx) {
                                 ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                                 _setstack.setCount(1);
                                 _entityxx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                                 if (_entityxx instanceof Player _player) {
                                    _player.getInventory().setChanged();
                                 }
                              }
                           } else if (!(new Object() {
                                    public boolean checkGamemode(Entity _ent) {
                                       if (_ent instanceof ServerPlayer _serverPlayer) {
                                          return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                       } else {
                                          return _ent.level().isClientSide() && _ent instanceof Player _player
                                             ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                                && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                   == GameType.CREATIVE
                                             : false;
                                       }
                                    }
                                 })
                                 .checkGamemode(entity)
                              && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
                                 == DwurdySharksModItems.REMORA_BUCKET.get()) {
                              if (entity instanceof LivingEntity _entityxxx) {
                                 _entityxxx.swing(InteractionHand.OFF_HAND, true);
                              }

                              if (entity instanceof Player _player) {
                                 ItemStack _stktoremove;
                                 ItemStack var160 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                                 _player.getInventory()
                                    .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                              }

                              if (entity instanceof LivingEntity _entityxxx) {
                                 ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                                 _setstack.setCount(1);
                                 _entityxxx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                                 if (_entityxxx instanceof Player _player) {
                                    _player.getInventory().setChanged();
                                 }
                              }
                           }
                        }
                     } else {
                        if (entity instanceof LivingEntity _entityxxxx) {
                           _entityxxxx.swing(InteractionHand.MAIN_HAND, true);
                        }

                        world.setBlock(BlockPos.containing(x + 1.0, y, z), Blocks.WATER.defaultBlockState(), 3);
                        if (!world.isClientSide()) {
                           if (world instanceof ServerLevel _levelxxxxxx) {
                              Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                                 .spawn(_levelxxxxxx, BlockPos.containing(x + 1.5, y, z + 0.5), MobSpawnType.MOB_SUMMONED);
                              if (entityToSpawn != null) {
                                 entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                              }
                           }

                           if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                              && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                              world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                                 .stream()
                                 .sorted((new Object() {
                                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                       return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                    }
                                 }).compareDistOf(x, y, z))
                                 .findFirst()
                                 .orElse(null)
                                 .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                           }

                           if (Math.random() > 5.0) {
                              if (world instanceof Level _levelxxxxxxx) {
                                 if (!_levelxxxxxxx.isClientSide()) {
                                    _levelxxxxxxx.playSound(
                                       null,
                                       BlockPos.containing(x + 1.0, y, z),
                                       (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _levelxxxxxxx.playLocalSound(
                                       x + 1.0,
                                       y,
                                       z,
                                       (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }
                           } else if (world instanceof Level _levelxxxxxxxx) {
                              if (!_levelxxxxxxxx.isClientSide()) {
                                 _levelxxxxxxxx.playSound(
                                    null,
                                    BlockPos.containing(x + 1.0, y, z),
                                    (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    0.9F
                                 );
                              } else {
                                 _levelxxxxxxxx.playLocalSound(
                                    x + 1.0,
                                    y,
                                    z,
                                    (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    0.9F,
                                    false
                                 );
                              }
                           }
                        }

                        if (!(new Object() {
                                 public boolean checkGamemode(Entity _ent) {
                                    if (_ent instanceof ServerPlayer _serverPlayer) {
                                       return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                    } else {
                                       return _ent.level().isClientSide() && _ent instanceof Player _player
                                          ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                             && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                == GameType.CREATIVE
                                          : false;
                                    }
                                 }
                              })
                              .checkGamemode(entity)
                           && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                              == DwurdySharksModItems.REMORA_BUCKET.get()) {
                           if (entity instanceof LivingEntity _entityxxxx) {
                              _entityxxxx.swing(InteractionHand.MAIN_HAND, true);
                           }

                           if (entity instanceof Player _player) {
                              ItemStack _stktoremove;
                              ItemStack var157 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                              _player.getInventory()
                                 .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                           }

                           if (entity instanceof LivingEntity _entityxxxx) {
                              ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                              _setstack.setCount(1);
                              _entityxxxx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                              if (_entityxxxx instanceof Player _player) {
                                 _player.getInventory().setChanged();
                              }
                           }
                        } else if (!(new Object() {
                                 public boolean checkGamemode(Entity _ent) {
                                    if (_ent instanceof ServerPlayer _serverPlayer) {
                                       return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                    } else {
                                       return _ent.level().isClientSide() && _ent instanceof Player _player
                                          ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                             && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                                == GameType.CREATIVE
                                          : false;
                                    }
                                 }
                              })
                              .checkGamemode(entity)
                           && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
                              == DwurdySharksModItems.REMORA_BUCKET.get()) {
                           if (entity instanceof LivingEntity _entityxxxxx) {
                              _entityxxxxx.swing(InteractionHand.OFF_HAND, true);
                           }

                           if (entity instanceof Player _player) {
                              ItemStack _stktoremove;
                              ItemStack var156 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                              _player.getInventory()
                                 .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                           }

                           if (entity instanceof LivingEntity _entityxxxxx) {
                              ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                              _setstack.setCount(1);
                              _entityxxxxx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                              if (_entityxxxxx instanceof Player _player) {
                                 _player.getInventory().setChanged();
                              }
                           }
                        }
                     }
                  } else {
                     if (entity instanceof LivingEntity _entityxxxxxx) {
                        _entityxxxxxx.swing(InteractionHand.MAIN_HAND, true);
                     }

                     world.setBlock(BlockPos.containing(x, y, z - 1.0), Blocks.WATER.defaultBlockState(), 3);
                     if (!world.isClientSide()) {
                        if (world instanceof ServerLevel _levelxxxxxxxxx) {
                           Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                              .spawn(_levelxxxxxxxxx, BlockPos.containing(x + 0.5, y, z - 0.5), MobSpawnType.MOB_SUMMONED);
                           if (entityToSpawn != null) {
                              entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                           }
                        }

                        if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                           && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                           world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                              .stream()
                              .sorted((new Object() {
                                 Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                    return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                                 }
                              }).compareDistOf(x, y, z))
                              .findFirst()
                              .orElse(null)
                              .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                        }

                        if (Math.random() > 5.0) {
                           if (world instanceof Level _levelxxxxxxxxxx) {
                              if (!_levelxxxxxxxxxx.isClientSide()) {
                                 _levelxxxxxxxxxx.playSound(
                                    null,
                                    BlockPos.containing(x, y, z - 1.0),
                                    (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              } else {
                                 _levelxxxxxxxxxx.playLocalSound(
                                    x,
                                    y,
                                    z - 1.0,
                                    (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F,
                                    false
                                 );
                              }
                           }
                        } else if (world instanceof Level _levelxxxxxxxxxxx) {
                           if (!_levelxxxxxxxxxxx.isClientSide()) {
                              _levelxxxxxxxxxxx.playSound(
                                 null,
                                 BlockPos.containing(x, y, z - 1.0),
                                 (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 0.9F
                              );
                           } else {
                              _levelxxxxxxxxxxx.playLocalSound(
                                 x,
                                 y,
                                 z - 1.0,
                                 (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 0.9F,
                                 false
                              );
                           }
                        }
                     }

                     if (!(new Object() {
                              public boolean checkGamemode(Entity _ent) {
                                 if (_ent instanceof ServerPlayer _serverPlayer) {
                                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                 } else {
                                    return _ent.level().isClientSide() && _ent instanceof Player _player
                                       ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                          && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                             == GameType.CREATIVE
                                       : false;
                                 }
                              }
                           })
                           .checkGamemode(entity)
                        && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                           == DwurdySharksModItems.REMORA_BUCKET.get()) {
                        if (entity instanceof LivingEntity _entityxxxxxx) {
                           _entityxxxxxx.swing(InteractionHand.MAIN_HAND, true);
                        }

                        if (entity instanceof Player _player) {
                           ItemStack _stktoremove;
                           ItemStack var153 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                           _player.getInventory()
                              .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                        }

                        if (entity instanceof LivingEntity _entityxxxxxx) {
                           ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                           _setstack.setCount(1);
                           _entityxxxxxx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                           if (_entityxxxxxx instanceof Player _player) {
                              _player.getInventory().setChanged();
                           }
                        }
                     } else if (!(new Object() {
                              public boolean checkGamemode(Entity _ent) {
                                 if (_ent instanceof ServerPlayer _serverPlayer) {
                                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                                 } else {
                                    return _ent.level().isClientSide() && _ent instanceof Player _player
                                       ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                          && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                             == GameType.CREATIVE
                                       : false;
                                 }
                              }
                           })
                           .checkGamemode(entity)
                        && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
                           == DwurdySharksModItems.REMORA_BUCKET.get()) {
                        if (entity instanceof LivingEntity _entityxxxxxxx) {
                           _entityxxxxxxx.swing(InteractionHand.OFF_HAND, true);
                        }

                        if (entity instanceof Player _player) {
                           ItemStack _stktoremove;
                           ItemStack var152 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                           _player.getInventory()
                              .clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                        }

                        if (entity instanceof LivingEntity _entityxxxxxxx) {
                           ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                           _setstack.setCount(1);
                           _entityxxxxxxx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                           if (_entityxxxxxxx instanceof Player _player) {
                              _player.getInventory().setChanged();
                           }
                        }
                     }
                  }
               } else {
                  if (entity instanceof LivingEntity _entityxxxxxxxx) {
                     _entityxxxxxxxx.swing(InteractionHand.MAIN_HAND, true);
                  }

                  world.setBlock(BlockPos.containing(x, y - 1.0, z), Blocks.WATER.defaultBlockState(), 3);
                  if (!world.isClientSide()) {
                     if (world instanceof ServerLevel _levelxxxxxxxxxxxx) {
                        Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                           .spawn(_levelxxxxxxxxxxxx, BlockPos.containing(x + 0.5, y - 1.0, z + 0.5), MobSpawnType.MOB_SUMMONED);
                        if (entityToSpawn != null) {
                           entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                        }
                     }

                     if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                        && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                        world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                           .stream()
                           .sorted((new Object() {
                              Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                 return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                              }
                           }).compareDistOf(x, y, z))
                           .findFirst()
                           .orElse(null)
                           .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                     }

                     if (Math.random() > 5.0) {
                        if (world instanceof Level _levelxxxxxxxxxxxxx) {
                           if (!_levelxxxxxxxxxxxxx.isClientSide()) {
                              _levelxxxxxxxxxxxxx.playSound(
                                 null,
                                 BlockPos.containing(x, y - 1.0, z),
                                 (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _levelxxxxxxxxxxxxx.playLocalSound(
                                 x,
                                 y - 1.0,
                                 z,
                                 (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (world instanceof Level _levelxxxxxxxxxxxxxx) {
                        if (!_levelxxxxxxxxxxxxxx.isClientSide()) {
                           _levelxxxxxxxxxxxxxx.playSound(
                              null,
                              BlockPos.containing(x, y - 1.0, z),
                              (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.9F
                           );
                        } else {
                           _levelxxxxxxxxxxxxxx.playLocalSound(
                              x,
                              y - 1.0,
                              z,
                              (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.9F,
                              false
                           );
                        }
                     }
                  }

                  if (!(new Object() {
                           public boolean checkGamemode(Entity _ent) {
                              if (_ent instanceof ServerPlayer _serverPlayer) {
                                 return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                              } else {
                                 return _ent.level().isClientSide() && _ent instanceof Player _player
                                    ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                       && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                          == GameType.CREATIVE
                                    : false;
                              }
                           }
                        })
                        .checkGamemode(entity)
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()
                        == DwurdySharksModItems.REMORA_BUCKET.get()) {
                     if (entity instanceof LivingEntity _entityxxxxxxxx) {
                        _entityxxxxxxxx.swing(InteractionHand.MAIN_HAND, true);
                     }

                     if (entity instanceof Player _player) {
                        ItemStack _stktoremove;
                        ItemStack var149 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                        _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                     }

                     if (entity instanceof LivingEntity _entityxxxxxxxx) {
                        ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                        _setstack.setCount(1);
                        _entityxxxxxxxx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                        if (_entityxxxxxxxx instanceof Player _player) {
                           _player.getInventory().setChanged();
                        }
                     }
                  } else if (!(new Object() {
                           public boolean checkGamemode(Entity _ent) {
                              if (_ent instanceof ServerPlayer _serverPlayer) {
                                 return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                              } else {
                                 return _ent.level().isClientSide() && _ent instanceof Player _player
                                    ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                       && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                          == GameType.CREATIVE
                                    : false;
                              }
                           }
                        })
                        .checkGamemode(entity)
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
                        == DwurdySharksModItems.REMORA_BUCKET.get()) {
                     if (entity instanceof LivingEntity _entityxxxxxxxxx) {
                        _entityxxxxxxxxx.swing(InteractionHand.OFF_HAND, true);
                     }

                     if (entity instanceof Player _player) {
                        ItemStack _stktoremove;
                        ItemStack var148 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                        _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                     }

                     if (entity instanceof LivingEntity _entityxxxxxxxxx) {
                        ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                        _setstack.setCount(1);
                        _entityxxxxxxxxx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                        if (_entityxxxxxxxxx instanceof Player _player) {
                           _player.getInventory().setChanged();
                        }
                     }
                  }
               }
            } else {
               if (entity instanceof LivingEntity _entityxxxxxxxxxx) {
                  _entityxxxxxxxxxx.swing(InteractionHand.MAIN_HAND, true);
               }

               world.setBlock(BlockPos.containing(x, y + 1.0, z), Blocks.WATER.defaultBlockState(), 3);
               if (!world.isClientSide()) {
                  if (world instanceof ServerLevel _levelxxxxxxxxxxxxxxx) {
                     Entity entityToSpawn = ((EntityType)DwurdySharksModEntities.REMORA.get())
                        .spawn(_levelxxxxxxxxxxxxxxx, BlockPos.containing(x + 0.5, y + 1.0, z + 0.5), MobSpawnType.MOB_SUMMONED);
                     if (entityToSpawn != null) {
                        entityToSpawn.setYRot(world.getRandom().nextFloat() * 360.0F);
                     }
                  }

                  if (!itemstack.getDisplayName().getString().equals("[Bucket of Remora]")
                     && !world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true).isEmpty()) {
                     world.getEntitiesOfClass(RemoraEntity.class, AABB.ofSize(new Vec3(x, y, z), 6.0, 6.0, 6.0), e -> true)
                        .stream()
                        .sorted((new Object() {
                           Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                              return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                           }
                        }).compareDistOf(x, y, z))
                        .findFirst()
                        .orElse(null)
                        .setCustomName(Component.literal(itemstack.getDisplayName().getString().replace("]", "").replace("[", "")));
                  }

                  if (Math.random() > 5.0) {
                     if (world instanceof Level _levelxxxxxxxxxxxxxxxx) {
                        if (!_levelxxxxxxxxxxxxxxxx.isClientSide()) {
                           _levelxxxxxxxxxxxxxxxx.playSound(
                              null,
                              BlockPos.containing(x, y + 1.0, z),
                              (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _levelxxxxxxxxxxxxxxxx.playLocalSound(
                              x,
                              y + 1.0,
                              z,
                              (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (world instanceof Level _levelxxxxxxxxxxxxxxxxx) {
                     if (!_levelxxxxxxxxxxxxxxxxx.isClientSide()) {
                        _levelxxxxxxxxxxxxxxxxx.playSound(
                           null,
                           BlockPos.containing(x, y + 1.0, z),
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.9F
                        );
                     } else {
                        _levelxxxxxxxxxxxxxxxxx.playLocalSound(
                           x,
                           y + 1.0,
                           z,
                           (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.9F,
                           false
                        );
                     }
                  }
               }

               if (!(new Object() {
                        public boolean checkGamemode(Entity _ent) {
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                           } else {
                              return _ent.level().isClientSide() && _ent instanceof Player _player
                                 ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                    && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                       == GameType.CREATIVE
                                 : false;
                           }
                        }
                     })
                     .checkGamemode(entity)
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == DwurdySharksModItems.REMORA_BUCKET.get()
                  )
                {
                  if (entity instanceof LivingEntity _entityxxxxxxxxxx) {
                     _entityxxxxxxxxxx.swing(InteractionHand.MAIN_HAND, true);
                  }

                  if (entity instanceof Player _player) {
                     ItemStack _stktoremove;
                     ItemStack var145 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                     _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                  }

                  if (entity instanceof LivingEntity _entityxxxxxxxxxx) {
                     ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                     _setstack.setCount(1);
                     _entityxxxxxxxxxx.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                     if (_entityxxxxxxxxxx instanceof Player _player) {
                        _player.getInventory().setChanged();
                     }
                  }
               } else if (!(new Object() {
                        public boolean checkGamemode(Entity _ent) {
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                           } else {
                              return _ent.level().isClientSide() && _ent instanceof Player _player
                                 ? Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                    && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode()
                                       == GameType.CREATIVE
                                 : false;
                           }
                        }
                     })
                     .checkGamemode(entity)
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == DwurdySharksModItems.REMORA_BUCKET.get()) {
                  if (entity instanceof LivingEntity _entityxxxxxxxxxxx) {
                     _entityxxxxxxxxxxx.swing(InteractionHand.OFF_HAND, true);
                  }

                  if (entity instanceof Player _player) {
                     ItemStack _stktoremove;
                     ItemStack var144 = _stktoremove = entity instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                     _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                  }

                  if (entity instanceof LivingEntity _entityxxxxxxxxxxx) {
                     ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
                     _setstack.setCount(1);
                     _entityxxxxxxxxxxx.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                     if (_entityxxxxxxxxxxx instanceof Player _player) {
                        _player.getInventory().setChanged();
                     }
                  }
               }
            }
         }
      }
   }
}
