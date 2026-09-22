# Milestone v1.4.1 — Configuration Standardization & Admin Controls

## Intent

1.4.0 shipped `dwurdysharks-server.toml` with spawning toggles and damage, plus
cap gamerules. Server feedback (Discord) asks for a **hard per-server maximum**,
a **speed multiplier**, and per-species tuning. This milestone professionalizes
the config into a stable, documented schema and adds the missing admin knobs —
without creating competing sources of truth.

## Design contract (resolved)

- **D1 — Config vs gamerule boundary:** config = server-wide defaults and
  attribute/toggle knobs; gamerules = live per-world runtime knobs. Where a
  value exists in both (global caps), the gamerule uses sentinel `-1` =
  "inherit config" so there is exactly one read path:
  `gamerule == -1 ? config : gamerule`.
- **D2 — Global caps are gamerules AND config:** `largeSharkGlobalCap` /
  `ambientFishGlobalCap` gamerules (default `-1` → inherit) over
  `population.*` config keys (default `0` = unlimited). Counted per dimension
  at `FinalizeSpawn` only — zero per-tick cost.
- **D3 — Attribute multipliers, not base values:** speed/health/damage are
  exposed as multipliers (global + per-species), applied as fixed-ID transient
  attribute modifiers on `EntityJoinLevelEvent`. `createAttributes()` runs
  before SERVER config loads, so modifiers are the only correct hook; a fixed
  `ResourceLocation` ID makes re-application idempotent.
- **D4 — Per-species overrides follow existing convention:**
  `[movement.speciesMultiplier]`, `[damage.speciesMultiplier]`, and
  `[damage.speciesHealth]` sections with one double per species key —
  consistent with the existing `[spawning.species]` block, type-safe, and
  self-documenting in TOML.
- **D5 — Spawn weights/min/max stay in datapack JSON** (NeoForge biome-modifier
  mechanism is the native override path; config duplicates would be a third
  source of truth). Documented, not duplicated.
- **D6 — `configVersion` field** (integer, default 1) for future migrations.

## Issues

| # | Title | Priority |
|---|-------|----------|
| 1 | Config schema v2 — sections, units, `configVersion`, comments | P0 |
| 2 | Attribute multipliers — speed (global + per-species), health, per-species damage | P0 |
| 3 | Global per-dimension caps — config defaults + gamerule overrides | P0 |
| 4 | Configurable despawn distance | P1 |
| 5 | Dryout timing config (replaces 19 hardcoded call sites) | P1 |
| 6 | Behavior toggles — `itemEatingEnabled`, `capAppliesToManualSpawns` unification | P2 |
| 7 | `docs/CONFIG.md`, `GAMERULES.md` sync, CurseForge page update | P1 |
| 8 | GameTest coverage + 1.4.1 release gate | P0 |

## Non-goals

- Per-species spawn weights/group sizes (datapack JSON — D5).
- Client-side config (all knobs are server-authoritative).
- Live config reload without restart — SERVER config reloads on `/reload`
  where NeoForge supports it; attribute modifiers re-apply on next entity join.
- Vanilla `spawning.species` renames/removals — additive only; no key removed
  in 1.4.1.

## Risks

- Attribute-modifier timing: modifiers must be applied idempotently on every
  join (entity NBT does not persist transient modifiers; permanent ones would
  stack). Mitigation: fixed modifier IDs + transient application.
- Global-cap counting cost: a whole-dimension entity scan is O(entities) but
  runs only at `FinalizeSpawn`, matching the local-cap precedent (D5, 1.4.0).
- Despawn distance: vanilla soft-despawn (32–128 random) remains; the config
  controls only the hard distance. A distance < vanilla soft range would make
  soft-despawn unreachable — clamp documented minimum to avoid paradoxes.
