# Testing Dwurdy Sharks

## Build gate

The current maintained target is Minecraft 1.21.1 on NeoForge 21.1.x with
Java 21. Run:

```powershell
.\gradlew.bat clean build
```

Run the static regression guards as well:

```powershell
pwsh -NoProfile -File tools/verify-persistence.ps1
pwsh -NoProfile -File tools/verify-spawn-procedures.ps1
pwsh -NoProfile -File tools/verify-dryout.ps1
pwsh -NoProfile -File tools/verify-ocean-spawns.ps1
pwsh -NoProfile -File tools/verify-spawn-caps.ps1
pwsh -NoProfile -File tools/verify-ai-goals.ps1
pwsh -NoProfile -File tools/verify-dimensions.ps1
pwsh -NoProfile -File tools/verify-hot-path.ps1
pwsh -NoProfile -File tools/verify-config.ps1
```

The build must finish successfully and produce a jar in `build/libs/`. A
passing Gradle build is a compilation and packaging check, not a claim of
complete in-game coverage.

## GameTest suite

`src/main/java/net/mcreator/sharks/gametest/DwurdySharksGameTests.java`
registers the full regression + stress suite. Run it with:

```powershell
.\gradlew.bat runGameTestServer
```

Each test runs in its own GameTest batch, so scenarios do not execute
concurrently in the same world. The run passes when the log reports
`All 17 required tests passed` with no `(optional) ... failed` warnings.

Required tests:

| Batch | Covers |
|---|---|
| `aggressive_off` / `aggressive_on` / `aggressive_whaleshark` / `aggressive_tamed` | `dwurdySharksAggressiveSharks` gamerule gating; filter feeders never target; tamed sharks never target owner |
| `tamed_follow` | tamed sharks do not path to an owner standing on land |
| `spawn_biomes` | `SpawnPlacements.checkSpawnRules` rejects river/swamp/plains, accepts ocean biomes; every biome carrying mod spawn data is in `dwurdysharks:shark_spawning_oceans` |
| `dryout_once` | dryout effect applies exactly once after the 600-tick delay and counts down; `workQueue` stays flat while beached |
| `cap` | natural spawns plateau at exactly `dwurdySharksLargeSharkLocalCap=8` out of 40 attempts; cancelled spawns never join the world |
| `cap_exemptions` | tamed, named, and persistence-required sharks bypass the cap; a 9th wild shark is rejected |
| `cap_abuse` | 2,000-attempt spawn storm plateaus at 8, post-storm P95 < 50 ms, no workQueue growth |
| `cap_disabled` | `dwurdySharksLargeSharkLocalCap=0` disables enforcement, 150/150 spawn, heap is reclaimed after despawn+GC |
| `despawn` | wild shark despawns past 128 blocks; tamed shark persists |

Optional (timed) stress profiles — each runs at least
`-Dsharks.stress.seconds` wall-clock seconds (default 300):

| Batch | Profile |
|---|---|
| `stress_baseline` | empty-world reference; asserts zero mod entities after purge and P95 < 50 ms |
| `stress_population_100` | 100 mixed sharks (prey species included) with per-sample top-up, survival player present, `dwurdySharksAggressiveSharks=true` |
| `stress_population_500` | same at target 500; asserts the sustained trough stays >= target/4 |
| `stress_dryout_beached` | 60 beached sharks per 1,200 ticks; asserts queue growth stays linear in live beached count (`start + 128 + 8 per beached shark`) |
| `stress_item_eat` | 70 filter feeders + 30 item drops per 600 ticks, top-up maintained |

Every stress sample logs spawned/alive counts, `avg tick`, and `p95 tick`
(computed from `MinecraftServer.getTickTimesNanos()`). The release budget is
**P95 tick time < 50 ms** on the target host.

### Knobs

| System property | Default | Purpose |
|---|---|---|
| `sharks.stress.seconds` | `300` | wall-clock seconds per timed profile; **never set below 300 for gate evidence** |
| `sharks.stress.abuseAttempts` | `2000` | spawn attempts in `cap_abuse` |
| `sharks.stress.capDisabledCount` | `150` | population in `cap_disabled` |
| `sharks.stress.population` | `100` | target for `stress_population_100` |
| `sharks.stress.population.500` | `500` | target for `stress_population_500` |

Pass them to the forked JVM via `JAVA_TOOL_OPTIONS`, e.g.
`$env:JAVA_TOOL_OPTIONS="-Dsharks.stress.seconds=15"` for a fast smoke pass
(not valid as gate evidence).

### Manual profiling (Spark)

The automated suite measures tick times through the server API rather than an
external profiler. This is a **documented deviation** from the original
request to profile with Spark: Spark is not a test-runtime dependency, so the
GameTest server cannot load it. To collect Spark evidence on a real server:

1. Build the jar (`.\gradlew.bat build`) and install it plus GeckoLib 4.7.x and
   [spark](https://spark.lucko.me/) on a dedicated test server.
2. Run `/spark tps` and `/spark profiler start --timeout 300` under each load
   profile above, then compare spark's MSPT/P95 with the GameTest numbers.

The vanilla GameTest server also writes a Java Flight Recorder capture under
`runs/gameTestServer/` for offline analysis.

For a dedicated-server smoke boot, run `./gradlew runServer --no-daemon` (or
`.\gradlew.bat runServer --no-daemon` on Windows), confirm the log reaches
`Done`, then stop the test server. This confirms that NeoForge can discover the
mod and its required GeckoLib dependency on the target version.

## Persistent-shark regression check

Run a dedicated test world with the built jar and GeckoLib 4.x installed.

1. Spawn a wild large shark with its spawn egg in a safe ocean test area.
2. Travel far enough away for the normal mob-despawn distance and wait for
   the normal despawn window. The wild shark should be eligible for despawn.
3. Tame another tamable shark using its normal food interaction, leave the
   area, return after the same interval, and confirm the tamed shark remains.
4. Repeat the first check after a failed taming attempt. A failed attempt must
   not turn a wild shark into a permanently persistent entity.

The regression is fixed in the entity persistence paths. As of 1.3.0 the mod
id is `dwurdysharks` (previously `benssharks`), so worlds saved by older
versions contain `benssharks:*` identifiers that this build does not resolve —
test upgrades only on a backup copy.

## Release evidence

Record the Minecraft version, NeoForge version, Java version, GeckoLib version,
build command, artifact name, and the result of the wild/tamed checks in the
release notes or pull request. Do not publish an unexecuted behavior check as
passing.
