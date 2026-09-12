$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $PSScriptRoot
$entityDir = Join-Path $root 'src/main/java/net/mcreator/sharks/entity'
$land = Get-Content (Join-Path $entityDir 'LandSharkEntity.java') -Raw
$nurse = Get-Content (Join-Path $entityDir 'NurseSharkEntity.java') -Raw

foreach ($source in @($land, $nurse)) {
    if ($source -notmatch 'removeWhenFarAway\s*\([^)]*\)\s*\{\s*return\s+!this\.isTame\(\);') {
        throw 'Tamable shark despawn guard is missing or no longer tame-aware.'
    }
}

$persistenceCalls = Get-ChildItem $entityDir -Filter '*.java' -File |
    Select-String -Pattern 'setPersistenceRequired\s*\('
if ($persistenceCalls) {
    $paths = ($persistenceCalls | ForEach-Object { $_.Path }) -join ', '
    throw "Unconditional entity persistence call found in: $paths"
}

$metadata = Get-Content (Join-Path $root 'src/main/resources/META-INF/neoforge.mods.toml') -Raw
if ($metadata -notmatch 'modId="benssharks"') { throw 'The compatibility mod id must remain benssharks.' }
if ($metadata -notmatch 'modId="geckolib"\s+type="required"\s+versionRange="\[4,5\)"') {
    throw 'GeckoLib 4.x must remain a required dependency.'
}

Write-Output 'Persistence regression guard passed.'
