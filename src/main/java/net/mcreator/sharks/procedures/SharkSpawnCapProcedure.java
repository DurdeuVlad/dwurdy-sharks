package net.mcreator.sharks.procedures;

import net.mcreator.sharks.init.BenssharksModGameRules;
import net.mcreator.sharks.init.DwurdySharksConfig;
import net.mcreator.sharks.init.DwurdySharksEntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

@EventBusSubscriber
public final class SharkSpawnCapProcedure {
   private SharkSpawnCapProcedure() {
   }

   @SubscribeEvent
   public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
      Mob mob = event.getEntity();
      EntityType<?> type = mob.getType();
      MobSpawnType spawnType = event.getSpawnType();
      if (spawnType == MobSpawnType.BUCKET || spawnType == MobSpawnType.BREEDING) {
         return;
      }
      boolean manual = spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND || spawnType == MobSpawnType.DISPENSER;
      if (!manual && DwurdySharksConfig.isModEntity(type) && (!DwurdySharksConfig.SPAWNING_ENABLED.get() || !DwurdySharksConfig.speciesEnabled(type))) {
         event.setSpawnCancelled(true);
         return;
      }
      TagKey<EntityType<?>> groupTag;
      if (type.is(DwurdySharksEntityTypeTags.LARGE_SHARKS)) {
         groupTag = DwurdySharksEntityTypeTags.LARGE_SHARKS;
      } else if (type.is(DwurdySharksEntityTypeTags.AMBIENT_FISH)) {
         groupTag = DwurdySharksEntityTypeTags.AMBIENT_FISH;
      } else {
         return;
      }
      if (isExempt(mob)) {
         return;
      }
      net.minecraft.world.level.GameRules rules = event.getLevel().getLevel().getGameRules();
      if (manual && !rules.getBoolean(BenssharksModGameRules.ENFORCE_CAP_FOR_MANUAL_SPAWNS)) {
         return;
      }
      int cap = rules.getInt(groupTag == DwurdySharksEntityTypeTags.LARGE_SHARKS
         ? BenssharksModGameRules.LARGE_SHARK_LOCAL_CAP
         : BenssharksModGameRules.AMBIENT_FISH_LOCAL_CAP);
      int radius = rules.getInt(BenssharksModGameRules.SPAWN_CAP_RADIUS);
      if (cap <= 0 || radius <= 0) {
         return;
      }
      AABB area = mob.getBoundingBox().inflate(radius);
      long wildCount = event.getLevel().getLevel().getEntitiesOfClass(Entity.class, area,
         e -> e != mob && e.getType().is(groupTag) && !isExempt(e)).size();
      if (wildCount >= cap) {
         event.setSpawnCancelled(true);
      }
   }

   private static boolean isExempt(Entity entity) {
      if (entity instanceof TamableAnimal tamable && tamable.isTame()) {
         return true;
      }
      return entity.hasCustomName() || (entity instanceof Mob mob && mob.isPersistenceRequired());
   }
}
