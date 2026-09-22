# Dwurdy Sharks — CurseForge Description

## Summary

**All the sharks. Maintained.** A credited community fork of Ben's Sharks for NeoForge 1.21.1, with 26 registered marine entity types and the wild-shark despawn fix.

## Description

🌊 **Dive back into the deep!** Dwurdy Sharks keeps the ocean packed with dangerous, strange, and adorable marine life from **Ben's Sharks**, while giving the project a community-maintained home.

> **Attribution:** Dwurdy Sharks is an independent maintenance fork of **[Ben's Sharks](https://github.com/PaoloBen/BensSharks)** by **benndevs / PaoloBen**. This fork is not the original project, and no endorsement or sponsorship by the original author is implied. Please support and credit the original work on [Modrinth](https://modrinth.com/mod/bens-sharks) and [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bens-sharks).

### What you get

- 🦈 **26 registered marine entity types** — 23 living/hostile marine mobs plus projectile and particle helpers — including Great White, Megalodon, Basking Shark, Axodile, Barracuda, Krill, Land Shark, and more
- 🦷 Shark-tooth items and weapons
- 🛡️ Jagged armor
- 🧸 Shark Plush for your base
- 🍲 Food and cooking items made from the mod's marine life
- ✅ A fix for the upstream 1.21.x persistent-shark bug tracked in [issue #7](https://github.com/PaoloBen/BensSharks/issues/7): wild sharks despawn normally, while tamed sharks still persist

### New in 1.4.0

- � **Wild sharks stay in the ocean.** A fix for sharks spawning in rivers, lakes, swamps, and beaches — natural spawns are now restricted to the `dwurdysharks:shark_spawning_oceans` biome tag, extensible by datapacks.
- 🚦 **Server protection built in.** Local population caps (`largeSharkLocalCap` 24, `ambientFishLocalCap` 64, `sharkSpawnCapRadius` 128) with density shaping stop entity floods before they start. Tamed and named sharks are exempt.
- ⚡ **Major tick-time fixes.** Beached sharks no longer flood the server work queue, spawn procedures no longer scan every tick, and all 24 species dropped per-tick dimension refreshes — the 5-minute automated stress gate holds p95 tick under 15 ms at ~470 live sharks.
- ⚙️ **New server config** (`dwurdysharks-server.toml`): master spawn switch, per-species toggles, damage multiplier, and Megalodon damage/armor-bypass options.
- 🧪 **Automated regression + stress suite** (`gradlew runGameTestServer`): 17 tests including five 5-minute wall-clock stress profiles.

<details><summary>Earlier highlights</summary>

**1.3.0** — `aggressiveSharks` gamerule (opt-in player hunting), in-game effect descriptions, Simplified Chinese localization, shader-friendly cutout rendering, tamed sharks no longer beach themselves while following.

</details>

### Requirements

- Minecraft **1.21.1**
- **[NeoForge 21.1.x](https://neoforged.net/)**
- **[GeckoLib 4.x](https://www.curseforge.com/minecraft/mc-mods/geckolib)** — required dependency
- **Java 21**

Only the NeoForge 1.21.1 target is currently verified. Do not mix this file's instructions with a different Minecraft version or loader.

### Server configuration

`dwurdysharks-server.toml` (per world, in `<world>/serverconfig/`) controls damage and speed multipliers (global + per-species), per-species health overrides, whole-dimension population caps, dryout timing, item eating, and despawn distance. Per-world live knobs stay as gamerules (`aggressiveSharks`, local/global caps — a gamerule of `-1` inherits the config default). Full reference: `docs/CONFIG.md` and `docs/GAMERULES.md` in the repo.

### Installation

1. Install Minecraft 1.21.1 with NeoForge 21.1.x.
2. Install [GeckoLib 4.x](https://www.curseforge.com/minecraft/mc-mods/geckolib) for the same Minecraft version.
3. Download the Dwurdy Sharks jar for Minecraft 1.21.1 and place it in your `mods` folder.
4. Launch the game and explore the oceans.

### ⚠️ Migrating from Ben's Sharks — important in 1.3.0

Dwurdy Sharks 1.3.0 registers under its own **`dwurdysharks`** mod ID instead of `benssharks`. Worlds saved by Ben's Sharks or Dwurdy Sharks ≤1.2.8 contain `benssharks:*` entity, item, block, effect, and sound identifiers that 1.3.0 does not resolve — those modded objects will be **missing** if you load an old world under 1.3.0.

1. **Back up your world first.** There is no automatic data migration in this release.
2. Remove the Ben's Sharks (or Dwurdy Sharks ≤1.2.x) jar and add the new jar. **Never load both jars together.**
3. To keep an existing world fully intact, stay on a `benssharks`-ID build — Dwurdy Sharks 1.2.7 on [GitHub Releases](https://github.com/DurdeuVlad/dwurdy-sharks/releases), or upstream Ben's Sharks — or start a fresh world on 1.3.0.

### Links

- Source: https://github.com/DurdeuVlad/dwurdy-sharks
- Releases & changelogs: https://github.com/DurdeuVlad/dwurdy-sharks/releases
- Original Ben's Sharks source: https://github.com/PaoloBen/BensSharks
- Original Modrinth page: https://modrinth.com/mod/bens-sharks
- Original CurseForge page: https://www.curseforge.com/minecraft/mc-mods/bens-sharks

### License

LGPL-3.0, the same license used by the upstream project.

— The Dwurdy Sharks Team
