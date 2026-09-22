## Intent

Spawn `weight`/`minCount`/`maxCount` select and size groups — they are **not**
a population limit. The incident involved a ~2,000-shark population; nothing in
the current code prevents that. This issue adds density shaping at spawn time
plus a hard local cap so a server cannot be overrun, including via commands.

## Expectation

- Natural spawns thin out as local density rises (spawn-cost budget), rather
  than clustering unboundedly.
- A local cap refuses additional wild `dwurdysharks` spawns once the radius
  limit is reached; tamed, named, and player-placed (bucket) entities are
  exempt.
- Large sharks spawn mostly solo (`1–1`), schools stay grouped.
- When `enforceCapForManualSpawns` is on (default on), `/summon` and spawn
  eggs beyond the cap are refused at join-time; when off, they pass.

## Acceptance criteria

- [ ] `neoforge:add_spawn_costs` modifiers exist: a restrictive budget/charge
      for large predators and a looser one for ambient schooling fish.
- [ ] Gamerules (precedent: `DwurdySharksModGameRules.AGGRESSIVE_SHARKS`):
      `largeSharkLocalCap` (default 24 / 128-block radius),
      `ambientLocalCap` (default 64 / 128-block radius),
      `enforceCapForManualSpawns` (default true).
- [ ] Cap is evaluated only at spawn/join time — zero per-tick cost.
- [ ] Exempt from the cap: tamed entities, named entities, bucket-released
      entities (persistence-required flag where applicable).
- [ ] `minCount`/`maxCount` in biome modifiers: large sharks `1–1`
      (rarely `1–2`); Krill/Remora/PilotFish group sizes preserved.
- [ ] GameTests: (a) spawn N+1st wild shark inside a capped radius is refused;
      (b) tamed/named entity does not count toward the cap; (c) with
      `enforceCapForManualSpawns=false`, manual spawn exceeds the cap; (d) cap
      radius/counters come from gamerules, not constants.

## Context that code cannot infer

- Why two mechanisms: `add_spawn_costs` is NeoForge's documented density
  shaping (charge vs energy_budget field) — it discourages clustering but is
  *not* a hard cap; the gamerule cap is the hard bound requested after the
  incident.
- `/summon`, spawn eggs, and natural spawn placement are distinct entry paths;
  vanilla mobcaps alone do not protect against manual spam.
- Decision (D2): caps live in gamerules, not the config file — per-world,
  in-game editable, matching the existing `aggressiveSharks` pattern.
- Decision (D5): enforce via `MobSpawnEvent.FinalizeSpawn`/`CheckSpawn` for
  natural spawns and `EntityJoinLevelEvent` with spawn-reason filtering for
  manual spawns.
- Radius and defaults (24/64 per 128 blocks) are starting points, server-
  tunable via the gamerules.

## Scope

- Type: enhancement. Priority: P0. Milestone: 1.4.0.
- Labels: `enhancement`, `priority:p0`, `area:spawning`.
- Affected: biome-modifier JSONs (spawn costs, group sizes),
  `DwurdySharksModGameRules`, a spawn-guard class (new), entity persistence flags.
- Assignee: unassigned.

## Non-goals

- No biome allow-list changes (issue #3 — same files, sequencing only).
- No species weight edits beyond group sizes stated above (config/datapack
  story is issue #9).
- No culling/killing of existing entities — cap affects new spawns only.

## Dependencies and open decisions

- Soft dependency on #3 (same JSON files; land after or rebase).
- Open decision: exact charge/budget numbers per species group — propose
  values in the PR with reasoning (e.g. predator charge ≫ ambient charge);
  defaults above are the baseline.

## Verification

- GameTests (a)–(d) above.
- Boot check: spawn-cost modifiers parse (no datapack errors).
- Stress check (feeds #11): attempting to force 2,000 entities with cap on
  plateaus at the cap; with cap off on a stress server it degrades gracefully.

## Proposed approach (non-binding)

New `SharkSpawnGuard` with a single `EntityJoinLevelEvent` handler that
counts matching `dwurdysharks` entities in an AABB at the join position,
applies exemptions, and calls `event.setCanceled(true)` when over cap; natural
spawns gated additionally via the placement predicate or `FinalizeSpawn` so
the spawn attempt is skipped rather than spawn-then-despawned.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
