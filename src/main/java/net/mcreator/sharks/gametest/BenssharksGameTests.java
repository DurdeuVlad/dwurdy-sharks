package net.mcreator.sharks.gametest;

import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.UUID;
import net.mcreator.sharks.BenssharksMod;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.mcreator.sharks.init.BenssharksModEntities;
import net.mcreator.sharks.init.BenssharksModGameRules;
import net.mcreator.sharks.procedures.FollowIfTamedProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;

@GameTestHolder(BenssharksMod.MODID)
public class BenssharksGameTests {
   @GameTest(template = "pool", batch = "aggressive_off", timeoutTicks = 300)
   public static void sharkIgnoresPlayerWhenRuleOff(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(BenssharksModGameRules.AGGRESSIVE_SHARKS).set(false, level.getServer());
      fillPool(helper);
      BullSharkEntity shark = helper.spawn(BenssharksModEntities.BULL_SHARK.get(), 3, 2, 3);
      placeSurvivalPlayer(helper, 3, 2, 4);
      helper.runAtTickTime(200L, () -> {
         helper.assertTrue(shark.getTarget() == null, "bull shark targeted a player with aggressiveSharks=false");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "aggressive_on", timeoutTicks = 300)
   public static void sharkTargetsPlayerWhenRuleOn(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(BenssharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      BullSharkEntity shark = helper.spawn(BenssharksModEntities.BULL_SHARK.get(), 3, 2, 3);
      ServerPlayer player = placeSurvivalPlayer(helper, 3, 2, 4);
      helper.succeedWhen(() -> helper.assertTrue(shark.getTarget() == player, "bull shark did not target a player with aggressiveSharks=true"));
   }

   @GameTest(template = "pool", batch = "aggressive_whaleshark", timeoutTicks = 300)
   public static void filterFeederIgnoresPlayerWhenRuleOn(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(BenssharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      WhaleSharkEntity shark = helper.spawn(BenssharksModEntities.WHALE_SHARK.get(), 3, 2, 3);
      placeSurvivalPlayer(helper, 3, 2, 4);
      helper.runAtTickTime(200L, () -> {
         helper.assertTrue(shark.getTarget() == null, "whale shark targeted a player with aggressiveSharks=true");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "aggressive_tamed", timeoutTicks = 300)
   public static void tamedSharkDoesNotTargetOwner(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(BenssharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      NurseSharkEntity shark = helper.spawn(BenssharksModEntities.NURSE_SHARK.get(), 3, 2, 3);
      ServerPlayer owner = placeSurvivalPlayer(helper, 3, 2, 4);
      shark.tame(owner);
      helper.runAtTickTime(200L, () -> {
         helper.assertTrue(shark.getTarget() == null, "tamed nurse shark targeted its owner with aggressiveSharks=true");
         helper.succeed();
      });
   }

   @GameTest(template = "pool", batch = "tamed_follow", timeoutTicks = 60)
   public static void tamedSharkDoesNotPathToOwnerOnLand(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      fillPool(helper);
      NurseSharkEntity shark = helper.spawn(BenssharksModEntities.NURSE_SHARK.get(), 2, 2, 2);
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

   @GameTest(template = "pool", batch = "stress_population", timeoutTicks = 1500, required = false)
   public static void sharkPopulationTickCost(GameTestHelper helper) {
      ServerLevel level = helper.getLevel();
      level.getServer().setDifficulty(Difficulty.NORMAL, true);
      level.getGameRules().getRule(BenssharksModGameRules.AGGRESSIVE_SHARKS).set(true, level.getServer());
      fillPool(helper);
      net.minecraft.world.entity.EntityType<?>[] types = new net.minecraft.world.entity.EntityType[]{
         BenssharksModEntities.BULL_SHARK.get(), BenssharksModEntities.TIGER_SHARK.get(),
         BenssharksModEntities.MAKO_SHARK.get(), BenssharksModEntities.LEMON_SHARK.get(),
         BenssharksModEntities.BLUE_SHARK.get(), BenssharksModEntities.BASKING_SHARK.get(),
         BenssharksModEntities.WHALE_SHARK.get(), BenssharksModEntities.BLACKTIP_REEF_SHARK.get(),
         BenssharksModEntities.BONNETHEAD_SHARK.get(), BenssharksModEntities.BARRACUDA.get()
      };
      int spawned = 0;
      for (int i = 0; i < 100; i++) {
         try {
            helper.spawn(types[i % types.length], 1 + i % 4, 1 + i / 50, 1 + i / 4 % 4);
            spawned++;
         } catch (Throwable ignored) {
         }
      }
      placeSurvivalPlayer(helper, 3, 2, 4);
      int total = spawned;
      helper.runAtTickTime(1200L, () -> {
         float mspt = level.getServer().getAverageTickTimeNanos() / 1000000.0F;
         long alive = level.getEntitiesOfClass(
            net.minecraft.world.entity.Mob.class,
            net.minecraft.world.phys.AABB.ofSize(helper.absolutePos(new BlockPos(2, 1, 2)).getCenter(), 40.0, 40.0, 40.0),
            e -> e.getType().is(net.mcreator.sharks.init.DwurdySharksEntityTypeTags.SHARKS)).size();
         BenssharksMod.LOGGER.info(
            "[stress_population] {} sharks spawned, {} alive, server avg tick time {} ms (last 100 ticks, aggressiveSharks=true, survival player present)",
            total, alive, String.format(java.util.Locale.ROOT, "%.2f", mspt));
         helper.succeed();
      });
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
