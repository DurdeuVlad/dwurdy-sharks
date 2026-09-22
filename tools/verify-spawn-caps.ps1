$ErrorActionPreference = 'Stop'

# Regression guard for issue #15: spawn density shaping + local safety cap.
# 1. Entity-type tags large_sharks/ambient_fish must exist.
# 2. add_spawn_costs modifiers must exist for both groups.
# 3. The four cap gamerules must be registered.
# 4. Large-shark biome modifiers must not allow groups > 1.

$root = Split-Path -Parent $PSScriptRoot

foreach ($tag in @('large_sharks', 'ambient_fish')) {
    $path = Join-Path $root "src/main/resources/data/dwurdysharks/tags/entity_type/$tag.json"
    if (-not (Test-Path $path)) { throw "Missing entity-type tag: $tag" }
}

foreach ($mod in @('large_shark_spawn_costs', 'ambient_fish_spawn_costs')) {
    $path = Join-Path $root "src/main/resources/data/dwurdysharks/neoforge/biome_modifier/$mod.json"
    if (-not (Test-Path $path)) { throw "Missing spawn-cost modifier: $mod" }
    $d = Get-Content $path -Raw | ConvertFrom-Json
    if ($d.type -ne 'neoforge:add_spawn_costs') { throw "$mod is not an add_spawn_costs modifier" }
    if ($d.biomes -ne '#dwurdysharks:shark_spawning_oceans') { throw "$mod must scope to the ocean tag" }
}

$rules = Get-Content (Join-Path $root 'src/main/java/net/mcreator/sharks/init/DwurdySharksModGameRules.java') -Raw
foreach ($rule in @('largeSharkLocalCap', 'ambientFishLocalCap', 'sharkSpawnCapRadius', 'enforceCapForManualSpawns')) {
    if ($rules -notmatch "`"$rule`"") { throw "Missing gamerule: $rule" }
}

$ambient = @('krill', 'pilot_fish', 'remora')
Get-ChildItem (Join-Path $root 'src/main/resources/data/dwurdysharks/neoforge/biome_modifier') -Filter '*_biome_modifier.json' |
    Where-Object { $ambient -notcontains ($_.BaseName -replace '_biome_modifier$', '') } |
    ForEach-Object {
        $s = (Get-Content $_.FullName -Raw | ConvertFrom-Json).spawners
        if ($s.maxCount -gt 1) { throw "$($_.Name) allows group size $($s.maxCount) > 1 for a large shark" }
    }

Write-Output 'Spawn-cap guard passed.'
