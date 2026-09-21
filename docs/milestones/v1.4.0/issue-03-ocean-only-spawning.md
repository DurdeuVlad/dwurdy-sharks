## Intent

Players report sharks in rivers, swamps, lakes, and beaches — ecologically
wrong for the pack and a gameplay bug (e.g. bull sharks in inland waterways).
Two independent defects enable it: biome-modifier JSONs explicitly list
non-ocean biomes, and the Java spawn predicate checks only "two water blocks"
with no biome guard.

## Expectation

Under default configuration, natural wild spawns occur only in biomes tagged
`dwurdysharks:shark_spawning_oceans` (seeded from vanilla ocean biome tags).
A future JSON edit adding `river` cannot reintroduce inland sharks because the
Java predicate enforces the tag independently. Modded ocean biomes work by
adding them to the tag via datapack — no rebuild.

## Acceptance criteria

- [ ] Every `neoforge/biome_modifier/*.json` references only the ocean biome
      tag(s) — zero hardcoded `river`, `swamp`, `mangrove_swamp`, `beach`,
      `stony_shore`, `snowy_beach`, or other non-ocean biome IDs in spawn lists.
      (Bull confirmed: `river`, `swamp`, `mangrove_swamp`, `beach`,
      `stony_shore` present; audit all 21 files.)
- [ ] `data/dwurdysharks/tags/worldgen/biome/shark_spawning_oceans.json` exists
      and covers vanilla oceans incl. deep/frozen/lukewarm/warm/cold variants.
- [ ] Each entity's `RegisterSpawnPlacementsEvent` predicate additionally
      requires `world.getBiome(pos)` ∈ `shark_spawning_oceans` (shared helper,
      e.g. `SharkSpawnPlacements.isOceanWater(world, pos)`).
- [ ] GameTest or integration check: spawn predicate returns false in a river
      biome, true in an ocean biome, even though both have water.
- [ ] `/summon` and spawn eggs are unaffected (spawn placement governs natural
      spawns only).

## Context that code cannot infer

- The requirement is ocean-only *by default*; pack makers may add biomes via
  the tag. Non-ocean removal from JSON is the second layer, the predicate is
  the enforcement layer — both are required because the JSON layer alone was
  already misconfigured once.
- `LandShark` is intentionally terrestrial and exempt from the ocean
  restriction — verify its biome list separately rather than blanket-editing.
- Bull shark river lore aside, the decided product rule is ocean-only; if a
  future release wants river bull sharks it goes through the tag + a config
  flag, not hardcoded biomes.

## Scope

- Type: bug (spawn policy). Priority: P0. Milestone: 1.4.0.
- Labels: `bug`, `priority:p0`, `area:spawning`.
- Affected: `src/main/resources/data/dwurdysharks/neoforge/biome_modifier/*.json`
  (21 files), new tag JSON, per-entity `init(...)` predicates, new helper class.
- Assignee: unassigned.

## Non-goals

- No weight/group-size changes (issue #4).
- No `MobCategory` changes (issue #8).
- No spawn-cost modifiers (issue #4).

## Dependencies and open decisions

- Blocks: none; independent of issues #1/#2. Recommended to land before #4
  (same files region).
- Open decision: whether deep_ocean variants belong in the default tag for all
  species or a separate `shark_spawning_deep_oceans` tag for Megalodon/deep
  species — implementer may split tags if species geography calls for it;
  record the choice in the PR.

## Verification

- GameTest: mock or fixture `LevelReader` per biome — predicate false in
  `minecraft:river`, true in `minecraft:ocean`/`deep_ocean`.
- Datapack-level check: tag file loads (no JSON schema errors on boot);
  `/summon` still works anywhere.
- Manual/observational (release gate): no `dwurdysharks:*` natural spawns in
  river/swamp/lake on a test world with default config.

## Proposed approach (non-binding)

Point `biomes` in each modifier at `#dwurdysharks:shark_spawning_oceans`;
create the tag JSON including `#minecraft:is_ocean` (plus deep variants);
add `SharkSpawnPlacements` helper used in each entity's `init` predicate:
`isOceanBiome && water && waterAbove`.

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
