# Issue — 1.4.1 release gate: regression suite + version bump

## Intent

Every new knob lands with proof. Extend the GameTest suite with
config-specific coverage and re-run the full 1.4.0 gate unchanged — the
stress profiles must pass at default config exactly as before.

## Expectation

- New required tests (batch `config`):
  - `config_speed_multiplier_applied` — MOVEMENT_SPEED doubles at
    multiplier 2.0 (set config in test, spawn, assert attribute).
  - `config_species_override_wins` — per-species speed/damage override
    affects only that species.
  - `config_global_cap_enforced` — N+1st wild spawn across a *separate*
    radius refused; gamerule override beats config.
  - `config_dryout_timings` — shortened delay produces the effect; `0`
    disables.
  - `config_despawn_distance` — discard beyond configured distance,
    exemptions honored.
  - `modifier_rejoin_idempotent` — attribute unchanged across a
    remove/rejoin cycle.
- All 1.4.0 tests pass at default config — zero regressions.
- Static guards extended: every `ConfigValue` key appears in
  `docs/CONFIG.md`; no `dryTick(` literal args remain; modifier IDs unique.

## Acceptance criteria

- [ ] New batch `config` runs in `runGameTestServer` alongside existing
      batches; all listed tests green locally.
- [ ] Full suite (existing 17 required + new config tests + 5×300 s stress
      profiles) green on a clean world.
- [ ] CI `build` + `release.yml` green on the `v1.4.1` tag.
- [ ] `mod_version=1.4.1`, `docs/releases/v1.4.1-mc1.21.1.md`, tag
      `v1.4.1`, GitHub release + CurseForge publish — **after maintainer
      decision** (same authority rule as 1.4.0).

## Context that code cannot infer

- Config mutation inside GameTests: tests must set `ConfigValue` state
  directly (the spec holder allows it in-dev) and **restore defaults in a
  `finally`** — leaked config state between batches was a 1.4.0 flake class
  (biome test); same isolation discipline applies.
- Config tests must not rely on `serverconfig` file IO — they operate on the
  loaded spec object.

## Scope

- Type: chore/test. Priority: P0. Milestone: 1.4.1.
- Labels: `tests`, `priority:p0`, `area:gametest`.
- Affected: `DwurdySharksGameTests.java`, `tools/*.ps1` guards,
  `gradle.properties`, `docs/releases/v1.4.1-mc1.21.1.md`, `release.yml`
  (unchanged — consumes notes path).
- Assignee: unassigned.
