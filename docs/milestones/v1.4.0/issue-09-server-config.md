## Intent

Admins currently cannot tune the mod without a rebuild: spawn enable/disable,
damage numbers (Megalodon `ATTACK_DAMAGE=20`, `MAX_HEALTH=150`,
`FOLLOW_RANGE=64`), and spawn rolls are all hardcoded. Modpack authors need a
server config; the incident showed how bad a hardcoded balance decision is in
production.

## Expectation

`DwurdySharksConfig` (NeoForge server config, `ModConfigSpec`) exposes, at
minimum:

- `spawningEnabled` (master switch)
- `oceanOnly` (guard toggle for issue #3's predicate)
- Per-species `enabled` — enforced at spawn-check time (works even though
  weights live in JSON)
- `sharkDamageMultiplier` (global) and `megalodonDamage` (specific)
- Documentation in the config comments + GAMERULES/config docs page.

Spawn weights/group sizes remain in biome-modifier JSON (datapack-override is
the vanilla-native mechanism — see open decision resolution below).

## Acceptance criteria

- [ ] Server config file generated at `config/dwurdysharks-server.toml` (or
      equivalent) with the keys above; defaults reproduce current behavior.
- [ ] `spawningEnabled=false` or `speciesEnabled=false` blocks that species'
      natural spawns at spawn-check time (hook into the #4 spawn guard).
- [ ] `oceanOnly=false` disables the biome-tag predicate added in #3
      (single source: the shared predicate reads the config).
- [ ] `sharkDamageMultiplier` multiplies shark attack damage (Megalodon uses
      `megalodonDamage` × multiplier or its own value — specify semantics in
      comments).
- [ ] Config reload on `/reload`/restart is honored for spawn checks and new
      damage applications.
- [ ] Docs: GAMERULES.md (or a CONFIG.md) updated with every key, default,
      and gamerule-vs-config boundary (gamerules = per-world runtime knobs
      like caps/aggression; config = global balance/toggles).

## Context that code cannot infer

- Decision (D4): per-species *weights* intentionally stay in JSON — NeoForge
  `add_spawns` reads datapack JSON and cannot consume mod config without a
  custom `BiomeModifier` codec. `enabled` flags are still config-driven
  because they gate at spawn-check time, not at spawn-list-build time.
- Megalodon balance today: `ATTACK_DAMAGE=20.0` (not insta-kill),
  `MAX_HEALTH=150`, `FOLLOW_RANGE=64`. Whether armor-piercing damage is
  desired is a product decision — default: no special armor bypass; expose
  `megalodonArmorBypass` boolean only if wanted (flag in PR for maintainer).
- Existing gamerule `aggressiveSharks` stays a gamerule (per-world); do not
  duplicate it in config.

## Scope

- Type: enhancement. Priority: P1. Milestone: 1.4.0.
- Labels: `enhancement`, `priority:p1`, `area:config`.
- Affected: new `DwurdySharksConfig` class, mod constructor registration,
  spawn guard (#4) + spawn predicate (#3) read points, entity damage paths,
  docs.
- Assignee: unassigned.

## Non-goals

- No custom `BiomeModifier` codec for config-driven weights (documented
  rejection — datapack overrides cover it).
- No client config. No migration of existing gamerules.

## Dependencies and open decisions

- Depends on #3 (predicate reads `oceanOnly`) and #4 (guard reads
  `spawningEnabled`/per-species flags) — implement last or wire read-points
  behind the same PRs; do not block #3/#4 on this.
- Open decision: `megalodonArmorBypass` — propose default false; maintainer
  confirms.

## Verification

- Config loads with defaults on dedicated server boot; editing a key changes
  behavior without rebuild.
- GameTest or runtime check: species disabled ⇒ no natural spawn; damage
  multiplier reflected in a hit.

## Proposed approach (non-binding)

`ModConfigSpec` builder in `DwurdySharksConfig` registered via
`context.registerConfig(ModConfig.Type.SERVER, SPEC)`; static accessors
`DwurdySharksConfig.sharksEnabled(species)` etc.; Alex's Mobs precedent for
`...SpawnWeight`/`...SpawnRolls` style keys (we keep weights in JSON, but the
per-species enabled pattern is the same).

## PR contract

PR body must include Intent, Expectation, Acceptance criteria with evidence,
Non-code context, Scope and non-goals, Verification and risk — explicit `None`
/`Not applicable`/`Unknown—blocked` where relevant.
