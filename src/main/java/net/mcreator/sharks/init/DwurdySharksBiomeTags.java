package net.mcreator.sharks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class DwurdySharksBiomeTags {
   public static final TagKey<Biome> SHARK_SPAWNING_OCEANS = TagKey.create(
      Registries.BIOME,
      ResourceLocation.fromNamespaceAndPath("dwurdysharks", "shark_spawning_oceans")
   );

   private DwurdySharksBiomeTags() {
   }
}
