## Intent

1.4.0 must not ship on "server started and seems fine". The incident showed
static claims don't equal fixed production behavior — this issue defines the
measurable gate that blocks the release, and packages the release itself.

## Expectation

All prior issues merged; automated regression coverage exists for each defect
class; standardized Spark profiles demonstrate the fixes on a real server;
version bumped to 1.4.0 with release notes.

## Acceptance criteria

### Automated regression (in CI / GameTest)

- [ ] Static gates from #1/#2/#6/#7 in place and green.
- [ ] New GameTests: spawn-biome rejection (#3), dryout-once + queue-flat (#2),
      cap enforcement + exemptions (#4), preserved targeting (#5).
- [ ] Wild sharks despawn; tamed sharks persist (persistence test).

### Stress verification on a real server (Spark)

Scenarios, each with `/spark profiler start --timeout 120` (CPU) **and**
`/spark profiler start --alloc --alloc-live-only --timeout 120` plus
`/spark heapsummary` before/after population and after cleanup:

- [ ] **Baseline**: no sharks — establishes reference MSPT/heap.
- [ ] **100 sharks** and **500 sharks**: P95 tick < 50 ms on the target
      hardware (record hardware + exact numbers in the issue).
- [ ] **2,000-entity abuse attempt with cap enabled**: population plateaus at
      the cap; server stays responsive (proves the guard, not a promise that
      2,000 full-AI sharks are free).
- [ ] **Cap disabled on a stress server**: graceful degradation + stable memory
      after despawn — no unbounded `workQueue` growth, live instances of
      `dwurdysharks:*`/`Tuple`/queue nodes return to baseline after GC.
- [ ] `*OnInitialEntitySpawnProcedure` absent from post-spawn hot stacks.
- [ ] `workQueue` size uncorrelated with beached-shark count.
- [ ] Zero natural `dwurdysharks:*` spawns in river/swamp/lake/non-ocean
      biomes under default config.

### Packaging

- [ ] `mod_version` and `neoforge.mods.toml` → `1.4.0`; release notes written
      (`docs/releases/v1.4.0-mc1.21.1.md` per repo convention) including the
      profile links and the population the release is certified for.
- [ ] CURSEFORGE/README updated if behavior-visible changes warrant it.

## Context that code cannot infer

- The incident's Spark link was unreadable during audit — this issue produces
  the replacement evidence. Do not cite the old profile as proof.
- JVM heap growing 15→17 GB after GC is not by itself a leak — heap need not
  return to the OS; the gate therefore compares **live heap after GC** and
  allocation profiles, not RSS.
- "Supported population" is defined *by this issue's measurements* on the
  target hardware — state the number in release notes.

## Scope

- Type: release/chore. Priority: P0 (blocks release). Milestone: 1.4.0.
- Labels: `priority:p0`, `area:release`, `documentation`.
- Affected: tests, `gradle.properties`, `neoforge.mods.toml`, docs/releases.
- Assignee: unassigned (requires access to the production-like server).

## Non-goals

- No new features. No fix work — failures route back to their source issue.

## Dependencies and open decisions

- Blocked by #1–#10 (all must merge first).
- Needs input: target server hardware + chosen supported-population number;
  who runs the Spark profiles.

## Verification

This issue *is* the verification surface. Close only with linked Spark
profile URLs (CPU + alloc per scenario), heapsummary outputs, CI gate results,
and the shipped version bump.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
