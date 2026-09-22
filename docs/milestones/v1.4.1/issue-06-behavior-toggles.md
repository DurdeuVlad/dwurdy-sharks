# Issue — Behavior toggles

## Intent

A small set of binary behavior knobs servers have asked for implicitly when
discussing caps and difficulty. Each maps to an existing procedure — config
adds the switch, no new behavior is invented here.

## Expectation

- `behavior.itemEatingEnabled` (bool, default true) — gates
  `EatDroppedItemProcedure`/item-eat goals so servers can stop sharks
  hoovering dropped loot.
- `capAppliesToManualSpawns` stays the single gamerule
  (`enforceCapForManualSpawns`) governing **both** local and global caps —
  do not add a second switch; document the unified semantic.
- `behavior.aggroFollowRangeMultiplier` (double, default 1.0, range 0.1–10) —
  scales `FOLLOW_RANGE` via the issue-2 modifier path, controlling how far
  aggressive sharks acquire targets.

## Acceptance criteria

- [ ] With `itemEatingEnabled = false`, a dropped food item near a wild shark
      is never consumed; `true` restores 1.4.0 behavior.
- [ ] `stress_item_eat` still passes at default (true).
- [ ] Aggro range multiplier measurably scales `FOLLOW_RANGE`; combined with
      `aggressiveSharks` gamerule unchanged.
- [ ] One `enforceCapForManualSpawns` gamerule documented as governing local
      AND global caps (docs sync in issue 7).

## Context that code cannot infer

- Why not per-behavior gamerules: these are server-policy knobs, not
  per-world gameplay state — config is the right home; `aggressiveSharks`
  remains a gamerule because 1.4.0 shipped it as one and worlds depend on it.
- `stress_item_eat` depends on item-eating being enabled by default — the
  GameTest sets no config; default `true` keeps it green.

## Scope

- Type: enhancement. Priority: P2. Milestone: 1.4.1.
- Labels: `enhancement`, `priority:p2`, `area:config`, `area:entities`.
- Affected: `DwurdySharksConfig.java`, `EatDroppedItemProcedure`/goal
  wiring, join-listener (aggro range), `DwurdySharksGameTests.java`.
- Assignee: unassigned.
