# Issue — Config & gamerule documentation

## Intent

The schema is only professional if an admin can discover every knob without
reading Java. Ship `docs/CONFIG.md` as the authoritative reference and keep
`GAMERULES.md` in sync, including the config↔gamerule interplay (D1).

## Expectation

- `docs/CONFIG.md`: every key, section, type, default, valid range, unit,
  reload semantics (SERVER config = per-world, applies at world load;
  attribute modifiers apply on next entity join), and worked examples
  (e.g. "halve all shark speed", "cap server at 50 large sharks").
- `docs/GAMERULES.md` updated: new global-cap gamerules, the `-1` = inherit
  sentinel, and the unified `dwurdySharksEnforceCapForManualSpawns` semantic.
- A "which knob wins" table: config default → gamerule override → datapack
  spawn weights — one place explaining precedence.
- CurseForge page (`CURSEFORGE.md`) gains a short "Server configuration"
  section pointing at the two docs.

## Acceptance criteria

- [ ] `docs/CONFIG.md` covers 100% of config keys (checked against
      `DwurdySharksConfig` fields — static guard script or checklist).
- [ ] `docs/GAMERULES.md` covers all gamerules including the two new caps and
      the sentinel semantics.
- [ ] Precedence table matches the actual read order in code.
- [ ] All examples in docs are valid TOML/commands (copy-paste tested).

## Context that code cannot infer

- `dwurdysharks-server.toml` lives at `<world>/serverconfig/` — singleplayer
  and dedicated-server paths differ; document both.
- NeoForge SERVER configs do not hot-reload into already-spawned entities;
  the modifier design means join-time application — phrase as "existing
  entities pick up changes on rejoin/relog", not "instant".

## Scope

- Type: documentation. Priority: P1. Milestone: 1.4.1.
- Labels: `documentation`, `priority:p1`, `area:config`.
- Affected: `docs/CONFIG.md` (new), `docs/GAMERULES.md`, `CURSEFORGE.md`.
- Assignee: unassigned.
