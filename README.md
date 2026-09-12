# Dwurdy Sharks

> **All the sharks. Maintained.**

Dwurdy Sharks is a community maintenance fork of **[Ben's Sharks](https://github.com/PaoloBen/BensSharks)** by **benndevs / PaoloBen** for Minecraft 1.21.1 and NeoForge. It keeps the original mod's marine life and items available while addressing a serious entity-persistence bug.

This is an independent, community-run fork. It is not the original project, and no endorsement or sponsorship by the original author is implied. Please give the original project the credit it deserves:

- [Ben's Sharks on GitHub](https://github.com/PaoloBen/BensSharks)
- [Ben's Sharks on Modrinth](https://modrinth.com/mod/bens-sharks)
- [Ben's Sharks on CurseForge](https://www.curseforge.com/minecraft/mc-mods/bens-sharks)

## What's fixed

The upstream 1.21.x persistent-shark bug tracked in [issue #7](https://github.com/PaoloBen/BensSharks/issues/7) made large sharks permanently persistent. In this fork, **wild sharks can despawn normally again**, while **tamed sharks still persist** as intended.

## Compatibility

Dwurdy Sharks keeps the original mod ID, `benssharks`, and is drop-in compatible with **Ben's Sharks 1.2.6**. For existing 1.21.1 worlds and modpacks, stop the game, back up your world, replace the Ben's Sharks jar with the Dwurdy Sharks jar, and keep the rest of the installation unchanged. Do not install both jars at the same time.

## What is included

- A 26+ marine entity roster, representing **26 registered marine entity types**, including Great White, Megalodon, Basking Shark, Axodile, Barracuda, Krill, Land Shark, and more
- Shark-tooth items and weapons, including the Shark Tooth Club and Megalodon Tooth
- Jagged armor
- Shark Plush
- Food and cooking items made from the mod's marine life

The content and original creative work remain credited to benndevs / PaoloBen.

## Requirements

- Minecraft **1.21.1**
- **NeoForge 21.1.x**
- **GeckoLib 4.x**

This repository currently verifies the NeoForge 1.21.1 target only. See [docs/VERSIONS.md](docs/VERSIONS.md) for the branch and support policy.

## Building

Use the included Gradle wrapper from the repository root:

```text
# Windows
gradlew.bat build

# Linux / macOS
./gradlew build
```

The built jar is written to `build/libs/`.

## Contributing

Pull requests are welcome. Keep changes focused, preserve the original mod's attribution and LGPL-3.0 licensing, and include a clear description of the problem, the behavior you changed, and the checks you ran. Please avoid changing the `benssharks` mod ID or breaking existing world compatibility without a strong migration plan.

## License

Dwurdy Sharks is licensed under **LGPL-3.0**, the same license used by the upstream project. See [LICENSE](LICENSE).
