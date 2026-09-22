$ErrorActionPreference = 'Stop'

# Regression guard for issue #13: entity tick procedures must never enqueue
# delayed DRYOUT_EFFECT work. The previous pattern queued a new
# queueServerWork(delay, lambda) every tick while the shark was beached,
# producing up to ~600 live Tuple<Runnable,int> entries per shark and pinning
# entity references in DwurdySharksMod.workQueue. Dryout timing now lives in
# DryoutProcedure's per-entity counter.

$root = Split-Path -Parent $PSScriptRoot
$procDir = Join-Path $root 'src/main/java/net/mcreator/sharks/procedures'

$bad = Get-ChildItem $procDir -Filter '*OnEntityTickUpdateProcedure.java' -File |
    Where-Object {
        $src = Get-Content $_.FullName -Raw
        $src -match 'queueServerWork' -and $src -match 'DRYOUT_EFFECT'
    }
if ($bad) {
    $paths = ($bad | ForEach-Object { $_.Name }) -join ', '
    throw "Tick procedures must not queue delayed dryout work: $paths"
}

Write-Output 'Dryout queue regression guard passed.'
