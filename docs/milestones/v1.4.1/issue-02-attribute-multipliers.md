# Issue — Attribute multipliers (speed, health, per-species damage)

## Intent

Discord feedback: servers want to slow or speed sharks and scale difficulty.
Today `MOVEMENT_SPEED`, `ATTACK_DAMAGE`, `MAX_HEALTH` are hardcoded per species
in `createAttributes()`. Add global multipliers plus per-species overrides,
applied consistently.

## Expectation

- `movement.sharkSpeedMultiplier` (double, default 1.0, range 0–100) scales
  every mod entity's `MOVEMENT_SPEED`.
- `movement.speciesMultiplier.<species>` (double, default 1.0) stacks with the
  global multiplier for that species.
- `damage.speciesMultiplier.<species>` (double, default 1.0) stacks with
  `sharkDamageMultiplier`; `megalodonDamage` remains an absolute base override
  applied before multipliers.
- `damage.speciesHealth.<species>` — absolute `MAX_HEALTH` override (0 =
  inherit base value), per species.
- Multipliers apply as **transient attribute modifiers with fixed
  `ResourceLocation` IDs** on `EntityJoinLevelEvent` — idempotent on rejoin,
  never stacking, never persisted into entity NBT.

## Acceptance criteria

- [ ] A single join listener applies speed/health/damage modifiers for every
      `DwurdySharksConfig.isModEntity` entity; modifier IDs live in one
      constants block.
- [ ] Speed multiplier measurable: at `sharkSpeedMultiplier = 2.0`, a spawned
      shark's `getAttributeValue(MOVEMENT_SPEED)` doubles vs. 1.0.
- [ ] Per-species override wins correctly: species key set to 0.5 halves that
      species only.
- [ ] Modifier re-application on rejoin does not accumulate (join twice →
      attribute identical).
- [ ] Megalodon armor-bypass and damage semantics unchanged.
- [ ] GameTests: speed multiplier applied; per-species override; idempotent
      rejoin.

## Context that code cannot infer

- Why not `createAttributes()`: the attribute supplier is built during
  common-setup, before SERVER config is loaded — reading config there yields
  defaults regardless of the file. Join-time modifiers are the correct hook.
- Transient (not permanent) modifiers: permanent modifiers persist to entity
  NBT, so a config change would leave stale modifiers on existing entities.
  Transient re-applies fresh each join; fixed ID prevents duplicates within a
  session.
- Species key naming: reuse the `[spawning.species]` keys (`axodile`,
  `bull_shark`, …) so one canonical name per entity across the file.

## Scope

- Type: enhancement. Priority: P0. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p0`, `area:config`, `area:entities`.
- Affected: `DwurdySharksConfig.java`, new join-listener procedure,
  `DwurdySharksGameTests.java`.
- Assignee: unassigned.
