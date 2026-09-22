## Intent

`BonnetheadShark`, `BlacktipReefShark`, and `Barracuda` are registered as
`MobCategory.WATER_AMBIENT` — the pool meant for small decorative wildlife
(squid-class). They compete in the ambient budget instead of the water-creature
budget, producing wrong population mix (predators crowd out ambient fish and
don't count against the creature pool they belong to). Alex's Mobs uses the
same split: sharks = `WATER_CREATURE`, small fauna (lobster, etc.) =
`WATER_AMBIENT`.

## Expectation

The three species register as `WATER_CREATURE`. Krill, Remora, and PilotFish
remain `WATER_AMBIENT`. No other behavioral change.

## Acceptance criteria

- [ ] `DwurdySharksModEntities`: `BonnetheadSharkEntity` (:97),
      `BlacktipReefSharkEntity` (:121), `BarracudaEntity` (:234) →
      `MobCategory.WATER_CREATURE`.
- [ ] Krill (:177), Remora (:65), PilotFish (:193) remain `WATER_AMBIENT` —
      static check or registry assertion.
- [ ] Category is reflected correctly in spawn placement (spawn-cost/cap
      groupings from #4 treat them as predators where intended — verify
      groupings still classify them correctly).
- [ ] Existing tests pass; registry loads without errors.

## Context that code cannot infer

- `WATER_CREATURE` vs `WATER_AMBIENT` changes the vanilla per-chunk spawn
  budget bucket — this is a population-policy fix, not cosmetic.
- Land deliberately as its own tiny PR: trivially reviewable, and keeps #4's
  spawn-cost numbers honest (ambient budget changes when these move pools).

## Scope

- Type: bug (spawn policy). Priority: P1. Milestone: 1.4.0.
- Labels: `bug`, `priority:p1`, `area:spawning`.
- Affected: `init/DwurdySharksModEntities.java` only.
- Assignee: unassigned.

## Non-goals

- No weight/group edits (that's #4). No other category changes.

## Dependencies and open decisions

- Soft ordering with #4 (cap groupings reference these categories). No open
  decisions.

## Verification

- Compile + existing GameTests; registry dump or runtime check showing the
  three entities under `WATER_CREATURE`.

## Proposed approach (non-binding)

Three-line change in `DwurdySharksModEntities`.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
