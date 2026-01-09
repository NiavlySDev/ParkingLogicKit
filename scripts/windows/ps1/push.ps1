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