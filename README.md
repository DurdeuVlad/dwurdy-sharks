# Dwurdy Sharks

A community maintenance fork of **[Ben's Sharks](https://modrinth.com/mod/bens-sharks)**
(originally by **benndevs** / PaoloBen) for Minecraft 1.21.1 + NeoForge.

All credit for the original mod — every model, texture, sound, entity and
mechanic — goes to benndevs. This fork exists to keep the mod maintained:
upstream has been dormant since Dec 2025 and the published 1.2.6 build
ships a bug that makes every large shark permanently persistent
([upstream issue #7](https://github.com/PaoloBen/BensSharks/issues/7)),
which leaks entities and grows server memory until OOM.

## What changed vs 1.2.6

- **Fixed immortal sharks** — removed unconditional
  `setPersistenceRequired()` calls and `removeWhenFarAway() -> false`
  overrides in BaskingShark, GreaterAxodile, GreenlandShark, LandShark,
  Megalodon, RollParticle, Shrak and WhaleShark. Wild sharks now despawn
  normally; tamed ones still persist (unchanged `mobInteract` path).
- Same content, same modid (`benssharks`) — drop-in upgrade, existing
  worlds and entities keep working.

## License

LGPL-3.0, same as upstream. See LICENSE.

## Building

`gradlew build` → `build/libs/dwurdysharks-*.jar`

Requires NeoForge 21.1.x + GeckoLib 4.x at runtime.
