# scripts/generate-dev-scripts.ps1
# Run from repo root:
#   powershell -ExecutionPolicy Bypass -File .\scripts\generate-dev-scripts.ps1

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# --- Always compute repo root from THIS script location (never from current folder) ---
$ScriptPath = $MyInvocation.MyCommand.Path
$ScriptDir  = Split-Path -Parent $ScriptPath                 # ...\ParkingProjetS2\scripts
$RepoRoot   = (Get-Item (Join-Path $ScriptDir "..")).FullName # ...\ParkingProjetS2

$Branches = @("sylvain","phily","ethan","virgile")

function To-Abs([string]$p) {
  if ([System.IO.Path]::IsPathRooted($p)) { return $p }
  return (Join-Path $RepoRoot $p)
}

function Ensure-Dir([string]$p) {
  $full = To-Abs $p
  New-Item -ItemType Directory -Force -Path $full | Out-Null
}

function Write-Utf8NoBom([string]$path, [string]$content) {
  $full = To-Abs $path
  $dir = Split-Path -Parent $full
  if ($dir -and !(Test-Path $dir)) { Ensure-Dir $dir }
  $enc = New-Object System.Text.UTF8Encoding($false) # UTF-8 no BOM
  [System.IO.File]::WriteAllText($full, $content, $enc)
}

# Create folders
Ensure-Dir "scripts"
Ensure-Dir "scripts\windows\ps1"
Ensure-Dir "scripts\windows\cmd"
Ensure-Dir "scripts\linux"

# ==========================================================
# PowerShell UPDATE (merge origin/main into dev branch)
# - Allows untracked files (like scripts/)
# - Blocks only if tracked files are modified/staged
# ==========================================================
$psUpdate = @'
param(
  [Parameter(Mandatory=$true)]
  [string]$Branch,
  [switch]$NoPush
)

function Fail([string]$msg) {
  Write-Host ("ERROR: " + $msg) -ForegroundColor Red
  exit 1
}

git rev-parse --is-inside-work-tree *> $null
if ($LASTEXITCODE -ne 0) { Fail "Not inside a Git repo. Run from project root." }

# Block ONLY if tracked files are modified/staged (untracked is OK)
git diff --quiet
$hasWorkingChanges = ($LASTEXITCODE -ne 0)
git diff --cached --quiet
$hasStagedChanges = ($LASTEXITCODE -ne 0)

if ($hasWorkingChanges -or $hasStagedChanges) {
  Fail "You have modified/staged tracked files. Commit or stash before update (git status)."
}

Write-Host "Fetching origin..." -ForegroundColor Cyan
git fetch origin
if ($LASTEXITCODE -ne 0) { Fail "git fetch failed" }

# Ensure local branch exists and tracks origin/Branch
git show-ref --verify --quiet ("refs/heads/" + $Branch)
if ($LASTEXITCODE -ne 0) {
  Write-Host ("Creating local branch " + $Branch + " tracking origin/" + $Branch + "...") -ForegroundColor Yellow
  git switch --track -c $Branch ("origin/" + $Branch)
  if ($LASTEXITCODE -ne 0) { Fail ("Cannot create branch " + $Branch + ". Does it exist on GitHub?") }
} else {
  Write-Host ("Switching to " + $Branch + "...") -ForegroundColor Cyan
  git switch $Branch
  if ($LASTEXITCODE -ne 0) { Fail ("Cannot switch to " + $Branch) }
}

Write-Host "Pulling branch..." -ForegroundColor Cyan
git pull
if ($LASTEXITCODE -ne 0) { Fail "git pull failed" }

Write-Host "Merging origin/main into branch..." -ForegroundColor Cyan
git merge origin/main --no-edit
if ($LASTEXITCODE -ne 0) {
  Fail "Merge conflict. Resolve conflicts then: git add . ; git commit ; git push"
}

if (-not $NoPush) {
  Write-Host "Pushing branch..." -ForegroundColor Cyan
  git push
  if ($LASTEXITCODE -ne 0) { Fail "git push failed" }
}

Write-Host ("OK: " + $Branch + " updated from main.") -ForegroundColor Green
'@

Write-Utf8NoBom "scripts\windows\ps1\update.ps1" $psUpdate

# Wrappers UPDATE per dev (PS1)
foreach ($b in $Branches) {
  $psWrapUpdate = @"
`$repoRoot = (Get-Item (Join-Path `$PSScriptRoot '..\..\..')).FullName
Set-Location `$repoRoot
& (Join-Path `$PSScriptRoot 'update.ps1') -Branch '$b'
"@
  Write-Utf8NoBom "scripts\windows\ps1\update_$b.ps1" $psWrapUpdate
}

# ==========================================================
# PowerShell PUSH (commit + push on dev branch)
# - Switches to branch
# - Pulls latest (merge)
# - git add -A
# - asks commit message
# ==========================================================
$psPush = @'
param(
  [Parameter(Mandatory=$true)]
  [string]$Branch
)

function Fail([string]$msg) {
  Write-Host ("ERROR: " + $msg) -ForegroundColor Red
  exit 1
}

git rev-parse --is-inside-work-tree *> $null
if ($LASTEXITCODE -ne 0) { Fail "Not inside a Git repo. Run from project root." }

Write-Host "Fetching origin..." -ForegroundColor Cyan
git fetch origin
if ($LASTEXITCODE -ne 0) { Fail "git fetch failed" }

# Ensure local branch exists and tracks origin/Branch
git show-ref --verify --quiet ("refs/heads/" + $Branch)
if ($LASTEXITCODE -ne 0) {
  Write-Host ("Creating local branch " + $Branch + " tracking origin/" + $Branch + "...") -ForegroundColor Yellow
  git switch --track -c $Branch ("origin/" + $Branch)
  if ($LASTEXITCODE -ne 0) { Fail ("Cannot create branch " + $Branch + ". Does it exist on GitHub?") }
} else {
  Write-Host ("Switching to " + $Branch + "...") -ForegroundColor Cyan
  git switch $Branch
  if ($LASTEXITCODE -ne 0) { Fail ("Cannot switch to " + $Branch) }
}

Write-Host "Pulling latest (merge)..." -ForegroundColor Cyan
git pull
if ($LASTEXITCODE -ne 0) { Fail "git pull failed (resolve conflicts if any)" }

Write-Host "Adding files..." -ForegroundColor Cyan
git add -A
if ($LASTEXITCODE -ne 0) { Fail "git add failed" }

# Anything to commit?
$pending = git status --porcelain
if (-not $pending) {
  Write-Host "Nothing to commit. Pushing anyway..." -ForegroundColor Yellow
  git push
  exit 0
}

$msg = Read-Host "Commit message (example: Add feature X)"
if ([string]::IsNullOrWhiteSpace($msg)) {
  $msg = "Update"
}

Write-Host "Committing..." -ForegroundColor Cyan
git commit -m $msg
if ($LASTEXITCODE -ne 0) { Fail "git commit failed" }

Write-Host "Pushing..." -ForegroundColor Cyan
git push
if ($LASTEXITCODE -ne 0) { Fail "git push failed" }

Write-Host "OK: pushed to origin/$Branch" -ForegroundColor Green
'@

Write-Utf8NoBom "scripts\windows\ps1\push.ps1" $psPush

# Wrappers PUSH per dev (PS1)
foreach ($b in $Branches) {
  $psWrapPush = @"
`$repoRoot = (Get-Item (Join-Path `$PSScriptRoot '..\..\..')).FullName
Set-Location `$repoRoot
& (Join-Path `$PSScriptRoot 'push.ps1') -Branch '$b'
"@
  Write-Utf8NoBom "scripts\windows\ps1\push_$b.ps1" $psWrapPush
}

# ==========================================================
# CMD UPDATE
# ==========================================================
$cmdUpdate = @'
@echo off
setlocal enabledelayedexpansion

set BRANCH=%~1
if "%BRANCH%"=="" (
  echo Usage: update.cmd ^<branch^>
  exit /b 1
)

git rev-parse --is-inside-work-tree >nul 2>&1
if errorlevel 1 (
  echo ERROR: Not inside a Git repo. Run from project root.
  exit /b 1
)

REM Block only if tracked files changed/staged (untracked allowed)
git diff --quiet
set WC=%ERRORLEVEL%
git diff --cached --quiet
set ST=%ERRORLEVEL%
if NOT "%WC%"=="0" (
  echo ERROR: You have modified tracked files. Commit/stash before update.
  exit /b 1
)
if NOT "%ST%"=="0" (
  echo ERROR: You have staged files. Commit/stash before update.
  exit /b 1
)

echo Fetching origin...
git fetch origin || exit /b 1

git show-ref --verify --quiet refs/heads/%BRANCH%
if errorlevel 1 (
  echo Creating local branch %BRANCH% tracking origin/%BRANCH%...
  git switch --track -c %BRANCH% origin/%BRANCH% || exit /b 1
) else (
  git switch %BRANCH% || exit /b 1
)

echo Pulling...
git pull || exit /b 1

echo Merging origin/main...
git merge origin/main --no-edit
if errorlevel 1 (
  echo ERROR: Merge conflict. Resolve then: git add . ^& git commit ^& git push
  exit /b 1
)

echo Pushing...
git push || exit /b 1

echo OK: %BRANCH% updated from main.
'@
Write-Utf8NoBom "scripts\windows\cmd\update.cmd" $cmdUpdate

foreach ($b in $Branches) {
  $cmdWrap = @"
@echo off
cd /d "%~dp0\..\..\.."
call scripts\windows\cmd\update.cmd $b
"@
  Write-Utf8NoBom "scripts\windows\cmd\update_$b.cmd" $cmdWrap
}

# ==========================================================
# CMD PUSH
# ==========================================================
$cmdPush = @'
@echo off
setlocal enabledelayedexpansion

set BRANCH=%~1
if "%BRANCH%"=="" (
  echo Usage: push.cmd ^<branch^>
  exit /b 1
)

git rev-parse --is-inside-work-tree >nul 2>&1
if errorlevel 1 (
  echo ERROR: Not inside a Git repo. Run from project root.
  exit /b 1
)

echo Fetching origin...
git fetch origin || exit /b 1

git show-ref --verify --quiet refs/heads/%BRANCH%
if errorlevel 1 (
  echo Creating local branch %BRANCH% tracking origin/%BRANCH%...
  git switch --track -c %BRANCH% origin/%BRANCH% || exit /b 1
) else (
  git switch %BRANCH% || exit /b 1
)

echo Pulling latest (merge)...
git pull || exit /b 1

echo Adding files...
git add -A || exit /b 1

for /f "delims=" %%A in ('git status --porcelain') do set HAS=1
if not defined HAS (
  echo Nothing to commit. Pushing anyway...
  git push || exit /b 1
  exit /b 0
)

set /p MSG=Commit message: 
if "%MSG%"=="" set MSG=Update

git commit -m "%MSG%"
if errorlevel 1 (
  echo ERROR: Commit failed.
  exit /b 1
)

git push || exit /b 1
echo OK: pushed to origin/%BRANCH%
'@
Write-Utf8NoBom "scripts\windows\cmd\push.cmd" $cmdPush

foreach ($b in $Branches) {
  $cmdWrap = @"
@echo off
cd /d "%~dp0\..\..\.."
call scripts\windows\cmd\push.cmd $b
"@
  Write-Utf8NoBom "scripts\windows\cmd\push_$b.cmd" $cmdWrap
}

# ==========================================================
# Linux SH UPDATE
# ==========================================================
$shUpdate = @'
#!/usr/bin/env sh
set -eu

BRANCH="${1:-}"
if [ -z "$BRANCH" ]; then
  echo "Usage: ./scripts/linux/update.sh <branch>"
  exit 1
fi

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || {
  echo "ERROR: Not inside a Git repo. Run from project root."
  exit 1
}

# Block only if tracked files changed/staged (untracked allowed)
if ! git diff --quiet; then
  echo "ERROR: You have modified tracked files. Commit/stash before update."
  exit 1
fi
if ! git diff --cached --quiet; then
  echo "ERROR: You have staged files. Commit/stash before update."
  exit 1
fi

echo "Fetching origin..."
git fetch origin

if git show-ref --verify --quiet "refs/heads/$BRANCH"; then
  git switch "$BRANCH"
else
  git switch --track -c "$BRANCH" "origin/$BRANCH"
fi

echo "Pulling..."
git pull

echo "Merging origin/main..."
if ! git merge origin/main --no-edit; then
  echo "ERROR: Merge conflict. Resolve then: git add . ; git commit ; git push"
  exit 1
fi

echo "Pushing..."
git push

echo "OK: $BRANCH updated from main."
'@
Write-Utf8NoBom "scripts\linux\update.sh" $shUpdate

# Wrappers Linux UPDATE per dev (IMPORTANT: single-quoted here-string so $0 is not expanded by PowerShell)
foreach ($b in $Branches) {
  $shWrap = @'
#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/../.."
./scripts/linux/update.sh BRANCH_PLACEHOLDER
'@ -replace 'BRANCH_PLACEHOLDER', $b

  Write-Utf8NoBom "scripts\linux\update_$b.sh" $shWrap
}

# ==========================================================
# Linux SH PUSH
# ==========================================================
$shPush = @'
#!/usr/bin/env sh
set -eu

BRANCH="${1:-}"
if [ -z "$BRANCH" ]; then
  echo "Usage: ./scripts/linux/push.sh <branch>"
  exit 1
fi

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || {
  echo "ERROR: Not inside a Git repo. Run from project root."
  exit 1
}

echo "Fetching origin..."
git fetch origin

if git show-ref --verify --quiet "refs/heads/$BRANCH"; then
  git switch "$BRANCH"
else
  git switch --track -c "$BRANCH" "origin/$BRANCH"
fi

echo "Pulling latest (merge)..."
git pull

echo "Adding files..."
git add -A

if [ -z "$(git status --porcelain)" ]; then
  echo "Nothing to commit. Pushing anyway..."
  git push
  exit 0
fi

printf "Commit message: "
read -r MSG || true
if [ -z "${MSG:-}" ]; then
  MSG="Update"
fi

git commit -m "$MSG"
git push

echo "OK: pushed to origin/$BRANCH"
'@
Write-Utf8NoBom "scripts\linux\push.sh" $shPush

# Wrappers Linux PUSH per dev
foreach ($b in $Branches) {
  $shWrap = @'
#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/../.."
./scripts/linux/push.sh BRANCH_PLACEHOLDER
'@ -replace 'BRANCH_PLACEHOLDER', $b

  Write-Utf8NoBom "scripts\linux\push_$b.sh" $shWrap
}

Write-Host "DONE: created update/push scripts for all devs (ps1/cmd/sh)." -ForegroundColor Green
Write-Host ("Repo root: " + $RepoRoot) -ForegroundColor DarkGray
