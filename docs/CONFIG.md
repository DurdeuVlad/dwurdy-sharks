# Server configuration

File: `<world>/serverconfig/dwurdysharks-server.toml` (NeoForge SERVER config —
per-world, generated on first world load). On dedicated servers the file lives
under the world directory; in singleplayer it is inside the save folder.

Edits apply on `/reload` or restart for spawn checks and damage. Attribute
changes (speed, health, follow range) apply to entities **when they join** —
already-loaded entities pick up changes on rejoin or relog.

## Which knob wins (precedence)

| Concern | Mechanism | Override |
|---|---|---|
| Natural spawning on/off, per-species | config `spawning.*` | none (master) |
| Local (per-radius) caps | gamerules | live per world |
| Global (whole-dimension) caps | config `population.*` | gamerule `-1` = inherit config, `>=0` overrides |
| Damage/speed/health balance | config | per-species table stacks on global |
| Dryout timing, item eating, despawn | config | none |
| Spawn weights / group sizes | datapack biome-modifier JSON | datapack wins |

One `dwurdySharksEnforceCapForManualSpawns` gamerule governs **both** local and global
caps for manual spawn paths (`/summon`, eggs, dispensers).

## Keys

Top level: `configVersion` (int, default `1`) — schema marker managed by the
mod; a warning is logged if the file carries a newer version than the build.

### `[spawning]`

| Key | Default | Effect |
|---|---|---|
| `spawningEnabled` | `true` | `false` cancels **natural** spawns of every dwurdysharks species. Manual spawns (`/summon`, spawn egg, dispenser, bucket, breeding) still work — admin escape hatch. |
| `oceanOnly` | `true` | `true` restricts all water spawn predicates to the `dwurdysharks:shark_spawning_oceans` biome tag. `false` restores pre-1.4.0 behavior (any water body). |
| `spawning.species.<id>Enabled` | `true` | Per-species natural-spawn toggle for all 24 registered species (`axodile` … `whitetip_shark`; excludes projectiles). |

### `[population]`

| Key | Default | Range | Effect |
|---|---|---|---|
| `largeSharkGlobalCap` | `0` | 0–100000 | Maximum wild non-exempt `LARGE_SHARKS` entities **per dimension**; `0` = unlimited. Gamerule `dwurdySharksLargeSharkGlobalCap` overrides when set `>= 0`. |
| `ambientFishGlobalCap` | `0` | 0–100000 | Same for `AMBIENT_FISH` (krill, remora, pilot fish, roll particle). |

Exempt from both global and local caps: tamed, named, persistence-required,
bucket-released, and bred entities.

### `[damage]`

| Key | Default | Range | Effect |
|---|---|---|---|
| `sharkDamageMultiplier` | `1.0` | 0–100 | Multiplier on every shark hit. |
| `megalodonDamage` | `20.0` | 0–1000 | Absolute base damage for Megalodon hits, before multipliers. |
| `megalodonArmorBypass` | `false` | — | `true` routes Megalodon hits through `dwurdysharks:megalodon_bite` (ignores armor). |
| `damage.speciesMultiplier.<id>` | `1.0` | 0–100 | Per-species damage multiplier, **stacks** with `sharkDamageMultiplier`. |
| `damage.speciesHealth.<id>` | `0.0` | 0–100000 | Absolute max-health override in half-hearts; `0` = inherit entity base. Applied on entity join. |

### `[movement]`

| Key | Default | Range | Effect |
|---|---|---|---|
| `sharkSpeedMultiplier` | `1.0` | 0–100 | Global movement-speed multiplier for every mod entity. Applied on entity join. |
| `movement.speciesMultiplier.<id>` | `1.0` | 0–100 | Per-species speed multiplier, stacks with the global one. |

### `[behavior]`

| Key | Default | Range | Effect |
|---|---|---|---|
| `dryoutDelayTicks` | `600` | 0–72000 | Ticks out of water before dryout starts (600 = 30 s). `0` disables dryout. |
| `dryoutDurationTicks` | `600` | 20–72000 | Duration of each dryout effect application. |
| `itemEatingEnabled` | `true` | — | `false` stops all dropped-food-eater species from consuming dropped items. |
| `sharksAttackBoats` | `false` | — | `true` makes `large_sharks`-tagged sharks path to and ram **occupied** boats (rowboats and chest boats) until they break — deterrent/border-marking use. Empty boats are ignored, tamed sharks are exempt, named/persistent sharks stay eligible. Independent of the `dwurdySharksAggressiveSharks` gamerule. |
| `aggroFollowRangeMultiplier` | `1.0` | 0.1–10 | Scales `FOLLOW_RANGE` — how far sharks acquire and chase targets (see `dwurdySharksAggressiveSharks` gamerule). Applied on entity join. |

### `[despawn]`

| Key | Default | Range | Effect |
|---|---|---|---|
| `hardDespawnDistanceBlocks` | `128` | 16–512 | Wild non-exempt mod entities are discarded when no player is within this distance (128 = vanilla WATER_CREATURE). Vanilla random soft-despawn (32–128) is unchanged — values below ~32 make it unreachable. |

## Examples

Halve all shark speed, double only the bull shark:

```toml
[movement]
sharkSpeedMultiplier = 0.5
  [movement.speciesMultiplier]
  bull_shark = 4.0   # 0.5 x 4.0 = 2x net
```

Hard cap the server at 50 large sharks, still per-world overridable:

```toml
[population]
largeSharkGlobalCap = 50
# in-game per world: /gamerule dwurdySharksLargeSharkGlobalCap 30
```

## Species keys

`axodile`, `barracuda`, `basking_shark`, `blacktip_reef_shark`, `blue_shark`,
`bonnethead_shark`, `bull_shark`, `cookiecutter_shark`, `greater_axodile`,
`greatwhiteshark`, `greenland_shark`, `krill`, `land_shark`, `lemon_shark`,
`mako_shark`, `megalodon`, `nurse_shark`, `pilot_fish`, `remora`,
`roll_particle`, `thalassoger`, `tiger_shark`, `whale_shark`, `whitetip_shark`.
