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
$mod = Get-Content (Join-Path $root "src/main/java/net/mcreator/sharks/DwurdySharksMod.java") -Raw
if ($mod -notmatch "registerConfig\(ModConfig\.Type\.SERVER, DwurdySharksConfig\.SPEC\)") {
    Fail "DwurdySharksMod does not register SERVER config"
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
if ($dmg -notmatch "damageMultiplier\(") { Fail "damage handler missing multiplier" }
if ($config -notmatch "sharkSpeedMultiplier") { Fail "DwurdySharksConfig missing sharkSpeedMultiplier" }
if ($config -notmatch "speciesMultiplier") { Fail "DwurdySharksConfig missing species multiplier tables" }
if ($dmg -notmatch "MEGALODON_ARMOR_BYPASS") { Fail "damage handler missing armor bypass" }
foreach ($p in @(
    "$root/src/main/resources/data/dwurdysharks/tags/entity_type/sharks.json",
    "$root/src/main/resources/data/dwurdysharks/damage_type/megalodon_bite.json",
    "$root/src/main/resources/data/minecraft/tags/damage_type/bypasses_armor.json")) {
    if (!(Test-Path $p)) { Fail "missing resource $p" }
}
if ($dmg -notmatch "MEGALODON_BITE\)\) \{\s*return") { Fail "bypass recursion guard missing" }

# 6. Every config key is documented in docs/CONFIG.md
$docsPath = Join-Path $root "docs/CONFIG.md"
if (!(Test-Path $docsPath)) { Fail "docs/CONFIG.md missing" }
$docs = Get-Content $docsPath -Raw
$keys = [regex]::Matches($config, 'define\w*\("([a-zA-Z0-9_]+)"') | ForEach-Object { $_.Groups[1].Value } | Sort-Object -Unique
foreach ($k in $keys) {
    if ($docs -notmatch [regex]::Escape($k)) { Fail "config key '$k' not documented in docs/CONFIG.md" }
}

# 7. No literal dryout timings remain at call sites
$dryCallSites = Get-ChildItem (Join-Path $src "procedures") -Filter "*OnEntityTickUpdateProcedure.java"
foreach ($f in $dryCallSites) {
    if ((Get-Content $f.FullName -Raw) -match "dryTick\(entity,\s*\d") { Fail "$($f.Name) still passes literal dryout timings" }
}
$dryProc = Get-Content (Join-Path $src "procedures/DryoutProcedure.java") -Raw
if ($dryProc -notmatch "DRYOUT_DELAY_TICKS" -or $dryProc -notmatch "DRYOUT_DURATION_TICKS") {
    Fail "DryoutProcedure does not read config timings"
}

# 8. Attribute modifier IDs are unique and dwurdysharks-namespaced
$attrProcPath = Join-Path $src "procedures/ConfigAttributeProcedure.java"
if (!(Test-Path $attrProcPath)) { Fail "ConfigAttributeProcedure.java missing" }
$attrProc = Get-Content $attrProcPath -Raw
$ids = [regex]::Matches($attrProc, '"dwurdysharks",\s*"([a-z_]+)"') | ForEach-Object { $_.Groups[1].Value }
if ($ids.Count -lt 3) { Fail "expected >=3 attribute modifier IDs in ConfigAttributeProcedure, found $($ids.Count)" }
if (($ids | Sort-Object -Unique).Count -ne $ids.Count) { Fail "duplicate attribute modifier IDs in ConfigAttributeProcedure" }

# 9. Global caps wired in the spawn guard
if ($cap -notmatch "GLOBAL_CAP") { Fail "spawn guard does not read global caps" }

if ($fail -eq 0) { Write-Host "Config guard passed." } else { exit 1 }
