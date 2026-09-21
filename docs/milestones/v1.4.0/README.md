# Dwurdy Sharks 1.4.0 — Milestone plan

Outcome boundary for release **1.4.0** of `dwurdysharks` (MC 1.21.1, NeoForge
21.1.248). Baseline audited at `master` @ `5c860ef`.

This plan was produced from a static audit of the repository plus the incident
report (CPU escalation, heap growth 15→17 GB, sharks spawning in rivers/lakes,
~2000-entity stress event). The Spark profile referenced in the incident was not
readable during the audit; every defect below is verified directly in code, and
the release-gate issue requires fresh Spark evidence rather than trusting static
analysis.

## Contract

- **Goal:** ship 1.4.0 such that a production server cannot be degraded by
  sharks — no per-tick queue/allocation storms, no out-of-ocean natural spawns,
  bounded local density, per-entity tick cost reduced by an order of magnitude,
  and all of it proven by automated tests plus Spark CPU/alloc profiles.
- **System:** Java/NeoForge mod, MCreator-generated sources, biome-modifier
  JSON spawns, GameTest suite (`BenssharksGameTests`, 5 tests, `pool` template).
- **Constraints:** keep `finalizeSpawn` behavior (guardian aggro) working; keep
  tamed/named sharks persistent while wild ones despawn; modded ocean biomes
  must be supported via tags, not hardcoded biome lists; no invented dates,
  owners, or estimates.
- **Evaluation:** each issue carries its own acceptance criteria and
  verification surface; the milestone closes only when the release-gate issue
  passes on a real server.

## Verified repository facts (evidence, not assumptions)

| # | Fact | Where |
|---|------|-------|
| F1 | 14 `*OnInitialEntitySpawnProcedure` classes are `@EventBusSubscriber`s on `EntityTickEvent.Pre`, running `getEntitiesOfClass(Entity.class, AABB.inflate(25))` + full sort **every tick** | e.g. `BullSharkOnInitialEntitySpawnProcedure.java:19-41`. Affected: Basking, Blue, Bonnethead, Bull, Cookiecutter, Greenland, LandShark, Mako, Megalodon, Nurse, Remora, Shrak, Tiger, Whitetip |
| F2 | `BenssharksMod.workQueue` is a `ConcurrentLinkedQueue` fully iterated every server tick | `BenssharksMod.java:43,82-93` |
| F3 | 19 `*OnEntityTickUpdateProcedure` classes call `queueServerWork(300-600, …)` **every tick** while the shark is out of water → ~600 queued lambdas per beached shark, each pinning the entity | `BullSharkOnEntityTickUpdateProcedure.java:14-19`; DRYOUT grep: 19 files |
| F4 | `*EntityIsHurtProcedure` `queueServerWork(600)` calls are per-hit (bounded), mostly redundant effect cleanup | `TigerSharkEntityIsHurtProcedure.java:40-48` |
| F5 | Bull shark spawn JSON lists `river`, `swamp`, `mangrove_swamp`, `beach`, `stony_shore`; other species have similar non-ocean entries (per-file cleanup required) | `bull_shark_biome_modifier.json`; 21 biome modifier files total |
| F6 | Java spawn predicate checks only "water + water above" — no biome guard | `BullSharkEntity.init:327-335`, same pattern per entity |
| F7 | `BonnetheadShark`, `BlacktipReefShark`, `Barracuda` registered `WATER_AMBIENT`; Krill, Remora, PilotFish correctly ambient | `BenssharksModEntities.java:97,121,234` |
| F8 | Goal counts: Axodile 86 (9+77 targets), GreaterAxodile 52, Bull 47, Mako 44, `RollParticleEntity` 47 incl. 44 target goals | `*Entity.java` `goalSelector`/`targetSelector` |
| F9 | `LookAtPlayerGoal` ranges 128F on Tiger/Shrak/Nurse/Mako/Bull/Blue, 64F Megalodon, **256F** Axodile | entity `registerGoals` |
| F10 | `refreshDimensions()` called in `baseTick()` of all 24 entity classes despite static dimensions | e.g. `BullSharkEntity.java:298` |
| F11 | Repeated per-tick AABB queries: `EatKrillProcedure` 18 calls, `BarracudaSprintProcedure` 19, `EatDroppedItemProcedure` ~9, each with full sort | procedures dir |
| F12 | No config class, no `data/dwurdysharks/tags` dir, no GitHub issues/milestones | repo |
| F13 | Existing precedent for server-tunable behavior: `BenssharksModGameRules.AGGRESSIVE_SHARKS` gamerule + 5 GameTests | `BenssharksModGameRules`, `gametest/` |

## Decisions taken (reversible, documented)

- **D1 — one GitHub milestone `1.4.0`, issues ordered by dependency.** Priority
  labels (`priority:p0/p1/p2`) carry sequencing; GitHub milestones map to
  releases, not phases.
- **D2 — caps and spawn toggles use gamerules** (`BenssharksModGameRules`),
  matching the existing `aggressiveSharks` precedent and giving per-world,
  in-game control. `DwurdySharksConfig` (issue #9) holds global toggles/damage
  numbers only — no double source of truth.
- **D3 — biome allow-list is a tag, `dwurdysharks:shark_spawning_oceans`,**
  seeded from `#minecraft:is_ocean` (and deep/cold variants as needed), so
  datapacks and modded biomes extend it without a rebuild.
- **D4 — spawn weights stay in biome-modifier JSON** (datapack-overridable —
  the vanilla-native mechanism). Config cannot feed `neoforge:add_spawns` at
  runtime without a custom `BiomeModifier` codec; rejected as out of scope for
  1.4.0 (documented in issue #9).
- **D5 — cap enforcement hook:** `MobSpawnEvent.FinalizeSpawn`/`CheckSpawn` for
  natural spawns; `EntityJoinLevelEvent` + spawn-reason filter for manual
  spawns when `enforceSafetyCapForManualSpawns` is on.

## Unknowns / needs-input

- Supported-population target for the P95 < 50 ms/tick criterion is hardware-
  dependent; issue #11 must record the actual server hardware + chosen target.
- Spark profile URL from the incident is unreadable; fresh profiles are
  required by #11.
- Megalodon `armorBypass` is a product decision left open in #9.
- Whether `enforceCapForManualSpawns` defaults `true` or `false` is proposed
  `true` (production-protective) in #4; admin override via gamerule.

## Issue order

P0 (block merge to release):

1. **#12** Stop initial-spawn procedures running every tick — `issue-01-initial-spawn-tick-subscribers.md`
2. **#13** Replace per-tick dryout `queueServerWork` with a dryout counter — `issue-02-dryout-queue-storm.md`
3. **#14** Restrict wild spawns to ocean biomes (JSON + tag + predicate) — `issue-03-ocean-only-spawning.md`
4. **#15** Spawn-cost density shaping + local population cap (+ group sizes) — `issue-04-density-cap.md`

P1:

5. **#16** Tag-based target goals (collapse 30–77 target goals → ≤8 per species) — `issue-05-tag-based-ai-goals.md`
6. **#17** Remove per-tick `refreshDimensions()` — `issue-06-refresh-dimensions.md`
7. **#18** Single-pass, throttled hot-path queries — `issue-07-hot-path-queries.md`
8. **#19** Reclassify Blacktip/Bonnethead/Barracuda → `WATER_CREATURE` — `issue-08-water-creature-category.md`
9. **#20** `DwurdySharksConfig` server config — `issue-09-server-config.md`

P2:

10. **#21** Retire global `EntityTickEvent.Pre` pattern; fix `RollParticleEntity` — `issue-10-global-event-cleanup.md`

Release gate:

11. **#22** 1.4.0 release gate — Spark CPU/alloc profiles, stress matrix,
    GameTest additions, version bump, release notes — `issue-11-release-gate.md`

## PR contract (applies to every issue)

Every PR body must repeat: **Intent**, **Expectation**, **Acceptance criteria**
(with evidence/status), **Non-code context**, **Scope and non-goals**,
**Verification and risk** — using `None`, `Not applicable`, or
`Unknown—blocked` explicitly where needed. A link to the issue alone is not
sufficient.
