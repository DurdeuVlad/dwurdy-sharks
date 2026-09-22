# Issue — Global per-dimension caps

## Intent

The Discord ask: "numărul maxim de rechini care se pot spawna" — a **hard
server-wide maximum**, not just a per-radius local cap. Local caps (1.4.0)
stop clustering but can't bound total population across a large ocean.

## Expectation

- New gamerules `largeSharkGlobalCap` and `ambientFishGlobalCap`, default
  `-1` = "inherit config".
- New config keys `population.largeSharkGlobalCap` and
  `population.ambientFishGlobalCap` (int, default `0` = unlimited, range
  0–100000).
- Effective cap = gamerule if `>= 0`, else config value; `0` effective =
  disabled.
- Counted across the **whole dimension** at `FinalizeSpawn`: wild,
  non-exempt entities of the same group tag. Same exemptions as local caps
  (tamed, named, persistence-required, bucket, breeding).
- Manual spawns (`/summon`, eggs, dispensers) respect the global cap only when
  `enforceCapForManualSpawns` is on — one switch governs both cap families.

## Acceptance criteria

- [ ] `SharkSpawnCapProcedure` checks local cap first, then global cap; a
      spawn failing either is cancelled.
- [ ] Global count uses `level.getEntities()` filtered by group tag +
      `!isExempt` — evaluated only at finalize, zero per-tick cost.
- [ ] Gamerule `-1` inherits config; gamerule `>= 0` overrides it live;
      effective `0` disables.
- [ ] GameTests: (a) with global cap N, the N+1st wild spawn in a *different*
      radius is refused; (b) tamed/named exempt; (c) `enforceCapForManualSpawns`
      gates manual spawns against the global cap; (d) gamerule override wins
      over config.

## Context that code cannot infer

- Sentinel design (D1/D2): one read path avoids the "which source wins"
  ambiguity that burned the spawn-guard refactor in 1.4.0. The gamerule is
  the live knob; the config is the shipped default.
- Per-dimension, not per-server-process: `getEntities()` is scoped to a
  `ServerLevel`; sharks don't cross dimensions, so dimension == effective
  scope. Comment this.
- Counting must include entities being finalized in the same tick batch —
  the existing local-cap code counts `e != mob` for exactly this reason;
  keep that guard.

## Scope

- Type: enhancement. Priority: P0. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p0`, `area:spawning`, `area:config`.
- Affected: `DwurdySharksModGameRules.java`, `DwurdySharksConfig.java`,
  `SharkSpawnCapProcedure.java`, `DwurdySharksGameTests.java`,
  `docs/GAMERULES.md`.
- Assignee: unassigned.
