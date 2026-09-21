$ErrorActionPreference = 'Stop'

# Regression guard for issue #14: natural wild spawning is ocean-only.
# 1. biome_modifier JSONs may only list ocean biomes or tag references.
# 2. Every IN_WATER spawn placement must gate on the
#    dwurdysharks:shark_spawning_oceans biome tag, so a JSON mistake alone
#    can never reintroduce river/swamp/beach spawns.

$root = Split-Path -Parent $PSScriptRoot
$modDir = Join-Path $root 'src/main/resources/data/dwurdysharks/neoforge/biome_modifier'
$entityDir = Join-Path $root 'src/main/java/net/mcreator/sharks/entity'

$ocean = @('ocean','deep_ocean','frozen_ocean','deep_frozen_ocean','cold_ocean',
           'deep_cold_ocean','lukewarm_ocean','deep_lukewarm_ocean','warm_ocean')

$badJson = Get-ChildItem $modDir -Filter '*.json' -File | Where-Object {
    $biomes = (Get-Content $_.FullName -Raw | ConvertFrom-Json).biomes
    $biomes | Where-Object { -not $_.StartsWith('#') -and ($ocean -notcontains ($_.Split(':')[-1])) }
}
if ($badJson) {
    throw "Non-ocean biomes in spawn modifiers: $(($badJson | ForEach-Object { $_.Name }) -join ', ')"
}

$badPred = Get-ChildItem $entityDir -Filter '*.java' -File | Where-Object {
    $src = Get-Content $_.FullName -Raw
    $src -match 'SpawnPlacementTypes\.IN_WATER' -and $src -notmatch 'SHARK_SPAWNING_OCEANS'
}
if ($badPred) {
    throw "Water spawn placements missing ocean-biome check: $(($badPred | ForEach-Object { $_.Name }) -join ', ')"
}

Write-Output 'Ocean-only spawn guard passed.'
