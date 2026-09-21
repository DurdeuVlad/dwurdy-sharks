$ErrorActionPreference = 'Stop'

# Regression guard for issue #17: refreshDimensions() must not be called
# per-tick. All 24 entities have static dimensions (no setPose/setBaby/
# age scaling anywhere); getDefaultDimensions(Pose) overrides provide the
# fixed per-species scale at construction.

$root = Split-Path -Parent $PSScriptRoot
$entityDir = Join-Path $root 'src/main/java/net/mcreator/sharks/entity'

$bad = Get-ChildItem $entityDir -Filter '*.java' -File |
    Where-Object { (Get-Content $_.FullName -Raw) -match 'refreshDimensions\(\)' }
if ($bad) {
    throw "Per-tick refreshDimensions found: $(($bad | ForEach-Object { $_.Name }) -join ', ')"
}

Write-Output 'Dimensions guard passed.'
