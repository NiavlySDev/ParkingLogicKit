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