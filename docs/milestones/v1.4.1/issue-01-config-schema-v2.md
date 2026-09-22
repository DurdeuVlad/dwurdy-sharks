# Issue — Config schema v2

## Intent

`DwurdySharksConfig` grew organically (spawning + damage in 1.4.0). Before new
knobs land, standardize the file: stable section names, units in comments,
valid ranges on every numeric key, and a `configVersion` field for future
migrations. This is the foundation every other 1.4.1 issue extends.

## Expectation

- Sections: `configVersion`, `spawning`, `population`, `damage`, `movement`,
  `behavior`, `despawn`.
- Every numeric key declares an explicit range in `defineInRange` and states
  its unit (blocks, ticks, multiplier) in the comment.
- No existing key renamed or removed — 1.4.0 keys keep working (`additive
  only`); comments may be improved.
- `configVersion` int default `1`; code logs once at load if the file carries
  a newer version than the mod understands.

## Acceptance criteria

- [ ] `DwurdySharksConfig` reorganized into the section layout above; all
      1.4.0 keys present with identical names/defaults.
- [ ] `configVersion` key exists; unknown-version handling logs a warning.
- [ ] Every `defineInRange` bound is sensible (multipliers ≥ 0, caps ≥ -1 or 0,
      distances within sane world bounds) and documented in the comment.
- [ ] `docs/CONFIG.md` generated/updated to describe every key (issue 7 owns
      the prose; this issue owns the schema being documented).
- [ ] Existing GameTest suite still passes (no behavior change).

## Context that code cannot infer

- SERVER-type config loads per-world at world start — too late for
  `createAttributes()` (common-setup). This is why attribute knobs (issue 2)
  must apply via join-time modifiers, not attribute builders.
- Section order in the file should match the README design table so admins
  can map docs → file mechanically.

## Scope

- Type: enhancement. Priority: P0. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p0`, `area:config`.
- Affected: `DwurdySharksConfig.java`, `docs/CONFIG.md`.
- Assignee: unassigned.
