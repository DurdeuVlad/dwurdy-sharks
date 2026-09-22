# Issue — Configurable despawn distance

## Intent

Vanilla `WATER_CREATURE` hard-despawns past 128 blocks — hardcoded. Servers
running shark encounters want shorter cleanup (performance) or longer
persistence (large oceans). Make the hard distance configurable.

## Expectation

- `despawn.hardDespawnDistanceBlocks` (int, default 128, range 16–512).
- Wild, non-exempt mod entities farther than the configured distance from the
  nearest player are discarded — replacing vanilla's fixed 128 for our
  entities.
- Same exemptions as caps: tamed, named, persistence-required.
- Vanilla soft-despawn (random 32–128) is unchanged; document that a
  configured hard distance below ~32 makes soft-despawn unreachable.

## Acceptance criteria

- [ ] Distance check runs amortized (staggered per entity, e.g. every ~40
      ticks by entity-id hash) — no per-tick player-distance scans.
- [ ] A shark placed beyond the configured distance despawns; one inside it
      persists; exempt entities never despawn.
- [ ] At default 128, behavior matches 1.4.0 vanilla semantics.
- [ ] GameTests: despawn beyond configured distance; exemption honored.

## Context that code cannot infer

- Vanilla despawns via `Mob.checkDespawn()` reading the category's
  `despawnDistance`/`noDespawnDistance` — immutable per `MobCategory`.
  Implementation therefore needs an explicit periodic check (entity tick
  event filtered to mod entities), not a category override.
- The 1.4.0 gate's `despawn` test asserts >128-block behavior; at default the
  new path must satisfy that existing test unchanged.

## Scope

- Type: enhancement. Priority: P1. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p1`, `area:config`, `area:entities`.
- Affected: `DwurdySharksConfig.java`, despawn-check procedure/handler,
  `DwurdySharksGameTests.java`.
- Assignee: unassigned.
