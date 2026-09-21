## Intent

Every shark currently registers one `NearestAttackableTargetGoal` **per prey
class** — Axodile has 77 target goals (86 total), GreaterAxodile 52, Bull 47,
Mako 44. Each goal is a candidate scan. This is the dominant per-entity AI cost
after the tick-subscriber bugs are removed. The established pattern (Alex's
Mobs does exactly this) is one goal per *role* with an entity-tag predicate.

## Expectation

Each predator registers a small, constant number of goals (~5–8): swim/wander,
melee, hurt-by, prey-target (tag predicate), conditional-player, avoid-threat,
plus species-specific behavior. `LookAtPlayerGoal` distances drop to ≤32F for
normal fauna (Megalodon may keep a distinct documented range). Behavior
visible to players is unchanged: same prey are hunted, same rules respected.

## Acceptance criteria

- [ ] Entity-type tags exist: `dwurdysharks:large_shark_prey`,
      `dwurdysharks:small_shark_prey`, `dwurdysharks:shark_threats` (add others
      only if a species genuinely needs them) — populated via tag JSONs, not
      code lists.
- [ ] Per-species total `goalSelector`+`targetSelector` registrations: Bull ≤8,
      Axodile ≤8, GreaterAxodile ≤8, Mako ≤8; every other species ≤12.
- [ ] `LookAtPlayerGoal`/`LookAtEntityGoal` ranges ≤32F except a single
      documented exception (Megalodon); the 128F/256F values are gone.
      (Includes collapsing the Cookiecutter/PilotFish/Remora "look at each
      shark species" lists into a tag predicate.)
- [ ] Existing GameTests still pass unchanged: `aggressiveSharks` on/off,
      filter-feeder exemption, tamed-owner exemption.
- [ ] New/updated GameTest: shark still acquires a valid prey entity from the
      prey tag (behavior preserved, not just fewer goals).

## Context that code cannot infer

- `aggressiveSharks` gamerule semantics must be preserved — conditional player
  targeting checks it today (tests depend on it).
- Tamed sharks must never target their owner (existing GameTest).
- Cookiecutter's per-shark `LookAtPlayerGoal(..., 6.0F)` list and Remora/
  PilotFish's identical pattern are social/attach behaviors — replace with a
  tag-based look goal rather than deleting the behavior.
- `FOLLOW_RANGE` attributes should stay coherent with new targeting ranges;
  note mismatches in the PR rather than silently changing balance.

## Scope

- Type: enhancement (performance). Priority: P1. Milestone: 1.4.0.
- Labels: `enhancement`, `priority:p1`, `area:ai`, `area:performance`.
- Affected: every `*Entity.java` `registerGoals`, new tag JSONs under
  `data/dwurdysharks/tags/entity_type/`, predicate helper.
- Assignee: unassigned.

## Non-goals

- No new prey species or hunting-behavior redesign — same targets, cheaper
  dispatch.
- No global tick-event cleanup (issue #10) beyond what the goal collapse needs.
- No `RollParticleEntity` work (issue #10).

## Dependencies and open decisions

- Ordering: land after #1/#2 (P0 fixes) to keep profiling attribution clean;
  no hard code dependency.
- Open decision: exact tag membership per species (e.g. is Turtle large or
  small prey) — derive from the current per-class goal lists; record the
  mapping table in the PR.

## Verification

- `grep -c "targetSelector.addGoal"` per entity — before/after table in PR.
- GameTests listed above.
- Spark CPU profile (#11): goal-scan share of tick time drops; report
  before/after on the same population.

## Proposed approach (non-binding)

`TargetTagGoal extends NearestAttackableTargetGoal<LivingEntity>` taking a
`TagKey<EntityType<?>>`; register one per role. Alex's Mobs precedent:
single target goal on `LivingEntity.class` with a tag-derived predicate.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
