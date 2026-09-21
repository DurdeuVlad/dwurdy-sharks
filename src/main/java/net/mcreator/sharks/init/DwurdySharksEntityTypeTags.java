package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class DwurdySharksEntityTypeTags {
   public static final TagKey<EntityType<?>> LARGE_SHARKS = TagKey.create(
      Registries.ENTITY_TYPE,
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "large_sharks")
   );
   public static final TagKey<EntityType<?>> AMBIENT_FISH = TagKey.create(
      Registries.ENTITY_TYPE,
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "ambient_fish")
   );

   private DwurdySharksEntityTypeTags() {
   }
}
