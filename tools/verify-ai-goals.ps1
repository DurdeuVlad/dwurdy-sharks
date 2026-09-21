$ErrorActionPreference = 'Stop'

# Regression guard for issue #16: per-class NearestAttackableTargetGoal lists
# must stay collapsed into tag-predicate goals. Any plain
#   new NearestAttackableTargetGoal(this, X.class, true, true)
# is a per-class scan; conditional/anonymous overrides and the tag form
# (LivingEntity.class + entity_type tag predicate) are allowed.

$root = Split-Path -Parent $PSScriptRoot
$entityDir = Join-Path $root 'src/main/java/net/mcreator/sharks/entity'

$bad = Get-ChildItem $entityDir -Filter '*.java' -File | Where-Object {
    $src = Get-Content $_.FullName -Raw
    [regex]::Matches($src, 'NearestAttackableTargetGoal\(this, \w+\.class, true, true\)\)').Count -gt 0
}
if ($bad) {
    throw "Per-class target goals reintroduced: $(($bad | ForEach-Object { $_.Name }) -join ', ')"
}

Write-Output 'AI goal tag guard passed.'
