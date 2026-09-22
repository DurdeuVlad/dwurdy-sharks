# Issue — Dryout timing config

## Intent

`DryoutProcedure.dryTick(entity, 600, 600)` is hardcoded at ~19 species tick
procedures — beached sharks take 30 s to start drying and the effect lasts
30 s, with no admin control. Expose both timings in config.

## Expectation

- `behavior.dryoutDelayTicks` (int, default 600, range 0–72000) — out-of-water
  ticks before the dryout effect applies.
- `behavior.dryoutDurationTicks` (int, default 600, range 20–72000) — effect
  duration per application.
- `dryoutDelayTicks = 0` disables dryout entirely (documented).
- All `*OnEntityTickUpdateProcedure` call sites read config instead of
  literals.

## Acceptance criteria

- [ ] No `dryTick(` call site passes literal timing arguments.
- [ ] Config change applies on next tick-procedure evaluation — no restart.
- [ ] GameTests: (a) dryout fires after configured delay (short value, e.g.
      20 ticks); (b) delay `0` means no dryout effect ever applies.

## Context that code cannot infer

- The delay counter persists in entity `getPersistentData()` under
  `dwurdysharks_dry_ticks` — lowering the delay mid-session is safe (counter
  already ≥ new delay → immediate effect); raising it just delays. Document.
- The existing `stress_dryout_beached` profile (300 s, default 600) must still
  pass unchanged — config reads must not add measurable per-tick cost
  (cache the `ConfigValue` lookups; `ModConfigSpec.IntValue.get()` is cheap,
  but 19 call sites × every tick is worth a measured check).

## Scope

- Type: enhancement. Priority: P1. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p1`, `area:config`, `area:entities`.
- Affected: `DwurdySharksConfig.java`, ~19 `*OnEntityTickUpdateProcedure.java`,
  `DwurdySharksGameTests.java`.
- Assignee: unassigned.
