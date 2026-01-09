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