# Game rules

All rules live in the `mobs` category and are prefixed `dwurdySharks` so they
are distinguishable from vanilla and other mods' rules. Set per world with
`/gamerule <name> <value>`; changes take effect immediately.

## `dwurdySharksAggressiveSharks`

- Type: boolean. Default: `false`.

When `false` (the default), sharks behave exactly as in previous versions:
they hunt their normal aquatic prey and retaliate when hurt, but never seek
out players.

When `true`, the large predatory species additionally hunt players as prey:

- Great white shark (`dwurdysharks:greatwhiteshark`)
- Bull shark
- Tiger shark
- Mako shark
- Bonnethead shark
- Blacktip reef shark
- Lemon shark
- Cookiecutter shark
- Megalodon
- Blue shark
- Greenland shark
- Oceanic whitetip shark
- Nurse shark (tamed individuals never target their owner)
- Barracuda

Filter feeders and non-predatory species are intentionally unaffected and
stay passive even with the rule enabled: whale shark, basking shark, krill,
remora, pilot fish, axodile, greater axodile, land shark, and thalassoger.

The rule is strictly opt-in; existing worlds and servers upgrading in place
keep the previous passive behavior because the default is `false`. How far
sharks notice targets is tuned via `behavior.aggroFollowRangeMultiplier` in
`dwurdysharks-server.toml`.

## `dwurdySharksLargeSharkLocalCap`

- Type: int. Default: `24`.

Maximum wild, non-exempt `LARGE_SHARKS`-tagged entities within
`dwurdySharksSpawnCapRadius` blocks of a spawn point. Additional wild spawns
in that radius are refused. `0` disables the local cap (global caps still
apply).

## `dwurdySharksAmbientFishLocalCap`

- Type: int. Default: `64`.

Same as `dwurdySharksLargeSharkLocalCap` but for `AMBIENT_FISH`-tagged
entities (krill, remora, pilot fish, roll particle).

## `dwurdySharksSpawnCapRadius`

- Type: int. Default: `128`.

Radius in blocks within which the local caps count existing entities.

## `dwurdySharksEnforceCapForManualSpawns`

- Type: boolean. Default: `true`.

When `true`, `/summon`, spawn eggs, and dispensers are refused once a cap
(local **or** global) is reached. When `false`, manual spawn paths ignore
caps entirely — natural spawns are still capped. Bucket-released and bred
entities are always exempt.

## `dwurdySharksLargeSharkGlobalCap`

- Type: int. Default: `-1` (inherit config).

Whole-dimension ceiling on wild non-exempt `LARGE_SHARKS` entities —
independent of spawn location, unlike the local caps. `-1` inherits
`population.largeSharkGlobalCap` from `dwurdysharks-server.toml`;
`>= 0` overrides it for this world; effective `0` = unlimited.

## `dwurdySharksAmbientFishGlobalCap`

- Type: int. Default: `-1` (inherit config).

Same as `dwurdySharksLargeSharkGlobalCap` for `AMBIENT_FISH`; inherits
`population.ambientFishGlobalCap`.

## Cap semantics (local and global)

Counted at spawn-finalize only — there is no per-tick population scan.
Exempt from counts and never refused: tamed, named (`NameTag`),
persistence-required, bucket-released, and bred entities. When both caps are
active a spawn must pass the local check first, then the global check.

## Renamed in 1.4.1

These rules were introduced without the prefix in earlier development builds
and renamed for clarity:

| Old name | New name |
|---|---|
| `aggressiveSharks` | `dwurdySharksAggressiveSharks` |
| `largeSharkLocalCap` | `dwurdySharksLargeSharkLocalCap` |
| `ambientFishLocalCap` | `dwurdySharksAmbientFishLocalCap` |
| `sharkSpawnCapRadius` | `dwurdySharksSpawnCapRadius` |
| `enforceCapForManualSpawns` | `dwurdySharksEnforceCapForManualSpawns` |
| `largeSharkGlobalCap` | `dwurdySharksLargeSharkGlobalCap` |
| `ambientFishGlobalCap` | `dwurdySharksAmbientFishGlobalCap` |

Worlds that stored values under the old names revert to defaults — re-apply
them under the new names.
