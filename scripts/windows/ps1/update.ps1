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