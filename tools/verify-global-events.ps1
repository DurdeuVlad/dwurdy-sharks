$ErrorActionPreference = "Stop"
$fail = 0
function Fail($msg) { Write-Host "FAIL: $msg"; $script:fail = 1 }
$root = Split-Path -Parent $PSScriptRoot
$src = Join-Path $root "src/main/java/net/mcreator/sharks"

# 1. Global EntityTickEvent subscribers allowed only in the three justified survivors
$allowed = @("EntityAnimationFactory.java", "BleedingImmunityProcedure.java", "IfWearingArmorProcedure.java", "SharkDespawnProcedure.java")
$subs = Get-ChildItem -Recurse (Join-Path $root "src/main/java") -Filter "*.java" |
    Where-Object { (Get-Content $_.FullName -Raw) -match "EntityTickEvent\.Pre" }
foreach ($s in $subs) { if ($allowed -notcontains $s.Name) { Fail "unexpected EntityTickEvent.Pre subscriber: $($s.Name)" } }

# 2. Dead procedures deleted
foreach ($d in @("BREACHProcedure.java", "ChaseTargetProcedure.java", "MovementRotationProcedure.java", "RollParticleOnEntityTickUpdateProcedure.java")) {
    if (Test-Path (Join-Path $src "procedures/$d")) { Fail "dead procedure still exists: $d" }
}

# 3. RollParticleEntity is AI-free with the water counter
$rp = Get-Content (Join-Path $src "entity/RollParticleEntity.java") -Raw
if ($rp -match "goalSelector\.addGoal|targetSelector\.addGoal") { Fail "RollParticleEntity still registers goals" }
if ($rp -notmatch "setNoAi\(true\)") { Fail "RollParticleEntity missing setNoAi(true)" }
if ($rp -notmatch "waterTicks") { Fail "RollParticleEntity missing water counter" }
if ($rp -match "queueServerWork") { Fail "RollParticleEntity still queues delayed work" }

# 4. Moved procedures have no subscriber machinery and are called from entity ticks
$moved = @{
    "BarracudaEatProcedure" = @("BarracudaEntity");
    "BarracudaSprintProcedure" = @("BarracudaEntity");
    "MakoSprintProcedure" = @("MakoSharkEntity");
    "EatKrillProcedure" = @("BaskingSharkEntity", "WhaleSharkEntity");
    "FollowIfTamedProcedure" = @("NurseSharkEntity");
    "RightClickSpeedProcedure" = @("NurseSharkEntity");
    "StopFloatingProcedure" = @("MegalodonEntity");
}
foreach ($proc in $moved.Keys) {
    $p = Get-Content (Join-Path $src "procedures/$proc.java") -Raw
    if ($p -match "@EventBusSubscriber|onEntityTick|EntityTickEvent") { Fail "$proc still has subscriber machinery" }
    foreach ($ent in $moved[$proc]) {
        $e = Get-Content (Join-Path $src "entity/$ent.java") -Raw
        if ($e -notmatch [regex]::Escape("$proc.execute(")) { Fail "$ent does not call $proc" }
    }
}
foreach ($proc in @("EatDroppedItemProcedure", "SharkBleedProcedure", "StopRidingBoatProcedure")) {
    $p = Get-Content (Join-Path $src "procedures/$proc.java") -Raw
    if ($p -match "@EventBusSubscriber|onEntityTick") { Fail "$proc still has subscriber machinery" }
}
$boat = Get-Content (Join-Path $src "entity/KrillEntity.java") -Raw
if ($boat -notmatch "StopRidingBoatProcedure\.execute\(this\)") { Fail "KrillEntity missing StopRidingBoat call" }

# 5. IsHurt lambdas keep animation reset but dropped redundant removeEffect
$hurt = Get-Content (Join-Path $src "procedures/TigerSharkEntityIsHurtProcedure.java") -Raw
if ($hurt -match "removeEffect") { Fail "IsHurt lambda still has redundant removeEffect" }
if ($hurt -notmatch 'setAnimation\("empty"\)') { Fail "IsHurt lambda lost animation reset" }

if ($fail -eq 0) { Write-Host "Global-event guard passed." } else { exit 1 }
