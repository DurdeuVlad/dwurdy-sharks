# CurseForge Listing Draft — Dwurdy Sharks

> Fill in and submit at https://www.curseforge.com/minecraft/mc-mods (Create Project).
> CurseForge requires manual upload; the description below is ready to paste.

## Project name
Dwurdy Sharks

## Summary (short)
Maintenance fork of Ben's Sharks for NeoForge 1.21.1 — all the same sharks,
now with the immortal-entity despawn bug fixed.

## Description (long)

**Dwurdy Sharks** is a community maintenance fork of
[Ben's Sharks](https://www.curseforge.com/minecraft/mc-mods/bens-sharks) by
**benndevs** (PaoloBen). All credit for the original mod — models, textures,
sounds, entities, items, mechanics — belongs to benndevs.

Upstream has been dormant since December 2025. This fork keeps the mod alive
and fixes a serious bug in the published 1.2.6 build: every large shark was
marked permanently persistent, so they accumulated in `world/entities/`
forever — on busy servers this grows to tens of thousands of stored mobs and
multiple GB of heap (upstream issue #7, fixed here).

### What's in it
- 26+ marine mobs — Great White, Megalodon, Basking Shark, Axodile,
  Barracuda, Krill, Land Shark, and more — with unique drops, armor,
  weapons, plushies and structures
- **Fixed**: wild sharks now despawn normally; tamed sharks still stay
- Same modid (`benssharks`) — drop-in upgrade, existing worlds keep working

### Requirements
- Minecraft 1.21.1
- NeoForge 21.1.x
- GeckoLib 4.x

### Links
- Source: https://github.com/DurdeuVlad/dwurdy-sharks
- Original: https://github.com/PaoloBen/BensSharks

License: LGPL-3.0 (same as upstream).

## Upload checklist

1. Create project: name "Dwurdy Sharks", category Mods, license LGPL-3.0
2. Upload `build/libs/dwurdysharks-1.2.7.jar`
   - Game version: 1.21.1
   - Loader: NeoForge
   - Release type: Release
   - Dependencies: GeckoLib (required on client, optional per upstream metadata)
3. Paste the description above; add the logo from `src/main/resources/logo.png`
4. In the project sidebar link the GitHub repo and credit benndevs in the
   description (already included above)
EOF