## Intent

All 24 entity classes call `this.refreshDimensions()` inside `baseTick()` —
~40,000 useless calls/sec at 2,000 entities. These entities have static
dimensions (`getDefaultDimensions` returns a constant-scaled value, e.g.
`BullSharkEntity.java:301-303`), so the call is pure overhead.

## Expectation

`refreshDimensions()` is invoked only when a dimension-affecting state changes
(pose/variant/scale) — which for current entities means never per-tick. Hitbox
sizes remain identical to today.

## Acceptance criteria

- [ ] Zero `refreshDimensions()` calls inside `baseTick()` across all entity
      classes (24 verified occurrences).
- [ ] Static gate: `refreshDimensions` does not appear in `baseTick()` bodies.
- [ ] Hitbox/dimensions unchanged — existing GameTests pass; a spawn-and-tick
      sanity test asserts bounding box size matches the previous constant.
- [ ] If any entity genuinely changes shape at runtime (verify: none found in
      audit — implementer must confirm), `refreshDimensions()` is called at the
      state-transition point only, not per tick.

## Context that code cannot infer

- Why now: trivial change, real win, and it removes noise before profiling the
  heavier fixes.
- MCreator generates this call; removing it is safe when dimensions are
  static — the audit confirmed no pose/scale transitions in the 24 classes.

## Scope

- Type: bug (performance). Priority: P1. Milestone: 1.4.0.
- Labels: `bug`, `priority:p1`, `area:performance`.
- Affected: all `entity/*.java` `baseTick()` overrides (24 classes).
- Assignee: unassigned.

## Non-goals

- No behavior/dimension changes.
- No other `baseTick()` refactors (dryout is #2, global-event cleanup is #10).

## Dependencies and open decisions

- None. Open decision: none material — only the "does any entity change
  dimensions" check, which the implementer confirms during the sweep.

## Verification

- Static gate (grep/JUnit source scan): pattern absent.
- Existing 5 GameTests pass; add bounding-box assertion to an existing spawn
  test rather than a new test class.

## Proposed approach (non-binding)

Delete the call line in each `baseTick()`; if a species later adds a pose
system, call `refreshDimensions()` inside the setter that changes the pose.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
