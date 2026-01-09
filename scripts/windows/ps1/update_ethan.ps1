$repoRoot = (Get-Item (Join-Path $PSScriptRoot '..\..\..')).FullName
Set-Location $repoRoot
& (Join-Path $PSScriptRoot 'update.ps1') -Branch 'ethan'