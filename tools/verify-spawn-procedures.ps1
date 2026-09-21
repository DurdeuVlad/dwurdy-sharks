$ErrorActionPreference = 'Stop'

# Regression guard for issue #12: *OnInitialEntitySpawnProcedure classes are
# spawn-time hooks called once from finalizeSpawn(). They must never subscribe
# to the event bus; the previous @EventBusSubscriber + EntityTickEvent.Pre
# wiring ran a 25-block sorted AABB scan every tick per entity.

$root = Split-Path -Parent $PSScriptRoot
$procDir = Join-Path $root 'src/main/java/net/mcreator/sharks/procedures'

$bad = Get-ChildItem $procDir -Filter '*OnInitialEntitySpawnProcedure.java' -File |
    Where-Object {
        $src = Get-Content $_.FullName -Raw
        $src -match '@SubscribeEvent' -or $src -match '@EventBusSubscriber' -or $src -match 'EntityTickEvent'
    }
if ($bad) {
    $paths = ($bad | ForEach-Object { $_.Name }) -join ', '
    throw "Initial-spawn procedures must not subscribe to tick events: $paths"
}

Write-Output 'Spawn-procedure subscriber guard passed.'
