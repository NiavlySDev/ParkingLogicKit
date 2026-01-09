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