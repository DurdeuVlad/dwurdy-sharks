## Intent

Server operators are seeing CPU climb linearly with shark count. Root cause:
"initial spawn" procedures — meant to run once — are also subscribed to
`EntityTickEvent.Pre`, so every shark re-runs a 25-block-radius entity query +
sort 20 times per second. At 2,000 sharks that is ~40,000 sorted AABB queries
per second before any AI work runs.

## Expectation

Each `*OnInitialEntitySpawnProcedure` executes exactly once per entity, from
`finalizeSpawn(...)`. No initial-spawn class subscribes to a tick event. The
guardian/elder-guardian aggro behavior still happens at spawn time.

## Acceptance criteria

- [ ] No `*OnInitialEntitySpawnProcedure` file contains `@EventBusSubscriber`,
      `@SubscribeEvent`, or `EntityTickEvent` — verified by a static gate
      (test or CI grep) that fails the build if the pattern returns.
- [ ] `finalizeSpawn` still calls each species' spawn procedure once (behavior
      unchanged at spawn).
- [ ] The 14 affected classes (Basking, Blue, Bonnethead, Bull, Cookiecutter,
      Greenland, LandShark, Mako, Megalodon, Nurse, Remora, Shrak, Tiger,
      Whitetip) no longer appear in post-spawn CPU stacks.
- [ ] A regression check (GameTest or unit) confirms a freshly spawned bull
      shark still triggers the guardian-aggro pass exactly once.

## Context that code cannot infer

- The double-wiring is an MCreator artifact: the procedure is *both* invoked
  from `finalizeSpawn` and registered on the global tick bus. Removing the
  subscriber is safe; the `finalizeSpawn` call is the intended path.
- Why now: this is the single largest confirmed CPU defect; at scale it alone
  can saturate the server thread.
- Verified at `BullSharkOnInitialEntitySpawnProcedure.java:19-41`:
  `getEntitiesOfClass(Entity.class, AABB.inflate(25)).stream().sorted(...)`
  inside `onEntityTick(Pre)`.

## Scope

- Type: bug (performance). Priority: P0. Milestone: 1.4.0.
- Labels: `bug`, `priority:p0`, `area:performance`.
- Affected: 14 files under `net.mcreator.sharks.procedures.*OnInitialEntitySpawnProcedure`.
- Assignee: unassigned.

## Non-goals

- No change to *what* the spawn procedure does (guardian aggro logic stays).
- No removal of `queueServerWork` or other tick subscribers (separate issues).
- The 7 initial-spawn classes that do **not** subscribe (Axodile, Barracuda,
  BlacktipReefShark, GreaterAxodile, LemonShark, PilotFish, WhaleShark) need no
  changes beyond the static gate covering them.

## Dependencies and open decisions

- Blocks: none. Recommended first issue to land.
- Open decision: where the static gate lives — JUnit test scanning sources,
  or a CI grep step. Either satisfies the criterion; pick whichever the repo's
  build already supports.

## Verification

- Static gate output: zero matches for `EntityTickEvent`/`@SubscribeEvent` in
  `*OnInitialEntitySpawnProcedure.java`.
- GameTest: spawn bull shark adjacent to a Guardian; assert Guardian targets
  the shark after spawn, and (via counter/log assertion) that the procedure ran
  once, not per tick.
- Post-fix Spark CPU profile (release gate, issue #11): these classes absent
  from hot stacks after spawn tick.

## Proposed approach (non-binding)

Delete the `@EventBusSubscriber` annotation, `onEntityTick` method, and tick
imports from each of the 14 classes; keep `execute(...)` public static so
`finalizeSpawn` continues to call it. Add a `SourceConventionTest` (or CI step)
asserting the pattern is absent.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
