## Intent

Per-species behavior is implemented as global `EntityTickEvent.Pre`
subscribers that run for every entity then instanceof-filter — an MCreator
pattern that doesn't scale and hides per-entity cost. Worst offender:
`RollParticleEntity`, an effect/particle entity carrying ~47 goals including
44 target goals, plus `RollParticleOnEntityTickUpdateProcedure` queueing
60-tick work every tick.

## Expectation

Behavior that belongs to one species lives in that entity's `tick()`/
`baseTick()` or a dedicated `Goal`, not on the global event bus.
`RollParticleEntity` is either an AI-free timed entity or keeps only the
minimal goals it provably needs.

## Acceptance criteria

- [ ] `RollParticleEntity`: 44 target goals removed; if it needs no combat AI
      it becomes a fixed-lifetime no-AI entity (or removes itself after its
      visual lifetime) — record the finding: is it ever targeted/attacked in
      gameplay? If yes, keep only the needed goals.
- [ ] `RollParticleOnEntityTickUpdateProcedure`'s per-tick
      `queueServerWork(60)` replaced by a lifetime counter or discard check.
- [ ] Global tick subscribers that duplicate per-entity state are moved into
      entity tick/goals: `EatDroppedItemProcedure`, `FollowIfTamedProcedure`,
      `StopRidingBoatProcedure`, `StopFloatingProcedure`, sprint procedures
      (`BarracudaSprint`, `MakoSprint`) — final list defined by an inventory
      pass; every move keeps behavior identical.
- [ ] `*EntityIsHurtProcedure` `queueServerWork(600)` effect-cleanup lambdas
      removed where redundant (e.g. DOLPHINS_GRACE already expires at 600t —
      `TigerSharkEntityIsHurtProcedure.java:40-48`); kept only where they
      implement real timed logic.
- [ ] Remaining global subscribers are only those handling genuinely global
      concerns (cross-entity systems like bleeding), listed in the PR.
- [ ] All existing GameTests pass; behavior-equivalence spot-check per moved
      procedure.

## Context that code cannot infer

- This is deliberately P2: it is structural hygiene, and landing it before the
  P0/P1 fixes would muddy profiling attribution. After #5/#7 the remaining
  global handlers are small.
- `queueServerWork` stays for legitimate one-shots (milk cooldowns,
  landmines) — the mechanism isn't the bug; per-tick scheduling was (#2).
- `RollParticleEntity` audit is part of this issue's work: name suggests a
  visual effect; 44 target goals on a particle is generated-code debris.

## Scope

- Type: refactor/enhancement. Priority: P2. Milestone: 1.4.0.
- Labels: `enhancement`, `priority:p2`, `area:performance`.
- Affected: `procedures/*` global tick subscribers, `RollParticleEntity`,
  entity classes gaining tick logic.
- Assignee: unassigned.

## Non-goals

- No redesign of `DwurdySharksMod.tick()`/`workQueue` mechanics.
- No behavior changes beyond what's required to relocate handlers.
- No MCreator-to-handwritten migration beyond the listed subscribers (broader
  migration is a future milestone).

## Dependencies and open decisions

- Depends on #5 and #7 (they rewrite the same procedures — this issue relocates
  the fixed versions). Land last among code issues.
- Open decision: keep-or-strip for `RollParticleEntity` AI — the audit found no
  evidence it needs combat AI; implementer verifies its usage (spawned by
  breach/roll attacks?) and documents the call.

## Verification

- Subscriber inventory diff: before/after list of `@EventBusSubscriber`
  tick handlers with justification for each survivor.
- GameTests unchanged + any new coverage for moved behavior.
- Spark profile (#11): global tick handlers no longer appear per entity.

## Proposed approach (non-binding)

Move each procedure's body into the owning entity's `baseTick()` (or a Goal);
delete the `@EventBusSubscriber` shell; for `RollParticleEntity` strip AI and
add `if (tickCount > lifetime) discard()`.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
