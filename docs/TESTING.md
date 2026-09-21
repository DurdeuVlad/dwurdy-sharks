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

`src/main/java/net/mcreator/sharks/gametest/BenssharksGameTests.java`
registers five behavioral tests (aggression gamerule on/off, filter-feeder
passivity, tamed-owner exclusion, and the tamed-follow land gate). Run them
with:

```powershell
.\gradlew.bat runGameTestServer
```

The run passes when the log reports `All 5 required tests passed`.

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
