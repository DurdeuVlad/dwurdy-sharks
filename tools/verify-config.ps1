$ErrorActionPreference = "Stop"
$fail = 0

function Fail($msg) { Write-Host "FAIL: $msg"; $script:fail = 1 }

$root = Split-Path -Parent $PSScriptRoot
$src = Join-Path $root "src/main/java/net/mcreator/sharks"

# 1. Config class exists and uses ModConfigSpec with required keys
$configPath = Join-Path $src "init/DwurdySharksConfig.java"
if (!(Test-Path $configPath)) { Fail "DwurdySharksConfig.java missing" }
$config = Get-Content $configPath -Raw
foreach ($key in @("ModConfigSpec", "spawningEnabled", "oceanOnly", "sharkDamageMultiplier", "megalodonDamage", "megalodonArmorBypass", "speciesEnabled", "isModEntity")) {
    if ($config -notmatch [regex]::Escape($key)) { Fail "DwurdySharksConfig missing $key" }
}
# 24 species flags
if (($config | Select-String -Pattern '"Enabled"' -AllMatches).Matches.Count -lt 1 -and ([regex]::Matches($config, 'Enabled"')).Count -lt 20) {
    Fail "expected ~24 per-species Enabled flags in DwurdySharksConfig"
}

# 2. Config registered on the mod container
$mod = Get-Content (Join-Path $root "src/main/java/net/mcreator/sharks/BenssharksMod.java") -Raw
if ($mod -notmatch "registerConfig\(ModConfig\.Type\.SERVER, DwurdySharksConfig\.SPEC\)") {
    Fail "BenssharksMod does not register SERVER config"
}

# 3. Spawn guard reads SPAWNING_ENABLED + speciesEnabled, namespace-scoped
$cap = Get-Content (Join-Path $src "procedures/SharkSpawnCapProcedure.java") -Raw
if ($cap -notmatch "SPAWNING_ENABLED") { Fail "spawn guard does not read SPAWNING_ENABLED" }
if ($cap -notmatch "speciesEnabled") { Fail "spawn guard does not read speciesEnabled" }
if ($cap -notmatch "isModEntity") { Fail "spawn guard not namespace-scoped (would cancel vanilla spawns)" }

# 4. Every water predicate honors OCEAN_ONLY
$entityFiles = Get-ChildItem (Join-Path $src "entity") -Filter "*Entity.java"
$withTag = @(); $withToggle = @()
foreach ($f in $entityFiles) {
    $c = Get-Content $f.FullName -Raw
    if ($c -match "SHARK_SPAWNING_OCEANS") {
        $withTag += $f.Name
        if ($c -match "OCEAN_ONLY\.get\(\)") { $withToggle += $f.Name }
    }
}
if ($withTag.Count -lt 20) { Fail "only $($withTag.Count) entity predicates use SHARK_SPAWNING_OCEANS" }
if ($withToggle.Count -ne $withTag.Count) { Fail "$($withTag.Count - $withToggle.Count) predicates use the ocean tag without OCEAN_ONLY" }

# 5. Damage path
$dmg = Get-Content (Join-Path $src "procedures/SharkDamageScaleProcedure.java") -Raw
if ($dmg -notmatch "SHARK_DAMAGE_MULTIPLIER") { Fail "damage handler missing multiplier" }
if ($dmg -notmatch "MEGALODON_ARMOR_BYPASS") { Fail "damage handler missing armor bypass" }
foreach ($p in @(
    "$root/src/main/resources/data/dwurdysharks/tags/entity_type/sharks.json",
    "$root/src/main/resources/data/dwurdysharks/damage_type/megalodon_bite.json",
    "$root/src/main/resources/data/minecraft/tags/damage_type/bypasses_armor.json")) {
    if (!(Test-Path $p)) { Fail "missing resource $p" }
}
if ($dmg -notmatch "MEGALODON_BITE\)\) \{\s*return") { Fail "bypass recursion guard missing" }

if ($fail -eq 0) { Write-Host "Config guard passed." } else { exit 1 }
