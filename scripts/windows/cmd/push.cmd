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