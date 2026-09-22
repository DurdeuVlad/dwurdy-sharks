# Issue 09 — `sharksAttackBoats`: large sharks ram occupied boats

## Intent

Give server owners a config toggle that turns large sharks into water-border
deterrents: when enabled, large sharks actively seek out boats carrying
passengers and ram them until the boat breaks, dumping occupants into the
water. Combined with persistent/named sharks, this is the "moat" pattern —
place sharks along a canal or border and they punish boat crossings.

## Expected behavior

- New config key `behavior.sharksAttackBoats` (boolean, default `false`).
- When enabled, any non-tamed entity in the `large_sharks` entity-type tag
  that is in water:
  - scans every 20 ticks (staggered by entity id) for `Boat`/`ChestBoat`
    entities **with at least one passenger** within its effective
    `FOLLOW_RANGE` (so `aggroFollowRangeMultiplier` applies);
  - paths toward the nearest occupied boat;
  - when within ~2.5 blocks, damages the boat via
    `boat.hurt(damageSources().mobAttack(mob), max(2, attackDamage * 0.25))`
    — roughly 2–3 rams to break a vanilla boat.
- Empty boats are ignored (harbor decorations are not targets).
- Tamed sharks never attack boats. Named/persistent sharks DO — deliberate
  border markers are the primary use case.
- Independent of the `aggressiveSharks` gamerule — a server can run
  "harmless to swimmers, hostile to boats" borders.

## Non-goals

- No gamerule mirror in this issue (boolean gamerules cannot express the
  `-1 = inherit config` sentinel; config-only matches `itemEatingEnabled`).
- No damage to passengers directly — the boat breaks, normal water
  aggression rules take over afterwards.
- No changes to spawn behavior.

## Acceptance criteria

- `sharksAttackBoats=false` (default): occupied boats are never damaged by
  sharks.
- `true`: a large shark next to an occupied boat damages it via the direct
  procedure path; an empty boat is untouched; a tamed shark ignores an
  occupied boat.
- GameTest `config` batch covers: enabled→boat damaged, empty boat ignored,
  tamed shark ignores occupied boat.
- `docs/CONFIG.md` documents the key; `verify-config` docs-coverage guard
  still passes.

## Affected areas

- `DwurdySharksConfig` — new `behavior` key.
- New `SharkAttackBoatProcedure` — per-entity `baseTick` call in the 18
  `large_sharks` entity classes (same pattern as `EatDroppedItemProcedure`).
- `DwurdySharksGameTests` — new `config` batch test (direct-call pattern).
- `docs/CONFIG.md`.

## Verification surface

- GameTest `configboatsattackoccupied` (direct procedure calls, deterministic).
- Dedicated server: enable in TOML, spawn bull shark + boat + passenger,
  observe ram + break.
