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
  that is waterborne:
  - every tick: checks for occupied boats already within ~2.5 blocks and
    rams them — `boat.hurt(mobAttack, max(2, attackDamage * 0.25))`;
  - every 20 ticks (staggered by entity id): wide scan within effective
    `FOLLOW_RANGE` (so `aggroFollowRangeMultiplier` applies) and paths
    toward the nearest occupied boat.
- Why two cadences: vanilla boat damage decays 1/tick, so hits spaced 20
  ticks apart can never accumulate the >40 damage needed to break a boat.
  The every-tick in-reach ram breaks a boat in ~3 hits; the staggered wide
  scan keeps the expensive query cheap.
- "Waterborne" = `isInWaterOrBubble()` OR water within ~1 block under the
  bounding box — ramming a floating boat puts the shark's box right at the
  waterline, where the plain water check reads false (found in live test).
- Empty boats are ignored (harbor decorations are not targets).
- Tamed sharks never attack boats. Named/persistent sharks DO — deliberate
  border markers are the primary use case.
- Independent of the `dwurdySharksAggressiveSharks` gamerule — a server can run
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
