## Intent

A beached shark currently enqueues a new `queueServerWork(600, …)` lambda
**every tick** — up to ~600 live delayed tasks per shark, each pinning the
entity in `workQueue`. With hundreds of beached/dryout-adjacent entities this
produces real heap pressure plus an O(queue) full traversal every server tick —
the closest thing in this codebase to the reported "memory leak".

## Expectation

Dryout is driven by per-entity state, not by scheduling delayed tasks. In
water/rain: counter resets and `DRYOUT_EFFECT` is removed. Out of water:
counter increments; when it crosses the species threshold the effect is applied
once and refreshed only as needed. `workQueue` no longer grows with the number
of beached sharks.

## Acceptance criteria

- [ ] No `*OnEntityTickUpdateProcedure` calls `queueServerWork` for dryout.
      Affected files (19): Axodile, Barracuda, BaskingShark, BlacktipReefShark,
      BlueShark, BonnetheadShark, BullShark, CookiecutterShark, GreenlandShark,
      Krill (300t), LemonShark, MakoShark, Megalodon, NurseShark, PilotFish
      (320t), Remora (320t), Shrak, TigerShark, WhaleShark, WhitetipShark.
- [ ] Dryout effect is applied at most once per threshold crossing per entity;
      re-application happens only to extend an expiring effect, not per tick.
- [ ] Static gate: no `queueServerWork` inside dryout tick procedures.
- [ ] GameTest: a shark out of water for `threshold` ticks gains
      `DRYOUT_EFFECT` exactly once; returning to water clears it; repeated
      beaching does not accumulate queued work (assert via test-visible queue
      size or instrumentation hook).
- [ ] `workQueue` size is invariant to the number of beached sharks
      (measurable: N beached sharks ⇒ queue does not grow by N per tick).

## Context that code cannot infer

- `DRYOUT_EFFECT` already deals 1 damage/tick via
  `shouldApplyEffectTickThisTick()`; the delayed task only *applies* the
  effect. A counter fully replaces the delay semantics.
- `*EntityIsHurtProcedure` `queueServerWork(600)` calls are per-hit effect
  cleanup (e.g. `TigerSharkEntityIsHurtProcedure.java:40-48`) — bounded and out
  of this issue's scope; noted for the P2 cleanup issue.
- Keep `queueServerWork` itself: legitimate one-shot uses exist (milk cooldowns
  `6000t`, landmines). Only the per-tick dryout call pattern is the defect.
- Why now: this is the primary heap/GC-pressure mechanism and also holds entity
  references after the entity should be discardable.

## Scope

- Type: bug (memory/performance). Priority: P0. Milestone: 1.4.0.
- Labels: `bug`, `priority:p0`, `area:performance`.
- Affected: `procedures/*OnEntityTickUpdateProcedure.java` (19 files),
  entity classes (counter field/persistent data), possibly a shared helper.
- Assignee: unassigned.

## Non-goals

- No redesign of `workQueue`/`BenssharksMod.tick()` (may still be needed for
  one-shots; a residual-risk note goes to #11 if the queue can still be abused).
- No changes to hurt-event effect cleanup, milking timers, or landmine timing.
- No `RollParticle` rework (handled by issue #10).

## Dependencies and open decisions

- Blocks: none. Open decision (minor): store `dryTicks` as an entity field with
  `addAdditionalSaveData`/`readAdditionalSaveData` (survives unload — preferred
  for correctness) vs `getPersistentData()` (simpler, matches MCreator style).
  Either satisfies the criteria; prefer saved field so partially-dry sharks
  don't reset on chunk reload.

## Verification

- GameTest additions in `BenssharksGameTests` (or sibling class): dryout
  applies once at threshold, clears in water, does not duplicate.
- Static gate (same mechanism as issue #1): `queueServerWork` absent from
  `*OnEntityTickUpdateProcedure` dryout blocks.
- Spark `--alloc --alloc-live-only` (release gate): `Tuple`/lambda/node counts
  return to baseline after entities are removed + GC.

## Proposed approach (non-binding)

Add `private int dryTicks` (+ per-species threshold constant) to each aquatic
entity or a shared base class; move the water/dry check into the entity's
`baseTick()`; delete the `queueServerWork` calls. Consider a shared
`AquaticDryout` helper to avoid 19 copies of identical logic.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
