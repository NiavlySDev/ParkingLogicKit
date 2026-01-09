$repoRoot = (Get-Item (Join-Path $PSScriptRoot '..\..\..')).FullName
Set-Location $repoRoot
& (Join-Path $PSScriptRoot 'push.ps1') -Branch 'virgile'