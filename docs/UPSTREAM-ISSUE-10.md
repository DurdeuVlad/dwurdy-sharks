# Upstream issue #10 audit: "Tried to add entity benssharks:shoal but it was
marked as removed already"

## Verdict: not applicable to this fork's codebase

The warning comes from `benssharks:shoal`, a group-spawn helper entity that
was introduced in upstream Ben's Sharks **1.3.0** (upstream `ShoalEntity`
spawns a school of sardines in `finalizeSpawn` and then calls
`entity.discard()` on itself; the self-discard inside the spawn path is what
produces the "marked as removed already" log line when chunk bookkeeping
races the removal).

This fork is based on the 1.2.6/1.2.7 codebase. It contains **no
`ShoalEntity`, no `benssharks:sardine`, and no shoal spawn procedure** — the
entire mechanic that produces the warning does not exist here, so the
warning cannot be emitted by this mod build.

## What was audited

- `src/main/java/net/mcreator/sharks/init/DwurdySharksModEntities.java` — the
  entity registry has no `SHOAL`/`SARDINE` entry (25 entities total).
- `grep -ri "shoal|sardine" src/` — no hits.
- Every `addFreshEntity` / `EntityType.spawn` call site (bucket-release
  procedures, `EntityDiesWithParasiteProcedure`, projectile/arrow spawn
  paths, `GreaterAxodileOnEntityTickUpdateProcedure`) constructs a **fresh**
  entity instance — no site re-adds a stored or previously-removed entity.
- `RollParticleOnEntityTickUpdateProcedure` calls `entity.discard()` only on
  the ticking entity itself (self-removal, which is legal), never re-adds
  it.
- `DwurdySharksModVariables` stores scalars only; no entity references are
  persisted and re-added later.

Because no code path in this fork adds an entity that could already be
removed, there is nothing to guard. Adding upstream's shoal feature — or
speculative `isRemoved()` checks on freshly constructed entities — would be
dead code, so this PR is intentionally documentation-only.

If the shoal mechanic is ever ported (it is part of the explicitly
out-of-scope upstream 1.3.0 port, issue #12), the port should spawn the
member fish first and discard the shoal entity **without** re-adding it,
and should re-run the chunk load/unload check described in the issue.
