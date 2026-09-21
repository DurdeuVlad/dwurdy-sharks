## Intent

Several per-tick procedures re-run the same AABB entity query multiple times
per activation, each with a full distance sort. Worst offenders:
`BarracudaSprintProcedure` (19 `getEntitiesOfClass` calls),
`EatKrillProcedure` (18), `EatDroppedItemProcedure` (~9). At scale this is a
major allocation + CPU source entirely separable from the goal-count problem.

## Expectation

Each scan procedure performs **at most one** entity query per activation, finds
the nearest match in a single pass (`min(comparator)` — no full sort), and
runs on a staggered throttle (`tickCount % 10 == entityId % 10` style, ~2 Hz
effective rate) instead of every tick.

## Acceptance criteria

- [ ] `EatDroppedItemProcedure`, `EatKrillProcedure`, `BarracudaSprintProcedure`,
      `MakoSprintProcedure`, `FollowIfTamedProcedure` (path recompute): ≤1
      `getEntitiesOfClass` call per activation each.
- [ ] No `.sorted(...)` on entity queries in these files — nearest match via
      `min(Comparator.comparingDouble(distanceToSqr))` or manual min loop.
- [ ] Scans are throttled (≤ every 10 ticks per entity) with an entity-derived
      offset so populations don't synchronize into spike ticks.
- [ ] Behavior preserved — GameTest: shark paths to and consumes a dropped
      food item; krill-eating species still eats krill.
- [ ] Client side: no scans run client-side (procedures already gate on
      `!level().isClientSide()` — preserve).

## Context that code cannot infer

- Verified counts at `5c860ef`: EatKrill 18, BarracudaSprint 19,
  EatDroppedItem ~9 `getEntitiesOfClass` calls per execution.
- `EatDroppedItemProcedure` subscribes `EntityTickEvent.Pre` for **all**
  `GeoEntity` then instanceof-filters — the throttling and single-pass fix
  apply within the current structure; moving it off the global bus is #10.
- Food semantics to preserve: only FOOD-component items attract sharks;
  golden/enchanted-golden apple give bonus effects; the item is consumed on
  close range (≤2.5 blocks) with an eat sound + heal.

## Scope

- Type: bug (performance). Priority: P1. Milestone: 1.4.0.
- Labels: `bug`, `priority:p1`, `area:performance`.
- Affected: the 5 procedures named above (plus any sibling scan procedures the
  implementer finds with the same pattern — document additions in the PR).
- Assignee: unassigned.

## Non-goals

- No relocation of these handlers off the global event bus (#10).
- No behavior/food-balance changes.
- No changes to `*OnInitialEntitySpawnProcedure` (#1) or dryout (#2).

## Dependencies and open decisions

- Ordering: after #1/#2. Overlaps #10 — land this first (smaller), #10 then
  moves the already-fixed code.
- Open decision: throttle interval (proposal: 10 ticks) and whether
  `FollowIfTamedProcedure` needs continuous pathing vs throttled re-path —
  choose per behavior and document.

## Verification

- Static gate: per-file `getEntitiesOfClass` count ≤1 in the named files.
- GameTests: drop food → shark approaches + consumes; krill consumed.
- Allocation profile (#11): `EntityGetter`/stream garbage from these
  procedures gone; AABB allocations in tick path reduced.

## Proposed approach (non-binding)

Extract `NearestEntity` helper (`getEntitiesOfClass` once + `min()`); add
`shouldScan(entity, interval)` = `(entity.tickCount + entity.getId()) %
interval == 0`; early-exit before any query when the instanceof/type gate
fails.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
