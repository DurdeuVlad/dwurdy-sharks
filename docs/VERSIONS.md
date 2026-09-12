# Version support and branch policy

## Current verified target

The only target currently verified by this repository is:

- Minecraft **1.21.1**
- **NeoForge 21.1.x**
- **GeckoLib 4.x**
- Mod ID: `benssharks`

This documentation does not claim that any other Minecraft version, loader, or launcher artifact has been built, tested, or published. In particular, there is no current claim of a Fabric, Forge, or older-version build.

## Support boundary

Future version listings and launcher artifacts must target **Minecraft 1.12.x or newer**. Versions below 1.12 are out of scope. A version may be advertised only after its branch has been built and tested; the policy is not a promise that every 1.12+ version is already available.

## Branch policy

The repository follows the versioned-branch shape used by the DwurdyVlad reference repositories:

- `master` is the integration/default branch.
- Each supported Minecraft target gets a version branch named with the Minecraft version, for example `1.21.1`.
- Version-specific fixes should land on the relevant version branch and be carried to `master` when they apply generally.
- A future target should not be represented by a branch, release, or launcher listing until it has a reproducible build and a matching verification pass.

The existing `master` and `1.21.1` branches describe the current repository layout; they are not evidence of support for unbuilt versions.

## Publishing checklist for a future target

Before listing another 1.12.x-or-newer target on a mod launcher:

1. Create or update the matching version branch.
2. Build the jar from that branch with the correct loader and dependencies.
3. Test installation and the relevant gameplay behavior on that Minecraft version.
4. Confirm that the mod ID and migration behavior are documented accurately.
5. Publish only the tested loader/version combination, with no implied support for neighboring versions.
