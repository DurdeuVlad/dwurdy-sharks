$ErrorActionPreference = 'Stop'

# Regression guard for issue #18: hot-path procedures must be single-pass
# and throttled. A procedure may not issue more than 2 entity queries per
# execute() path, and the global tick subscribers listed here must carry a
# tickCount throttle so 500 sharks never scan in the same tick.

$root = Split-Path -Parent $PSScriptRoot
$procDir = Join-Path $root 'src/main/java/net/mcreator/sharks/procedures'

$hot = @('EatKrillProcedure', 'BarracudaSprintProcedure', 'EatDroppedItemProcedure',
         'BarracudaEatProcedure', 'MakoSprintProcedure')
foreach ($name in $hot) {
    $src = Get-Content (Join-Path $procDir "$name.java") -Raw
    if (-not ($src -match 'tickCount')) {
        throw "$name lost its tick-throttle"
    }
    if ([regex]::Matches($src, 'getEntitiesOfClass|getEntities\(').Count -gt 2) {
        throw "$name issues more than 2 entity queries per call"
    }
}

Write-Output 'Hot-path query guard passed.'
