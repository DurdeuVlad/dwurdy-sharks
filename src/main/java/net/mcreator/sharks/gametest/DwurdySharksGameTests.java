package net.mcreator.sharks.gametest;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import net.mcreator.sharks.DwurdySharksMod;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.mcreator.sharks.init.DwurdySharksModEntities;
import net.mcreator.sharks.init.DwurdySharksModGameRules;
import net.mcreator.sharks.init.DwurdySharksModMobEffects;
import net.mcreator.sharks.init.DwurdySharksBiomeTags;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.mcreator.sharks.procedures.FollowIfTamedProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;

@GameTestHolder(DwurdySharksMod.MODID)
public class DwurdySharksGameTests {
   /**
    * Tests that place fake players, mutate gamerules/config across tick boundaries, or assert
    * shared-state-sensitive outcomes run under this lock: GameTest executes tests concurrently in
    * one dimension, so unlocked tests would see each other's players, caps, and config values.
    * Holders release at their terminal callback; the safety release prevents a timed-out test from
    * deadlocking the queue.
    */
   private static final java.util.concurrent.atomic.AtomicBoolean EXCLUSIVE = new java.util.concurrent.atomic.AtomicBoolean();

   /**
    * Runs {@code body} once no other exclusive test is running; retries every 20 ticks.
    * {@code body} receives the tick at which the lock was acquired — all timed scheduling inside
    * the body must be relative to it (runAtTickTime is absolute and timeouts count from placement).
    * {@code runTicks} is the body's expected runtime; a safety release fires just past it so a
    * crashed holder cannot deadlock the queue.
    */
   private static void exclusive(GameTestHelper helper, long runTicks, java.util.function.LongConsumer body) {
      runExclusive(helper, runTicks, body);
   }

   private static void runExclusive(GameTestHelper helper, long runTicks, java.util.function.LongConsumer body) {
      if (EXCLUSIVE.compareAndSet(false, true)) {
         long base = helper.getTick();
         helper.runAtTickTime(base + runTicks + 40L, () -> EXCLUSIVE.set(false));
         try {
            body.accept(base);
         } catch (Throwable t) {
            EXCLUSIVE.set(false);
            throw t;
         }
      } else {
         helper.runAtTickTime(helper.getTick() + 20L, () -> runExclusive(helper, runTicks, body));
      }
   }

   private static Runnable guarded(Runnable body) {
      return () -> {
         try {
            body.run();
         } catch (Throwable t) {
            EXCLUSIVE.set(false);
            throw t;
         }
      };
   }

   private static Runnable guardedEnd(Runnable body) {
      return () -> {
         EXCLUSIVE.set(false);
         body.run();
      };
   }

   @GameTest(template = "pool", batch = "aggressive_off", timeoutTicks = 1500)
   public static void sharkIgnoresPlayerWhenRuleOff(GameTestHelper helper) {
      exclusive(helper, 250, base -> sharkIgnoresPlayerWhenRuleOffImpl(helper, base));
   }

   private static void sharkIgnoresPlayerWhenRuleOffImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      boolean prevAggro = level.getGameRules().getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BullSharkEntity shark = helper.spawn(DwurdySharksModEntities.BULL_SHARK.get(), 3, 2, 3);
      placeSurvivalPlayer(helper, 3, 2, 4);
      helper.runAtTickTime(base + 200L, guardedEnd(() -> {
         level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
         helper.assertTrue(shark.getTarget() == null, "bull shark targeted a player with aggressiveSharks=false");
         helper.succeed();
      }));
   }

   @GameTest(template = "pool", batch = "aggressive_on", timeoutTicks = 1500)
   public static void sharkTargetsPlayerWhenRuleOn(GameTestHelper helper) {
      exclusive(helper, 250, base -> sharkTargetsPlayerWhenRuleOnImpl(helper, base));
   }

   private static void sharkTargetsPlayerWhenRuleOnImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      boolean prevAggro = level.getGameRules().getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      BullSharkEntity shark = helper.spawn(DwurdySharksModEntities.BULL_SHARK.get(), 3, 2, 3);
      ServerPlayer player = placeSurvivalPlayer(helper, 3, 2, 4);
      helper.succeedWhen(() -> {
         helper.assertTrue(shark.getTarget() == player, "bull shark did not target a player with aggressiveSharks=true");
         level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
         EXCLUSIVE.set(false);
      });
   }

   @GameTest(template = "pool", batch = "aggressive_whaleshark", timeoutTicks = 1500)
   public static void filterFeederIgnoresPlayerWhenRuleOn(GameTestHelper helper) {
      exclusive(helper, 250, base -> filterFeederIgnoresPlayerWhenRuleOnImpl(helper, base));
   }

   private static void filterFeederIgnoresPlayerWhenRuleOnImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      boolean prevAggro = level.getGameRules().getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      WhaleSharkEntity shark = helper.spawn(DwurdySharksModEntities.WHALE_SHARK.get(), 3, 2, 3);
      placeSurvivalPlayer(helper, 3, 2, 4);
      helper.runAtTickTime(base + 200L, guardedEnd(() -> {
         level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
         helper.assertTrue(shark.getTarget() == null, "whale shark targeted a player with aggressiveSharks=true");
         helper.succeed();
      }));
   }

   @GameTest(template = "pool", batch = "aggressive_tamed", timeoutTicks = 1500)
   public static void tamedSharkDoesNotTargetOwner(GameTestHelper helper) {
      exclusive(helper, 250, base -> tamedSharkDoesNotTargetOwnerImpl(helper, base));
   }

   private static void tamedSharkDoesNotTargetOwnerImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      boolean prevAggro = level.getGameRules().getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      NurseSharkEntity shark = helper.spawn(DwurdySharksModEntities.NURSE_SHARK.get(), 3, 2, 3);
      ServerPlayer owner = placeSurvivalPlayer(helper, 3, 2, 4);
      shark.tame(owner);
      helper.runAtTickTime(base + 200L, guardedEnd(() -> {
         level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
         helper.assertTrue(shark.getTarget() == null, "tamed nurse shark targeted its owner with aggressiveSharks=true");
         helper.succeed();
      }));
   }

   @GameTest(template = "pool", batch = "tamed_follow", timeoutTicks = 60)
   public static void tamedSharkDoesNotPathToOwnerOnLand(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      fillPool(helper);
      NurseSharkEntity shark = helper.spawn(DwurdySharksModEntities.NURSE_SHARK.get(), 2, 2, 2);
      ServerPlayer owner = placeSurvivalPlayer(helper, 7, 1, 7);
      shark.tame(owner);
      helper.runAtTickTime(3L, () -> {
         shark.getNavigation().stop();
         FollowIfTamedProcedure.execute(level, shark.getX(), shark.getY(), shark.getZ(), shark);
         helper.assertTrue(shark.getNavigation().getPath() == null, "tamed shark pathed toward owner standing on land");

         BlockPos waterPos = helper.absolutePos(new BlockPos(3, 2, 4));
         owner.moveTo(waterPos.getX() + 0.5, waterPos.getY(), waterPos.getZ() + 0.5, 0.0F, 0.0F);
         helper.assertTrue(shark.getOwner() == owner, "getOwner did not resolve the mock player");
         helper.assertTrue(shark.isOwnedBy(owner), "isOwnedBy false for mock player");
         helper.assertTrue(
            level.getFluidState(BlockPos.containing(owner.position())).is(net.minecraft.tags.FluidTags.WATER), "owner position not in water"
         );
         helper.assertTrue(
            !level.getEntitiesOfClass(Player.class, net.minecraft.world.phys.AABB.ofSize(shark.position(), 64.0, 64.0, 64.0), e -> true).isEmpty(),
            "no player found in the procedure's 64-block AABB"
         );
         helper.assertTrue(shark.isInWaterOrBubble(), "shark itself is not in water after ticking");
         shark.getNavigation().stop();
         FollowIfTamedProcedure.execute(level, shark.getX(), shark.getY(), shark.getZ(), shark);
         helper.assertTrue(shark.getNavigation().getPath() != null, "tamed shark did not path toward owner in water");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "stress_population_100", timeoutTicks = 20000000, required = false)
   public static void sharkPopulation100TickCost(GameTestHelper helper) {
      runPopulationStress(helper, Integer.getInteger("sharks.stress.population", 100), "stress_population_100");
   }

   @GameTest(template = "pool", batch = "stress_population_500", timeoutTicks = 20000000, required = false)
   public static void sharkPopulation500TickCost(GameTestHelper helper) {
      runPopulationStress(helper, Integer.getInteger("sharks.stress.population.500", 500), "stress_population_500");
   }

   private static void runPopulationStress(GameTestHelper helper, int target, String tag) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      int prevCramming = rules.getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
      rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(0, level.getServer());
      fillPool(helper);
      fillUpperPool(helper);
      EntityType<?>[] types = new EntityType[]{
         DwurdySharksModEntities.BULL_SHARK.get(), DwurdySharksModEntities.TIGER_SHARK.get(),
         DwurdySharksModEntities.MAKO_SHARK.get(), DwurdySharksModEntities.LEMON_SHARK.get(),
         DwurdySharksModEntities.BLUE_SHARK.get(), DwurdySharksModEntities.BASKING_SHARK.get(),
         DwurdySharksModEntities.WHALE_SHARK.get(), DwurdySharksModEntities.BLACKTIP_REEF_SHARK.get(),
         DwurdySharksModEntities.BONNETHEAD_SHARK.get(), DwurdySharksModEntities.BARRACUDA.get()
      };
      BlockPos center = helper.absolutePos(new BlockPos(2, 1, 2));
      purgeModMobs(helper);
      int[] spawned = new int[]{0};
      long runSeconds = Long.getLong("sharks.stress.seconds", 300L);
      for (int i = 0; i < target; i++) {
         try {
            helper.spawn(types[i % types.length], 1 + i % 4, 1 + (i / 16) % 4, 1 + (i / 4) % 4);
            spawned[0]++;
         } catch (Throwable ignored) {
         }
      }
      placeSurvivalPlayer(helper, 3, 2, 4);
      long start = System.currentTimeMillis();
      scheduleEvery(helper, 1200, () -> {
         int alive = countModEntities(level, helper.getBounds().inflate(4.0));
         for (int i = alive; i < target; i++) {
            try {
               helper.spawn(types[i % types.length], 1 + i % 4, 1 + (i / 16) % 4, 1 + (i / 4) % 4);
               spawned[0]++;
            } catch (Throwable ignored) {
            }
         }
         double mspt = tickAvgMs(level);
         double p95 = tickP95Ms(level);
         long elapsedSec = (System.currentTimeMillis() - start) / 1000;
         DwurdySharksMod.LOGGER.info(
            "[{}] {}s elapsed | {} spawned incl. top-up, {} alive of target {}, avg tick {} ms, p95 tick {} ms (aggressiveSharks=true, survival player present)",
            tag, elapsedSec, spawned[0], alive, target, fmt(mspt), fmt(p95));
         helper.assertTrue(alive > 0, tag + " load vanished: no mod entities remain alive");
      });
      scheduleEvery(helper, 100, () -> {
         long elapsedSec = (System.currentTimeMillis() - start) / 1000;
         if (elapsedSec >= runSeconds) {
            int alive = countModEntities(level, helper.getBounds().inflate(4.0));
            double p95 = tickP95Ms(level);
            helper.assertTrue(alive >= target / 4,
               tag + " sustained load decayed to " + alive + "/" + target + " despite top-up");
            helper.assertTrue(p95 < 50.0, tag + " P95 tick " + fmt(p95) + " ms exceeded 50 ms budget");
            rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(prevCramming, level.getServer());
            helper.succeed();
         }
      });
   }

   @GameTest(template = "pool", batch = "stress_baseline", timeoutTicks = 20000000, required = false)
   public static void baselineIdleTickCost(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos center = helper.absolutePos(new BlockPos(2, 1, 2));
      int leaked = purgeModMobs(helper);
      if (leaked > 0) {
         DwurdySharksMod.LOGGER.warn("[stress_baseline] purged {} mod entities leaked from earlier batches", leaked);
      }
      long start = System.currentTimeMillis();
      long runSeconds = Long.getLong("sharks.stress.seconds", 300L);
      scheduleEvery(helper, 100, () -> {
         long elapsedSec = (System.currentTimeMillis() - start) / 1000;
         if (elapsedSec >= runSeconds) {
            int remaining = countModEntities(level, helper.getBounds().inflate(16.0));
            double mspt = tickAvgMs(level);
            double p95 = tickP95Ms(level);
            DwurdySharksMod.LOGGER.info(
               "[stress_baseline] no sharks ({} mod entities within 96 blocks after purge), avg tick {} ms, p95 tick {} ms",
               remaining, fmt(mspt), fmt(p95));
            helper.assertTrue(remaining == 0, "baseline world contaminated by " + remaining + " surviving mod entities");
            helper.assertTrue(p95 < 50.0, "baseline P95 tick " + fmt(p95) + " ms exceeded 50 ms");
            helper.succeed();
         }
      });
   }

   @GameTest(template = "pool", batch = "stress_dryout_beached", timeoutTicks = 20000000, required = false)
   public static void dryoutBeachedSharksTickCost(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevCramming = rules.getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
      rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(0, level.getServer());
      int queueAtStart = DwurdySharksMod.getPendingServerWork();
      EntityType<?>[] types = new EntityType[]{
         DwurdySharksModEntities.BULL_SHARK.get(), DwurdySharksModEntities.TIGER_SHARK.get(),
         DwurdySharksModEntities.LEMON_SHARK.get(), DwurdySharksModEntities.BLUE_SHARK.get(),
         DwurdySharksModEntities.BLACKTIP_REEF_SHARK.get()
      };
      int[] spawned = new int[]{0};
      scheduleEvery(helper, 1200, () -> {
         for (int i = 0; i < 60; i++) {
            try {
               helper.spawn(types[i % types.length], 1 + i % 4, 1, 1 + i / 4 % 4);
               spawned[0]++;
            } catch (Throwable ignored) {
            }
         }
      });
      BlockPos center = helper.absolutePos(new BlockPos(2, 1, 2));
      long start = System.currentTimeMillis();
      long runSeconds = Long.getLong("sharks.stress.seconds", 300L);
      scheduleEvery(helper, 100, () -> {
         long elapsedSec = (System.currentTimeMillis() - start) / 1000;
         if (elapsedSec >= runSeconds) {
            for (int i = 0; i < 20; i++) {
               try {
                  helper.spawn(types[i % types.length], 1 + i % 4, 1, 1 + i / 4 % 4);
                  spawned[0]++;
               } catch (Throwable ignored) {
               }
            }
            int beached = countModEntities(level, helper.getBounds().inflate(4.0));
            int queue = DwurdySharksMod.getPendingServerWork();
            double mspt = tickAvgMs(level);
            double p95 = tickP95Ms(level);
            DwurdySharksMod.LOGGER.info(
               "[stress_dryout] {} beached sharks spawned, {} alive (dryout path), workQueue={}, avg tick {} ms, p95 tick {} ms",
               spawned[0], beached, queue, fmt(mspt), fmt(p95));
            helper.assertTrue(beached > 0, "dryout stress load vanished: no beached sharks alive");
            helper.assertTrue(queue <= queueAtStart + 128 + 8 * beached,
               "workQueue grew from " + queueAtStart + " to " + queue + " with " + beached
                  + " beached sharks (super-linear queue/beach correlation regression)");
            rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(prevCramming, level.getServer());
            helper.succeed();
         }
      });
   }

   @GameTest(template = "pool", batch = "stress_item_eat", timeoutTicks = 20000000, required = false)
   public static void droppedFoodEatersTickCost(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      GameRules rules = level.getGameRules();
      int prevCramming = rules.getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
      rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(0, level.getServer());
      fillPool(helper);
      fillUpperPool(helper);
      EntityType<?>[] types = new EntityType[]{
         DwurdySharksModEntities.BASKING_SHARK.get(), DwurdySharksModEntities.WHALE_SHARK.get(),
         DwurdySharksModEntities.TIGER_SHARK.get(), DwurdySharksModEntities.LEMON_SHARK.get(),
         DwurdySharksModEntities.BLUE_SHARK.get()
      };
      BlockPos center = helper.absolutePos(new BlockPos(2, 1, 2));
      purgeModMobs(helper);
      int[] spawned = new int[]{0};
      for (int i = 0; i < 70; i++) {
         try {
            helper.spawn(types[i % types.length], 1 + i % 4, 1 + i / 50, 1 + i / 4 % 4);
            spawned[0]++;
         } catch (Throwable ignored) {
         }
      }
      scheduleEvery(helper, 600, () -> {
         for (int i = 0; i < 30; i++) {
            BlockPos p = helper.absolutePos(new BlockPos(1 + i % 4, 2, 1 + i / 4 % 4));
            level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(level, p.getX(), p.getY(), p.getZ(),
               new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.APPLE)));
         }
         int eaters = countModEntities(level, helper.getBounds().inflate(4.0));
         for (int i = eaters; i < 70; i++) {
            try {
               helper.spawn(types[i % types.length], 1 + i % 4, 1 + i / 50, 1 + i / 4 % 4);
               spawned[0]++;
            } catch (Throwable ignored) {
            }
         }
      });
      long start = System.currentTimeMillis();
      long runSeconds = Long.getLong("sharks.stress.seconds", 300L);
      scheduleEvery(helper, 100, () -> {
         long elapsedSec = (System.currentTimeMillis() - start) / 1000;
         if (elapsedSec >= runSeconds) {
            int alive = countModEntities(level, helper.getBounds().inflate(4.0));
            double mspt = tickAvgMs(level);
            double p95 = tickP95Ms(level);
            DwurdySharksMod.LOGGER.info(
               "[stress_item_eat] {} eater sharks spawned incl. top-up ({} alive) + food drops every 600t, avg tick {} ms, p95 tick {} ms",
               spawned[0], alive, fmt(mspt), fmt(p95));
            helper.assertTrue(alive > 0, "item-eat stress load vanished: no mod entities remain alive");
            helper.assertTrue(p95 < 50.0, "item-eat P95 tick " + fmt(p95) + " ms exceeded 50 ms budget");
            rules.getRule(GameRules.RULE_MAX_ENTITY_CRAMMING).set(prevCramming, level.getServer());
            helper.succeed();
         }
      });
   }

   @GameTest(template = "pool", batch = "cap", timeoutTicks = 400)
   public static void localCapBoundsNaturalSpawns(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevCap = rules.getInt(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP);
      int prevRadius = rules.getInt(DwurdySharksModGameRules.SPAWN_CAP_RADIUS);
      boolean prevAggro = rules.getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      placeSurvivalPlayer(helper, 2, 2, 4);
      try {
         int admitted = 0;
         for (int i = 0; i < 40; i++) {
            Entity e = DwurdySharksModEntities.BULL_SHARK.get().spawn(level, base, MobSpawnType.NATURAL);
            if (e != null) {
               e.setInvulnerable(true);
               admitted++;
            }
         }
         helper.assertTrue(admitted == 8,
            "expected spawn plateau at largeSharkLocalCap=8, but " + admitted + " of 40 natural attempts were admitted");
         helper.runAtTickTime(40L, () -> {
            int inWorld = countLargeSharks(level, helper.getBounds().inflate(4.0));
            helper.assertTrue(inWorld == 8,
               "in-world large shark count " + inWorld + " != cap 8 (cancelled spawns leaked into the world)");
            helper.succeed();
         });
      } finally {
         rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(prevCap, level.getServer());
         rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(prevRadius, level.getServer());
         rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
      }
   }

   @GameTest(template = "pool", batch = "cap_exemptions", timeoutTicks = 200)
   public static void capExemptSharksDoNotConsumeWildCap(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevCap = rules.getInt(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP);
      int prevRadius = rules.getInt(DwurdySharksModGameRules.SPAWN_CAP_RADIUS);
      boolean prevAggro = rules.getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      ServerPlayer owner = placeSurvivalPlayer(helper, 3, 2, 4);
      try {
         int admitted = 0;
         for (int i = 0; i < 12; i++) {
            Entity e = DwurdySharksModEntities.BULL_SHARK.get().spawn(level, base, MobSpawnType.NATURAL);
            if (e != null) {
               e.setInvulnerable(true);
               admitted++;
            }
         }
         helper.assertTrue(admitted == 8, "cap did not plateau at 8 during fill phase (admitted " + admitted + ")");

         NurseSharkEntity tamed = DwurdySharksModEntities.NURSE_SHARK.get().create(level);
         tamed.moveTo(base.getX() + 0.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
         tamed.tame(owner);
         BullSharkEntity named = DwurdySharksModEntities.BULL_SHARK.get().create(level);
         named.moveTo(base.getX() + 1.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
         named.setCustomName(Component.literal("Tagged"));
         BullSharkEntity persistent = DwurdySharksModEntities.BULL_SHARK.get().create(level);
         persistent.moveTo(base.getX() + 0.5, base.getY(), base.getZ() + 1.5, 0.0F, 0.0F);
         persistent.setPersistenceRequired();
         BullSharkEntity control = DwurdySharksModEntities.BULL_SHARK.get().create(level);
         control.moveTo(base.getX() + 1.5, base.getY(), base.getZ() + 1.5, 0.0F, 0.0F);

         for (Mob candidate : new Mob[]{tamed, named, persistent, control}) {
            EventHooks.finalizeMobSpawn(candidate, level, level.getCurrentDifficultyAt(base), MobSpawnType.NATURAL, null);
         }
         helper.assertTrue(level.addFreshEntity(tamed), "tamed shark spawn was cancelled despite tamed exemption");
         helper.assertTrue(level.addFreshEntity(named), "named shark spawn was cancelled despite custom-name exemption");
         helper.assertTrue(level.addFreshEntity(persistent), "persistent shark spawn was cancelled despite persistence exemption");
         helper.assertFalse(level.addFreshEntity(control), "9th wild shark joined the world despite a full cap");
         control.discard();

         helper.runAtTickTime(20L, () -> {
            int inWorld = countLargeSharks(level, helper.getBounds().inflate(4.0));
            helper.assertTrue(inWorld == 11,
               "expected 8 wild + 3 exempt = 11 large sharks, found " + inWorld);
            helper.succeed();
         });
      } finally {
         rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(prevCap, level.getServer());
         rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(prevRadius, level.getServer());
         rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
      }
   }

   @GameTest(template = "pool", batch = "cap_abuse", timeoutTicks = 400)
   public static void capPlateausUnderAbuseSpawnStorm(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevCap = rules.getInt(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP);
      int prevRadius = rules.getInt(DwurdySharksModGameRules.SPAWN_CAP_RADIUS);
      boolean prevAggro = rules.getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      placeSurvivalPlayer(helper, 2, 2, 4);
      int attempts = Integer.getInteger("sharks.stress.abuseAttempts", 2000);
      int queueBefore = DwurdySharksMod.getPendingServerWork();
      try {
         int admitted = 0;
         for (int i = 0; i < attempts; i++) {
            Entity e = DwurdySharksModEntities.BULL_SHARK.get().spawn(level, base, MobSpawnType.NATURAL);
            if (e != null) {
               e.setInvulnerable(true);
               admitted++;
            }
         }
         int total = admitted;
         helper.assertTrue(total == 8,
            "abuse storm: " + attempts + " spawn attempts admitted " + total + ", expected cap plateau at 8");
         helper.runAtTickTime(60L, () -> {
            int inWorld = countLargeSharks(level, helper.getBounds().inflate(4.0));
            helper.assertTrue(inWorld == 8, "post-storm in-world large sharks " + inWorld + " != 8");
            helper.assertTrue(DwurdySharksMod.getPendingServerWork() <= queueBefore + 128,
               "workQueue grew from " + queueBefore + " to " + DwurdySharksMod.getPendingServerWork() + " during spawn storm");
         });
         helper.runAtTickTime(160L, () -> {
            double p95 = tickP95Ms(level);
            DwurdySharksMod.LOGGER.info("[stress_cap_abuse] {} attempts, {} admitted, post-settle p95 tick {} ms",
               attempts, total, fmt(p95));
            helper.assertTrue(p95 < 50.0, "post-storm P95 tick " + fmt(p95) + " ms exceeded 50 ms budget");
            helper.succeed();
         });
      } finally {
         rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(prevCap, level.getServer());
         rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(prevRadius, level.getServer());
         rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
      }
   }

   @GameTest(template = "pool", batch = "cap_disabled", timeoutTicks = 600)
   public static void capDisabledLoadIsReclaimed(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevCap = rules.getInt(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP);
      int prevRadius = rules.getInt(DwurdySharksModGameRules.SPAWN_CAP_RADIUS);
      boolean prevAggro = rules.getBoolean(DwurdySharksModGameRules.AGGRESSIVE_SHARKS);
      rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(0, level.getServer());
      rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(8, level.getServer());
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      fillUpperPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      placeSurvivalPlayer(helper, 2, 2, 4);
      int target = Integer.getInteger("sharks.stress.capDisabledCount", 150);
      int queueBefore = DwurdySharksMod.getPendingServerWork();
      try {
         Runtime runtime = Runtime.getRuntime();
         System.gc();
         System.gc();
         long heapBefore = runtime.totalMemory() - runtime.freeMemory();
         int admitted = 0;
         for (int i = 0; i < target; i++) {
            BlockPos pos = helper.absolutePos(new BlockPos(1 + i % 4, 1 + i / 60, 1 + i / 4 % 4));
            Entity e = DwurdySharksModEntities.BULL_SHARK.get().spawn(level, pos, MobSpawnType.NATURAL);
            if (e != null) {
               e.setInvulnerable(true);
               admitted++;
            }
         }
         helper.assertTrue(admitted == target,
            "cap disabled (cap=0) still rejected " + (target - admitted) + " of " + target + " natural spawns");
         int total = admitted;
         helper.runAtTickTime(300L, () -> {
            int alive = countModEntities(level, helper.getBounds().inflate(4.0));
            double p95 = tickP95Ms(level);
            DwurdySharksMod.LOGGER.info("[stress_cap_disabled] {} spawned, {} alive, loaded p95 tick {} ms",
               total, alive, fmt(p95));
            helper.assertTrue(alive == total,
               "cap-disabled population shrank before despawn phase: " + alive + "/" + total);
            for (Mob mob : level.getEntitiesOfClass(Mob.class, helper.getBounds().inflate(4.0),
               e -> DwurdySharksConfig.isModEntity(e.getType()))) {
               mob.discard();
            }
         });
         helper.runAtTickTime(340L, () -> {
            System.gc();
            System.gc();
         });
         helper.runAtTickTime(400L, () -> {
            long heapAfter = runtime.totalMemory() - runtime.freeMemory();
            long deltaMb = (heapAfter - heapBefore) / (1024 * 1024);
            int remaining = countModEntities(level, helper.getBounds().inflate(4.0));
            DwurdySharksMod.LOGGER.info(
               "[stress_cap_disabled] heap before {} MB, after despawn+GC {} MB, delta {} MB, {} mod entities remaining",
               heapBefore / (1024 * 1024), heapAfter / (1024 * 1024), deltaMb, remaining);
            helper.assertTrue(remaining == 0, remaining + " cap-disabled entities survived despawn");
            helper.assertTrue(heapAfter - heapBefore < 128L * 1024 * 1024,
               "heap grew " + deltaMb + " MB after despawn+GC (possible entity reference leak)");
            helper.assertTrue(DwurdySharksMod.getPendingServerWork() <= queueBefore + 128,
               "workQueue grew from " + queueBefore + " to " + DwurdySharksMod.getPendingServerWork() + " during cap-disabled run");
            helper.succeed();
         });
      } finally {
         rules.getRule(DwurdySharksModGameRules.LARGE_SHARK_LOCAL_CAP).set(prevCap, level.getServer());
         rules.getRule(DwurdySharksModGameRules.SPAWN_CAP_RADIUS).set(prevRadius, level.getServer());
         rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(prevAggro, level.getServer());
      }
   }

   @GameTest(template = "pool", batch = "spawn_biomes", timeoutTicks = 300)
   public static void naturalSpawnsRejectedOutsideOceanBiomes(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      helper.assertTrue(DwurdySharksConfig.OCEAN_ONLY.get(),
         "oceanOnly config is disabled; the ocean-biome spawn gate cannot be verified");
      fillPool(helper);
      BlockPos waterPos = helper.absolutePos(new BlockPos(2, 1, 2));
      helper.assertTrue(level.getBlockState(waterPos).is(Blocks.WATER), "test setup broken: spawn position is not water");
      helper.assertTrue(level.getBlockState(waterPos.above()).is(Blocks.WATER), "test setup broken: no water above spawn position");

      for (ResourceKey<Biome> key : new ResourceKey[]{Biomes.RIVER, Biomes.FROZEN_RIVER, Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.PLAINS}) {
         fillTestBiome(helper, key);
         helper.assertTrue(level.getBiome(waterPos).is(key), "biome " + key.location() + " not readable at spawn position after fill");
         helper.assertFalse(
            SpawnPlacements.checkSpawnRules(DwurdySharksModEntities.BULL_SHARK.get(), level, MobSpawnType.NATURAL, waterPos, level.random),
            "spawn predicate accepted bull shark in non-ocean biome " + key.location());
      }
      for (ResourceKey<Biome> key : new ResourceKey[]{Biomes.WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.OCEAN}) {
         fillTestBiome(helper, key);
         helper.assertTrue(level.getBiome(waterPos).is(key), "biome " + key.location() + " not readable at spawn position after fill");
         helper.assertTrue(
            SpawnPlacements.checkSpawnRules(DwurdySharksModEntities.BULL_SHARK.get(), level, MobSpawnType.NATURAL, waterPos, level.random),
            "spawn predicate rejected bull shark in ocean biome " + key.location());
      }

      List<String> offenders = new ArrayList<>();
      int oceanSpawnBiomes = 0;
      for (Holder.Reference<Biome> biome : level.registryAccess().registryOrThrow(Registries.BIOME).holders().toList()) {
         Set<ResourceLocation> modTypes = new HashSet<>();
         for (MobCategory category : MobCategory.values()) {
            for (MobSpawnSettings.SpawnerData data : biome.value().getMobSettings().getMobs(category).unwrap()) {
               if (DwurdySharksConfig.isModEntity(data.type)) {
                  modTypes.add(BuiltInRegistries.ENTITY_TYPE.getKey(data.type));
               }
            }
         }
         if (!modTypes.isEmpty()) {
            if (biome.is(DwurdySharksBiomeTags.SHARK_SPAWNING_OCEANS)) {
               oceanSpawnBiomes++;
            } else {
               offenders.add(biome.key().location() + "=" + modTypes);
            }
         }
      }
      helper.assertTrue(offenders.isEmpty(), "mod natural spawn data present in biomes outside shark_spawning_oceans: " + offenders);
      helper.assertTrue(oceanSpawnBiomes > 0, "no shark_spawning_oceans biome carries mod spawn data; biome modifiers not applied");
      DwurdySharksMod.LOGGER.info("[spawn_biomes] {} ocean-tagged biomes carry mod spawn data, 0 offenders", oceanSpawnBiomes);
      helper.succeed();
   }

   @GameTest(template = "pool", batch = "dryout_once", timeoutTicks = 2000)
   public static void dryoutAppliesOnceAndDoesNotQueueWork(GameTestHelper helper) {
      exclusive(helper, 850, base -> dryoutAppliesOnceAndDoesNotQueueWorkImpl(helper, base));
   }

   private static void dryoutAppliesOnceAndDoesNotQueueWorkImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      BlockPos pos = helper.absolutePos(new BlockPos(2, 1, 2));
      BullSharkEntity shark = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      shark.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      shark.setPersistenceRequired();
      shark.setInvulnerable(true);
      level.addFreshEntity(shark);
      int queueAtStart = DwurdySharksMod.getPendingServerWork();
      int[] firstDuration = new int[]{-1};
      helper.runAtTickTime(base + 700L, guarded(() -> {
         MobEffectInstance instance = shark.getEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT);
         helper.assertTrue(shark.isAlive(), "beached shark died before the dryout observation window");
         helper.assertTrue(instance != null, "beached shark never received the dryout effect (600-tick delay elapsed)");
         firstDuration[0] = instance.getDuration();
         helper.assertTrue(firstDuration[0] < 600,
            "dryout effect duration " + firstDuration[0] + " should already be counting down at tick 700");
         helper.assertTrue(DwurdySharksMod.getPendingServerWork() <= queueAtStart + 128,
            "workQueue grew from " + queueAtStart + " to " + DwurdySharksMod.getPendingServerWork() + " while shark was beached");
      }));
      helper.runAtTickTime(base + 800L, guardedEnd(() -> {
         MobEffectInstance instance = shark.getEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT);
         helper.assertTrue(instance != null, "dryout effect expired before its duration elapsed");
         helper.assertTrue(instance.getDuration() < firstDuration[0],
            "dryout effect was re-applied instead of ticking down (duration " + instance.getDuration()
               + " vs earlier " + firstDuration[0] + ")");
         helper.assertTrue(DwurdySharksMod.getPendingServerWork() <= queueAtStart + 128,
            "workQueue grew past " + (queueAtStart + 128) + " while shark was beached (queue/beach correlation regression)");
         helper.succeed();
      }));
   }

   @GameTest(template = "pool", batch = "despawn", timeoutTicks = 400)
   public static void wildSharkDespawnsTamedDoesNot(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BlockPos wildPos = helper.absolutePos(new BlockPos(2, 2, 2));
      BullSharkEntity wild = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      wild.moveTo(wildPos.getX() + 0.5, wildPos.getY(), wildPos.getZ() + 0.5, 0.0F, 0.0F);
      level.addFreshEntity(wild);
      BlockPos tamedPos = helper.absolutePos(new BlockPos(4, 2, 2));
      NurseSharkEntity tamed = DwurdySharksModEntities.NURSE_SHARK.get().create(level);
      tamed.moveTo(tamedPos.getX() + 0.5, tamedPos.getY(), tamedPos.getZ() + 0.5, 0.0F, 0.0F);
      level.addFreshEntity(tamed);
      ServerPlayer player = placeSurvivalPlayer(helper, 3, 2, 4);
      tamed.tame(player);
      tamed.setInvulnerable(true);
      helper.runAtTickTime(40L, () -> {
         BlockPos base = helper.absolutePos(new BlockPos(3, 2, 4));
         for (ServerPlayer p : level.players()) {
            p.moveTo(base.getX() + 140.0, base.getY(), base.getZ(), 0.0F, 0.0F);
         }
      });
      helper.runAtTickTime(300L, () -> {
         helper.assertTrue(
            level.getNearestPlayer(wildPos.getX() + 0.5, wildPos.getY(), wildPos.getZ() + 0.5, 130.0, false) == null,
            "a player remained within 130 blocks of the wild shark; despawn precondition not met");
         helper.assertTrue(!wild.isPersistenceRequired(), "spawned wild shark unexpectedly persistent");
         helper.assertTrue(wild.isRemoved(),
            "wild bull shark was not removed at 140 blocks (>128 WATER_CREATURE despawn range)");
         helper.assertTrue(!tamed.isRemoved() && tamed.isAlive(),
            "tamed nurse shark was removed at 140 blocks");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configSpeedMultiplierApplied(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      double prev = DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.get();
      try {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(1.0);
         BullSharkEntity control = spawnedShark(level, base);
         double baseSpeed = control.getAttributeValue(Attributes.MOVEMENT_SPEED);
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(2.0);
         BullSharkEntity scaled = spawnedShark(level, base);
         double scaledSpeed = scaled.getAttributeValue(Attributes.MOVEMENT_SPEED);
         helper.assertTrue(Math.abs(scaledSpeed - baseSpeed * 2.0) < 1.0E-4,
            "MOVEMENT_SPEED " + scaledSpeed + " != 2x base " + baseSpeed + " at sharkSpeedMultiplier=2.0");
         helper.succeed();
      } finally {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(prev);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configSpeciesSpeedOverrideWins(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      double prevGlobal = DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.get();
      ModConfigSpec.DoubleValue bullOverride = DwurdySharksConfig.speciesSpeedMultiplierValue("bull_shark");
      helper.assertTrue(bullOverride != null, "movement.speciesMultiplier.bull_shark missing from config");
      double prevBull = bullOverride.get();
      try {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(1.0);
         BullSharkEntity control = spawnedShark(level, base);
         double baseSpeed = control.getAttributeValue(Attributes.MOVEMENT_SPEED);
         bullOverride.set(0.5);
         BullSharkEntity halved = spawnedShark(level, base);
         double halvedSpeed = halved.getAttributeValue(Attributes.MOVEMENT_SPEED);
         helper.assertTrue(Math.abs(halvedSpeed - baseSpeed * 0.5) < 1.0E-4,
            "bull_shark MOVEMENT_SPEED " + halvedSpeed + " != 0.5x base " + baseSpeed + " at speciesMultiplier=0.5");
         NurseSharkEntity nurse = DwurdySharksModEntities.NURSE_SHARK.get().create(level);
         nurse.moveTo(base.getX() + 2.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
         nurse.setPersistenceRequired();
         level.addFreshEntity(nurse);
         double nurseSpeed = nurse.getAttributeValue(Attributes.MOVEMENT_SPEED);
         helper.assertTrue(bullOverride.get() == 0.5 && Math.abs(nurseSpeed - nurse.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue()) < 1.0E-4,
            "nurse_shark speed " + nurseSpeed + " changed although only bull_shark override was set");
         helper.succeed();
      } finally {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(prevGlobal);
         bullOverride.set(prevBull);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configSpeciesHealthOverride(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      ModConfigSpec.DoubleValue bullHealth = DwurdySharksConfig.speciesHealthOverrideValue("bull_shark");
      helper.assertTrue(bullHealth != null, "damage.speciesHealth.bull_shark missing from config");
      double prev = bullHealth.get();
      try {
         bullHealth.set(40.0);
         BullSharkEntity scaled = spawnedShark(level, base);
         double maxHealth = scaled.getAttributeValue(Attributes.MAX_HEALTH);
         helper.assertTrue(Math.abs(maxHealth - 40.0) < 1.0E-4,
            "bull_shark MAX_HEALTH " + maxHealth + " != configured override 40.0");
         helper.assertTrue(scaled.getHealth() <= scaled.getMaxHealth(),
            "health " + scaled.getHealth() + " exceeds overridden max " + scaled.getMaxHealth());
         helper.succeed();
      } finally {
         bullHealth.set(prev);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configSpeciesDamageOverrideScalesHits(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      ModConfigSpec.DoubleValue bullDamage = DwurdySharksConfig.speciesDamageMultiplierValue("bull_shark");
      helper.assertTrue(bullDamage != null, "damage.speciesMultiplier.bull_shark missing from config");
      double prevSpecies = bullDamage.get();
      double prevGlobal = DwurdySharksConfig.SHARK_DAMAGE_MULTIPLIER.get();
      try {
         DwurdySharksConfig.SHARK_DAMAGE_MULTIPLIER.set(1.0);
         bullDamage.set(1.0);
         BullSharkEntity shark = spawnedShark(level, base);
         Pig pigA = EntityType.PIG.create(level);
         pigA.moveTo(base.getX() + 1.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
         pigA.setPersistenceRequired();
         level.addFreshEntity(pigA);
         float healthA = pigA.getHealth();
         pigA.hurt(level.damageSources().mobAttack(shark), 4.0F);
         float deltaA = healthA - pigA.getHealth();
         helper.assertTrue(deltaA > 0.0F, "control pig took no damage from shark mobAttack");

         bullDamage.set(0.5);
         Pig pigB = EntityType.PIG.create(level);
         pigB.moveTo(base.getX() + 2.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
         pigB.setPersistenceRequired();
         level.addFreshEntity(pigB);
         float healthB = pigB.getHealth();
         pigB.hurt(level.damageSources().mobAttack(shark), 4.0F);
         float deltaB = healthB - pigB.getHealth();
         helper.assertTrue(Math.abs(deltaB - deltaA * 0.5F) < 0.01F,
            "damage at speciesMultiplier=0.5 (" + deltaB + ") != half of " + deltaA);
         helper.succeed();
      } finally {
         bullDamage.set(prevSpecies);
         DwurdySharksConfig.SHARK_DAMAGE_MULTIPLIER.set(prevGlobal);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configModifiersIdempotentOnRejoin(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      double prev = DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.get();
      try {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(2.0);
         BullSharkEntity shark = spawnedShark(level, base);
         double firstJoin = shark.getAttributeValue(Attributes.MOVEMENT_SPEED);
         NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(shark, level));
         NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(shark, level));
         double afterRepost = shark.getAttributeValue(Attributes.MOVEMENT_SPEED);
         helper.assertTrue(Math.abs(afterRepost - firstJoin) < 1.0E-4,
            "MOVEMENT_SPEED stacked on repeated join: " + firstJoin + " -> " + afterRepost);
         helper.succeed();
      } finally {
         DwurdySharksConfig.SHARK_SPEED_MULTIPLIER.set(prev);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 300)
   public static void configItemEatingToggle(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      fillPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      boolean prev = DwurdySharksConfig.ITEM_EATING_ENABLED.get();
      helper.runAtTickTime(190L, () -> DwurdySharksConfig.ITEM_EATING_ENABLED.set(prev));
      DwurdySharksConfig.ITEM_EATING_ENABLED.set(false);
      BullSharkEntity shark = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      shark.moveTo(base.getX() + 0.5, base.getY(), base.getZ() + 0.5, 0.0F, 0.0F);
      shark.setPersistenceRequired();
      shark.setNoAi(true);
      shark.setInvulnerable(true);
      level.addFreshEntity(shark);
      net.minecraft.world.entity.item.ItemEntity apple = new net.minecraft.world.entity.item.ItemEntity(
         level, base.getX() + 0.5, base.getY(), base.getZ() + 0.5,
         new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.APPLE));
      apple.setNoPickUpDelay();
      level.addFreshEntity(apple);
      helper.runAtTickTime(80L, () -> {
         helper.assertTrue(apple.isAlive(),
            "dropped apple vanished while itemEatingEnabled=false");
         DwurdySharksConfig.ITEM_EATING_ENABLED.set(true);
         helper.runAtTickTime(180L, () -> {
            helper.assertTrue(!apple.isAlive(),
               "dropped apple survived 100 ticks next to an eater shark with itemEatingEnabled=true");
            helper.succeed();
         });
      });
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 200)
   public static void configAggroRangeMultiplierApplied(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      purgeModMobs(helper);
      double prev = DwurdySharksConfig.AGGRO_FOLLOW_RANGE_MULTIPLIER.get();
      try {
         DwurdySharksConfig.AGGRO_FOLLOW_RANGE_MULTIPLIER.set(1.0);
         BullSharkEntity control = spawnedShark(level, base);
         double baseRange = control.getAttributeValue(Attributes.FOLLOW_RANGE);
         DwurdySharksConfig.AGGRO_FOLLOW_RANGE_MULTIPLIER.set(2.0);
         BullSharkEntity scaled = spawnedShark(level, base);
         double scaledRange = scaled.getAttributeValue(Attributes.FOLLOW_RANGE);
         helper.assertTrue(Math.abs(scaledRange - baseRange * 2.0) < 1.0E-4,
            "FOLLOW_RANGE " + scaledRange + " != 2x base " + baseRange + " at aggroFollowRangeMultiplier=2.0");
         helper.succeed();
      } finally {
         DwurdySharksConfig.AGGRO_FOLLOW_RANGE_MULTIPLIER.set(prev);
      }
   }

   private static BullSharkEntity spawnedShark(ServerLevel level, BlockPos pos) {
      BullSharkEntity shark = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      shark.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      shark.setPersistenceRequired();
      level.addFreshEntity(shark);
      return shark;
   }

   @GameTest(template = "pool", batch = "global_cap", timeoutTicks = 400)
   public static void globalCapBoundsWholeDimension(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      GameRules rules = level.getGameRules();
      int prevGlobal = rules.getInt(DwurdySharksModGameRules.AMBIENT_FISH_GLOBAL_CAP);
      int prevLocal = rules.getInt(DwurdySharksModGameRules.AMBIENT_FISH_LOCAL_CAP);
      boolean prevManual = rules.getBoolean(DwurdySharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS);
      int prevConfig = DwurdySharksConfig.AMBIENT_FISH_GLOBAL_CAP.get();
      rules.getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BlockPos base = helper.absolutePos(new BlockPos(2, 2, 2));
      try {
         rules.getRule(DwurdySharksModGameRules.AMBIENT_FISH_LOCAL_CAP).set(0, level.getServer());
         int baselineFish = countTaggedEntities(level, DwurdySharksEntityTypeTags.AMBIENT_FISH);
         rules.getRule(DwurdySharksModGameRules.AMBIENT_FISH_GLOBAL_CAP).set(baselineFish + 6, level.getServer());
         int admitted = 0;
         for (int i = 0; i < 10; i++) {
            if (DwurdySharksModEntities.KRILL.get().spawn(level, base, MobSpawnType.NATURAL) != null) {
               admitted++;
            }
         }
         helper.assertTrue(admitted == 6,
            "expected plateau at ambientFishGlobalCap=" + (baselineFish + 6) + " (baseline " + baselineFish
               + ") with local cap disabled, but " + admitted + " of 10 were admitted");
         rules.getRule(DwurdySharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS).set(true, level.getServer());
         helper.assertTrue(DwurdySharksModEntities.KRILL.get().spawn(level, base, MobSpawnType.COMMAND) == null,
            "manual /summon spawn admitted past the global cap while enforceCapForManualSpawns=true");
         rules.getRule(DwurdySharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS).set(false, level.getServer());
         helper.assertTrue(DwurdySharksModEntities.KRILL.get().spawn(level, base, MobSpawnType.COMMAND) != null,
            "manual /summon spawn refused at global cap while enforceCapForManualSpawns=false");
         rules.getRule(DwurdySharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS).set(true, level.getServer());
         int baselineFish2 = countTaggedEntities(level, DwurdySharksEntityTypeTags.AMBIENT_FISH);
         rules.getRule(DwurdySharksModGameRules.AMBIENT_FISH_GLOBAL_CAP).set(-1, level.getServer());
         DwurdySharksConfig.AMBIENT_FISH_GLOBAL_CAP.set(baselineFish2 + 3);
         int configAdmitted = 0;
         for (int i = 0; i < 6; i++) {
            if (DwurdySharksModEntities.KRILL.get().spawn(level, base, MobSpawnType.NATURAL) != null) {
               configAdmitted++;
            }
         }
         helper.assertTrue(configAdmitted == 3,
            "gamerule -1 should inherit config cap " + (baselineFish2 + 3) + ", but " + configAdmitted
               + " of 6 were admitted");
         helper.succeed();
      } finally {
         rules.getRule(DwurdySharksModGameRules.AMBIENT_FISH_GLOBAL_CAP).set(prevGlobal, level.getServer());
         rules.getRule(DwurdySharksModGameRules.AMBIENT_FISH_LOCAL_CAP).set(prevLocal, level.getServer());
         rules.getRule(DwurdySharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS).set(prevManual, level.getServer());
         DwurdySharksConfig.AMBIENT_FISH_GLOBAL_CAP.set(prevConfig);
      }
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 2000)
   public static void configDryoutTimings(GameTestHelper helper) {
      exclusive(helper, 150, base -> configDryoutTimingsImpl(helper, base));
   }

   private static void configDryoutTimingsImpl(GameTestHelper helper, long base) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      int prevDelay = DwurdySharksConfig.DRYOUT_DELAY_TICKS.get();
      int prevDuration = DwurdySharksConfig.DRYOUT_DURATION_TICKS.get();
      BlockPos pos = helper.absolutePos(new BlockPos(2, 1, 2));
      DwurdySharksConfig.DRYOUT_DELAY_TICKS.set(20);
      DwurdySharksConfig.DRYOUT_DURATION_TICKS.set(100);
      BullSharkEntity shark = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      shark.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      shark.setPersistenceRequired();
      shark.setInvulnerable(true);
      level.addFreshEntity(shark);
      helper.runAtTickTime(base + 130L, () -> {
         DwurdySharksConfig.DRYOUT_DELAY_TICKS.set(prevDelay);
         DwurdySharksConfig.DRYOUT_DURATION_TICKS.set(prevDuration);
      });
      helper.runAtTickTime(base + 60L, guarded(() -> {
         helper.assertTrue(shark.hasEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT),
            "beached shark lacked dryout effect 40 ticks after configured 20-tick delay");
         shark.discard();
         DwurdySharksConfig.DRYOUT_DELAY_TICKS.set(0);
         BullSharkEntity disabled = DwurdySharksModEntities.BULL_SHARK.get().create(level);
         disabled.moveTo(pos.getX() + 2.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
         disabled.setPersistenceRequired();
         disabled.setInvulnerable(true);
         level.addFreshEntity(disabled);
         helper.runAtTickTime(base + 120L, guardedEnd(() -> {
            helper.assertTrue(!disabled.hasEffect(DwurdySharksModMobEffects.DRYOUT_EFFECT),
               "dryoutDelayTicks=0 still applied the dryout effect");
            helper.succeed();
         }));
      }));
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 400)
   public static void configDespawnDistance(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(DwurdySharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      int prevDistance = DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.get();
      DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.set(64);
      BlockPos wildPos = helper.absolutePos(new BlockPos(2, 2, 2));
      BullSharkEntity wild = DwurdySharksModEntities.BULL_SHARK.get().create(level);
      wild.moveTo(wildPos.getX() + 0.5, wildPos.getY(), wildPos.getZ() + 0.5, 0.0F, 0.0F);
      level.addFreshEntity(wild);
      BlockPos tamedPos = helper.absolutePos(new BlockPos(4, 2, 2));
      NurseSharkEntity tamed = DwurdySharksModEntities.NURSE_SHARK.get().create(level);
      tamed.moveTo(tamedPos.getX() + 0.5, tamedPos.getY(), tamedPos.getZ() + 0.5, 0.0F, 0.0F);
      level.addFreshEntity(tamed);
      ServerPlayer player = placeSurvivalPlayer(helper, 3, 2, 4);
      tamed.tame(player);
      tamed.setInvulnerable(true);
      helper.runAtTickTime(20L, () -> {
         for (ServerPlayer p : level.players()) {
            p.moveTo(wildPos.getX() + 100.0, wildPos.getY(), wildPos.getZ(), 0.0F, 0.0F);
         }
      });
      helper.runAtTickTime(170L, () -> {
         DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.set(prevDistance);
      });
      helper.runAtTickTime(160L, () -> {
         helper.assertTrue(
            level.getNearestPlayer(wildPos.getX() + 0.5, wildPos.getY(), wildPos.getZ() + 0.5, 90.0, false) == null,
            "a player remained within 90 blocks of the wild shark; configured despawn precondition not met");
         helper.assertTrue(wild.isRemoved(),
            "wild bull shark was not discarded at 100 blocks with hardDespawnDistanceBlocks=64");
         helper.assertTrue(!tamed.isRemoved() && tamed.isAlive(),
            "tamed nurse shark was discarded despite the exemption");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "config", timeoutTicks = 400)
   public static void configDespawnNoPlayers(GameTestHelper helper) {
      ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
      helper.assertTrue(nether != null, "nether dimension unavailable");
      int prevDistance = DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.get();
      try {
         DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.set(64);
         BullSharkEntity wild = DwurdySharksModEntities.BULL_SHARK.get().create(nether);
         wild.moveTo(0.5, 70.0, 0.5, 0.0F, 0.0F);
         wild.setInvulnerable(true);
         nether.addFreshEntity(wild);
         helper.assertTrue(nether.players().isEmpty(), "a player is in the nether; no-player precondition not met");
         for (int i = 0; i < 5; i++) {
            net.mcreator.sharks.procedures.SharkDespawnProcedure.checkHardDespawn(wild);
         }
         helper.assertTrue(!wild.isRemoved() && wild.isAlive(),
            "wild bull shark was discarded with no players online; hard despawn must require a player in the dimension");
         wild.discard();
         helper.succeed();
      } finally {
         DwurdySharksConfig.HARD_DESPAWN_DISTANCE_BLOCKS.set(prevDistance);
      }
   }

   private static void scheduleEvery(GameTestHelper helper, long intervalTicks, Runnable task) {
      task.run();
      scheduleNext(helper, intervalTicks, task);
   }

   private static void scheduleNext(GameTestHelper helper, long intervalTicks, Runnable task) {
      helper.runAfterDelay(intervalTicks, () -> {
         task.run();
         scheduleNext(helper, intervalTicks, task);
      });
   }

   private static double tickAvgMs(ServerLevel level) {
      return level.getServer().getAverageTickTimeNanos() / 1_000_000.0;
   }

   private static double tickP95Ms(ServerLevel level) {
      long[] sorted = java.util.Arrays.stream(level.getServer().getTickTimesNanos()).filter(v -> v > 0).sorted().toArray();
      if (sorted.length == 0) {
         return 0.0;
      }
      return sorted[Math.min(sorted.length - 1, (int) Math.ceil(sorted.length * 0.95) - 1)] / 1_000_000.0;
   }

   private static String fmt(double value) {
      return String.format(Locale.ROOT, "%.2f", value);
   }

   private static int countModEntities(ServerLevel level, AABB box) {
      return level.getEntitiesOfClass(Mob.class, box,
         e -> DwurdySharksConfig.isModEntity(e.getType())).size();
   }

   private static int countLargeSharks(ServerLevel level, AABB box) {
      return level.getEntitiesOfClass(Mob.class, box,
         e -> e.isAlive() && e.getType().is(DwurdySharksEntityTypeTags.LARGE_SHARKS)).size();
   }

   private static int purgeModMobs(GameTestHelper helper) {
      List<Mob> strays = helper.getLevel().getEntitiesOfClass(Mob.class, helper.getBounds().inflate(4.0),
         e -> DwurdySharksConfig.isModEntity(e.getType()));
      strays.forEach(Entity::discard);
      return strays.size();
   }

   private static int countTaggedEntities(ServerLevel level, TagKey<EntityType<?>> tag) {
      int count = 0;
      for (Entity e : level.getEntities().getAll()) {
         if (e.getType().is(tag) && !isCapExempt(e)) {
            count++;
         }
      }
      return count;
   }

   private static boolean isCapExempt(Entity e) {
      if (e instanceof net.minecraft.world.entity.TamableAnimal tamable && tamable.isTame()) {
         return true;
      }
      return e.hasCustomName() || (e instanceof Mob mob && mob.isPersistenceRequired());
   }

   private static void fillTestBiome(GameTestHelper helper, ResourceKey<Biome> key) {
      ServerLevel level = helper.getLevel();
      Holder.Reference<Biome> biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(key);
      // Biome reads jitter +-1 quart (~4 blocks) around the query position, so the fill
      // must extend past the test volume; fill() rejects unloaded chunks, so pull the
      // expanded region into memory first.
      AABB bounds = helper.getBounds().inflate(8.0D);
      BlockPos min = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
      BlockPos max = BlockPos.containing(bounds.maxX - 1.0D, bounds.maxY - 1.0D, bounds.maxZ - 1.0D);
      for (int cx = min.getX() >> 4; cx <= max.getX() >> 4; cx++) {
         for (int cz = min.getZ() >> 4; cz <= max.getZ() >> 4; cz++) {
            level.getChunk(cx, cz);
         }
      }
      Either<Integer, CommandSyntaxException> result = FillBiomeCommand.fill(level, min, max, biome);
      helper.assertTrue(result.left().isPresent(),
         "biome fill failed for " + key.location() + ": " + result.right().map(CommandSyntaxException::getMessage).orElse("no error"));
   }

   private static void fillPool(GameTestHelper helper) {
      for (int x = 0; x <= 4; x++) {
         for (int z = 0; z <= 4; z++) {
            for (int y = 1; y <= 2; y++) {
               helper.setBlock(new BlockPos(x, y, z), Blocks.WATER.defaultBlockState());
            }
         }
      }
   }

   private static void fillUpperPool(GameTestHelper helper) {
      for (int x = 0; x <= 4; x++) {
         for (int y = 3; y <= 4; y++) {
            for (int z = 0; z <= 4; z++) {
               helper.setBlock(new BlockPos(x, y, z), Blocks.WATER.defaultBlockState());
            }
         }
      }
   }

   private static ServerPlayer placeSurvivalPlayer(GameTestHelper helper, int x, int y, int z) {
      ServerLevel level = helper.getLevel();
      CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "gametest-player"), false);
      ServerPlayer player = new ServerPlayer(level.getServer(), level, cookie.gameProfile(), cookie.clientInformation()) {
         @Override
         public boolean isSpectator() {
            return false;
         }

         @Override
         public boolean isCreative() {
            return false;
         }
      };
      Connection connection = new Connection(PacketFlow.SERVERBOUND);
      new EmbeddedChannel(connection);
      player.connection = new ServerGamePacketListenerImpl(level.getServer(), connection, player, cookie);
      level.addNewPlayer(player);
      BlockPos pos = helper.absolutePos(new BlockPos(x, y, z));
      player.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      return player;
   }
}
