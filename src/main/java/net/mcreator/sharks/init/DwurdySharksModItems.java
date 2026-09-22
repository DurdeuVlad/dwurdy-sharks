package net.mcreator.sharks.init;

import net.mcreator.sharks.block.display.LandMineDisplayItem;
import net.mcreator.sharks.block.display.LandMinePrimedDisplayItem;
import net.mcreator.sharks.block.display.SharkPlushieDisplayItem;
import net.mcreator.sharks.item.AxoleatherItem;
import net.mcreator.sharks.item.AxoscuteItem;
import net.mcreator.sharks.item.BarracudaBucketItem;
import net.mcreator.sharks.item.BlacktipReefSharkBucketItem;
import net.mcreator.sharks.item.BonnetheadBucketItem;
import net.mcreator.sharks.item.CellophaneNoodlesItem;
import net.mcreator.sharks.item.CookedBarracudaItem;
import net.mcreator.sharks.item.CookedPilotFishItem;
import net.mcreator.sharks.item.CookiecutterSharkBucketItem;
import net.mcreator.sharks.item.CookiecutterSharkLiveItem;
import net.mcreator.sharks.item.DentItem;
import net.mcreator.sharks.item.EggCapsuleItem;
import net.mcreator.sharks.item.FishBrothItem;
import net.mcreator.sharks.item.JaggedItem;
import net.mcreator.sharks.item.KrillBucketItem;
import net.mcreator.sharks.item.KrillCakeItem;
import net.mcreator.sharks.item.KrillItemItem;
import net.mcreator.sharks.item.KrillNoodlesItem;
import net.mcreator.sharks.item.MaelstromBowItem;
import net.mcreator.sharks.item.MaelstromItem;
import net.mcreator.sharks.item.MegalodonToothItem;
import net.mcreator.sharks.item.MutatedEggItem;
import net.mcreator.sharks.item.PilotFishBucketItem;
import net.mcreator.sharks.item.RawBarracudaItem;
import net.mcreator.sharks.item.RawPilotFishItem;
import net.mcreator.sharks.item.RemoraBucketItem;
import net.mcreator.sharks.item.SharkFinItem;
import net.mcreator.sharks.item.SharkFinSoupItem;
import net.mcreator.sharks.item.SharkMealItem;
import net.mcreator.sharks.item.SharkToothClubItem;
import net.mcreator.sharks.item.SharkToothItem;
import net.mcreator.sharks.item.SoupSharkfinItem;
import net.mcreator.sharks.item.SpetumItem;
import net.mcreator.sharks.item.StarchItem;
import net.mcreator.sharks.item.SuckerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public class DwurdySharksModItems {
   public static final Items REGISTRY = DeferredRegister.createItems("dwurdysharks");
   public static final DeferredItem<Item> GREATWHITESHARK_SPAWN_EGG = REGISTRY.register(
      "greatwhiteshark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.GREATWHITESHARK, -9667702, -3089945, new Properties())
   );
   public static final DeferredItem<Item> SHARK_FIN_SOUP = REGISTRY.register("shark_fin_soup", SharkFinSoupItem::new);
   public static final DeferredItem<Item> NURSE_SHARK_SPAWN_EGG = REGISTRY.register(
      "nurse_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.NURSE_SHARK, -7766419, -1580587, new Properties())
   );
   public static final DeferredItem<Item> FISH_BUCKET = REGISTRY.register("fish_bucket", SharkMealItem::new);
   public static final DeferredItem<Item> REMORA_SPAWN_EGG = REGISTRY.register(
      "remora_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.REMORA, -14210244, -7495501, new Properties())
   );
   public static final DeferredItem<Item> SHARK_TOOTH = REGISTRY.register("shark_tooth", SharkToothItem::new);
   public static final DeferredItem<Item> REMORA_BUCKET = REGISTRY.register("remora_bucket", RemoraBucketItem::new);
   public static final DeferredItem<Item> SUCKER = REGISTRY.register("sucker", SuckerItem::new);
   public static final DeferredItem<Item> LAND_MINE = REGISTRY.register(
      DwurdySharksModBlocks.LAND_MINE.getId().getPath(), () -> new LandMineDisplayItem((Block)DwurdySharksModBlocks.LAND_MINE.get(), new Properties())
   );
   public static final DeferredItem<Item> TIGER_SHARK_SPAWN_EGG = REGISTRY.register(
      "tiger_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.TIGER_SHARK, -9807546, -3292239, new Properties())
   );
   public static final DeferredItem<Item> DENT = REGISTRY.register("dent", DentItem::new);
   public static final DeferredItem<Item> FISH_BROTH = REGISTRY.register("fish_broth", FishBrothItem::new);
   public static final DeferredItem<Item> AXODILE_SPAWN_EGG = REGISTRY.register(
      "axodile_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.AXODILE, -12362095, -5395065, new Properties())
   );
   public static final DeferredItem<Item> AXOLEATHER = REGISTRY.register("axoleather", AxoleatherItem::new);
   public static final DeferredItem<Item> AXOSCUTE = REGISTRY.register("axoscute", AxoscuteItem::new);
   public static final DeferredItem<Item> JAGGED_HELMET = REGISTRY.register("jagged_helmet", JaggedItem.Helmet::new);
   public static final DeferredItem<Item> JAGGED_CHESTPLATE = REGISTRY.register("jagged_chestplate", JaggedItem.Chestplate::new);
   public static final DeferredItem<Item> JAGGED_LEGGINGS = REGISTRY.register("jagged_leggings", JaggedItem.Leggings::new);
   public static final DeferredItem<Item> JAGGED_BOOTS = REGISTRY.register("jagged_boots", JaggedItem.Boots::new);
   public static final DeferredItem<Item> STARCH = REGISTRY.register("starch", StarchItem::new);
   public static final DeferredItem<Item> CELLOPHANE_NOODLES = REGISTRY.register("cellophane_noodles", CellophaneNoodlesItem::new);
   public static final DeferredItem<Item> BLUE_SHARK_SPAWN_EGG = REGISTRY.register(
      "blue_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BLUE_SHARK, -10905911, -4203544, new Properties())
   );
   public static final DeferredItem<Item> BONNETHEAD_SHARK_SPAWN_EGG = REGISTRY.register(
      "bonnethead_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BONNETHEAD_SHARK, -5921371, -1, new Properties())
   );
   public static final DeferredItem<Item> MAKO_SHARK_SPAWN_EGG = REGISTRY.register(
      "mako_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.MAKO_SHARK, -10389086, -526598, new Properties())
   );
   public static final DeferredItem<Item> BONNETHEAD_SHARK_BUCKET = REGISTRY.register("bonnethead_shark_bucket", BonnetheadBucketItem::new);
   public static final DeferredItem<Item> COOKIECUTTER_SHARK_SPAWN_EGG = REGISTRY.register(
      "cookiecutter_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.COOKIECUTTER_SHARK, -10728903, -6322827, new Properties())
   );
   public static final DeferredItem<Item> COOKIECUTTER_SHARK_BUCKET = REGISTRY.register("cookiecutter_shark_bucket", CookiecutterSharkBucketItem::new);
   public static final DeferredItem<Item> BLACKTIP_REEF_SHARK_SPAWN_EGG = REGISTRY.register(
      "blacktip_reef_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BLACKTIP_REEF_SHARK, -7169893, -12828864, new Properties())
   );
   public static final DeferredItem<Item> BLACKTIP_REEF_SHARK_BUCKET = REGISTRY.register("blacktip_reef_shark_bucket", BlacktipReefSharkBucketItem::new);
   public static final DeferredItem<Item> COOKIECUTTER_SHARK_LIVE = REGISTRY.register("cookiecutter_shark_live", CookiecutterSharkLiveItem::new);
   public static final DeferredItem<Item> BASKING_SHARK_SPAWN_EGG = REGISTRY.register(
      "basking_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BASKING_SHARK, -9738139, -6579302, new Properties())
   );
   public static final DeferredItem<Item> BULL_SHARK_SPAWN_EGG = REGISTRY.register(
      "bull_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BULL_SHARK, -9076581, -2368549, new Properties())
   );
   public static final DeferredItem<Item> SHARK_PLUSH_BLOCK = REGISTRY.register(
      DwurdySharksModBlocks.SHARK_PLUSH_BLOCK.getId().getPath(),
      () -> new SharkPlushieDisplayItem((Block)DwurdySharksModBlocks.SHARK_PLUSH_BLOCK.get(), new Properties())
   );
   public static final DeferredItem<Item> MEGALODON_SPAWN_EGG = REGISTRY.register(
      "megalodon_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.MEGALODON, -11775140, -4473928, new Properties())
   );
   public static final DeferredItem<Item> LAND_SHARK_SPAWN_EGG = REGISTRY.register(
      "land_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.LAND_SHARK, -10915167, -6840401, new Properties())
   );
   public static final DeferredItem<Item> MUTATED_EGG = REGISTRY.register("mutated_egg", MutatedEggItem::new);
   public static final DeferredItem<Item> LEMON_SHARK_SPAWN_EGG = REGISTRY.register(
      "lemon_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.LEMON_SHARK, -4151200, -133677, new Properties())
   );
   public static final DeferredItem<Item> SHARK_FIN = REGISTRY.register("shark_fin", SharkFinItem::new);
   public static final DeferredItem<Item> THALASSOGER_SPAWN_EGG = REGISTRY.register(
      "thalassoger_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.THALASSOGER, -6972517, -12820585, new Properties())
   );
   public static final DeferredItem<Item> SOUP_SHARKFIN = REGISTRY.register("soup_sharkfin", SoupSharkfinItem::new);
   public static final DeferredItem<Item> KRILL_SPAWN_EGG = REGISTRY.register(
      "krill_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.KRILL, -2195122, -8827080, new Properties())
   );
   public static final DeferredItem<Item> KRILL_ITEM = REGISTRY.register("krill_item", KrillItemItem::new);
   public static final DeferredItem<Item> WHALE_SHARK_SPAWN_EGG = REGISTRY.register(
      "whale_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.WHALE_SHARK, -12762287, -5189411, new Properties())
   );
   public static final DeferredItem<Item> LAND_MINE_PRIMED = REGISTRY.register(
      DwurdySharksModBlocks.LAND_MINE_PRIMED.getId().getPath(),
      () -> new LandMinePrimedDisplayItem((Block)DwurdySharksModBlocks.LAND_MINE_PRIMED.get(), new Properties())
   );
   public static final DeferredItem<Item> MAELSTROM = REGISTRY.register("maelstrom", MaelstromItem::new);
   public static final DeferredItem<Item> MAELSTROM_BOW = REGISTRY.register("maelstrom_bow", MaelstromBowItem::new);
   public static final DeferredItem<Item> PILOT_FISH_SPAWN_EGG = REGISTRY.register(
      "pilot_fish_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.PILOT_FISH, -5322279, -13617608, new Properties())
   );
   public static final DeferredItem<Item> RAW_PILOT_FISH = REGISTRY.register("raw_pilot_fish", RawPilotFishItem::new);
   public static final DeferredItem<Item> PILOT_FISH_BUCKET = REGISTRY.register("pilot_fish_bucket", PilotFishBucketItem::new);
   public static final DeferredItem<Item> COOKED_PILOT_FISH = REGISTRY.register("cooked_pilot_fish", CookedPilotFishItem::new);
   public static final DeferredItem<Item> KRILL_CAKE = REGISTRY.register("krill_cake", KrillCakeItem::new);
   public static final DeferredItem<Item> KRILL_NOODLES = REGISTRY.register("krill_noodles", KrillNoodlesItem::new);
   public static final DeferredItem<Item> GREATER_AXODILE_SPAWN_EGG = REGISTRY.register(
      "greater_axodile_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.GREATER_AXODILE, -13408880, -2237997, new Properties())
   );
   public static final DeferredItem<Item> KRILL_BUCKET = REGISTRY.register("krill_bucket", KrillBucketItem::new);
   public static final DeferredItem<Item> EGG_CAPSULE = REGISTRY.register("egg_capsule", EggCapsuleItem::new);
   public static final DeferredItem<Item> GREENLAND_SHARK_SPAWN_EGG = REGISTRY.register(
      "greenland_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.GREENLAND_SHARK, -12105914, -9738139, new Properties())
   );
   public static final DeferredItem<Item> WHITETIP_SHARK_SPAWN_EGG = REGISTRY.register(
      "whitetip_shark_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.WHITETIP_SHARK, -9870239, -1579804, new Properties())
   );
   public static final DeferredItem<Item> MEGALODON_TOOTH = REGISTRY.register("megalodon_tooth", MegalodonToothItem::new);
   public static final DeferredItem<Item> SHARK_TOOTH_CLUB = REGISTRY.register("shark_tooth_club", SharkToothClubItem::new);
   public static final DeferredItem<Item> SPETUM = REGISTRY.register("spetum", SpetumItem::new);
   public static final DeferredItem<Item> BARRACUDA_SPAWN_EGG = REGISTRY.register(
      "barracuda_spawn_egg", () -> new DeferredSpawnEggItem(DwurdySharksModEntities.BARRACUDA, -4469285, -11962230, new Properties())
   );
   public static final DeferredItem<Item> RAW_BARRACUDA = REGISTRY.register("raw_barracuda", RawBarracudaItem::new);
   public static final DeferredItem<Item> BARRACUDA_BUCKET = REGISTRY.register("barracuda_bucket", BarracudaBucketItem::new);
   public static final DeferredItem<Item> COOKED_BARRACUDA = REGISTRY.register("cooked_barracuda", CookedBarracudaItem::new);
}
