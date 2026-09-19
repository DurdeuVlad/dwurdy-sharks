# Contributing to Dwurdy Sharks

Dwurdy Sharks is a community maintenance fork of **Ben's Sharks** by
**benndevs / PaoloBen**. Contributions are welcome, especially fixes that keep
the fork compatible with existing worlds and modpacks.

## Before opening a pull request

- Keep the mod id `dwurdysharks` unchanged unless a migration plan is included.
- Preserve attribution to benndevs and do not imply upstream endorsement.
- Keep version-specific changes on the matching Minecraft branch.
- Do not claim support for a Minecraft version or loader until its build and
  smoke checks pass.

## Branches

- `master` is the primary maintained branch and currently tracks Minecraft
  1.21.1 on NeoForge.
- Version branches use the exact Minecraft version, such as `1.21.1`.
- Future branches may target Minecraft 1.12.x and newer only. Older versions
  are out of scope. A branch is created only when it contains a real port and
  a passing build; placeholder branches are intentionally avoided.
- Short-lived work uses `feat/*`, `fix/*`, or `docs/*` and should merge into
  the relevant current version branch.

## Local checks

Use Java 21 for the current branch:

```powershell
.\gradlew.bat clean build
```

The same command runs in CI. For behavior checks, follow
[`docs/TESTING.md`](docs/TESTING.md), including the wild-versus-tamed despawn
check before reporting a release-ready fix.

## Pull requests

Explain the affected Minecraft version, loader, and compatibility impact.
Include the commands you ran and call out anything that could not be tested.
