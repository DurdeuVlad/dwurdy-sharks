# Server configuration

File: `config/dwurdysharks-server.toml` (NeoForge server config, generated on first boot).
Edits apply on `/reload` or restart for new spawn checks and new damage applications.

## Boundary: config vs gamerules

- **Config** = global balance and master toggles (this file, per-installation).
- **Gamerules** = per-world runtime knobs (`/gamerule`): `aggressiveSharks`,
  `largeSharkLocalCap`, `ambientFishLocalCap`, `sharkSpawnCapRadius`,
  `enforceCapForManualSpawns`. Spawn weights/group sizes stay in biome-modifier
  JSON; override them per-modpack via datapack.

## Keys

### `[spawning]`

| Key | Default | Effect |
|---|---|---|
| `spawningEnabled` | `true` | `false` cancels **natural** spawns of every dwurdysharks species. Manual spawns (`/summon`, spawn egg, dispenser, bucket, breeding) still work — admin escape hatch. |
| `oceanOnly` | `true` | `true` restricts all 21 water spawn predicates to the `dwurdysharks:shark_spawning_oceans` biome tag. `false` restores pre-1.4.0 behavior (any water body). |
| `spawning.species.<id>Enabled` | `true` | Per-species natural-spawn toggle for all 24 registered species (`axodile` … `whitetip_shark`; excludes projectiles). |

### `[damage]`

| Key | Default | Effect |
|---|---|---|
| `sharkDamageMultiplier` | `1.0` | Multiplier applied to every shark hit (entities in `dwurdysharks:sharks`). Range 0.0–100.0. |
| `megalodonDamage` | `20.0` | Absolute base damage for Megalodon hits, replacing the attribute value, then multiplied by `sharkDamageMultiplier`. |
| `megalodonArmorBypass` | `false` | `true` routes Megalodon hits through `dwurdysharks:megalodon_bite` (in `minecraft:bypasses_armor`), ignoring armor. Off = vanilla-like armor reduction. |
