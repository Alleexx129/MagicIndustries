# Put this port on a GitHub branch named `1.21.1`

## Why the Git in the uploaded ZIP was broken

The uploaded source ZIP did not contain a normal `.git` directory. Its `.git` was a **worktree pointer** containing an absolute path from the original computer:

```text
gitdir: /home/alex/Desktop/Coding/Minecraft stuff/MagicIndustries-Forge-1.21.1/.git/worktrees/MagicIndustries-Forge-1.21.1-1.21.1-forge
```

That pointer only works while the original parent repository and worktree metadata exist at exactly that location. This NeoForge ZIP intentionally contains **no `.git` metadata** so it will not inherit that broken state.

## Safest workflow — preserve your existing GitHub history

Do **not** run `git init` inside this ZIP if you want to keep the history already on GitHub. Instead, make a fresh clone of your GitHub repository and put this port on the branch there.

### 1. Clone your repo

```bash
git clone YOUR_GITHUB_REPO_URL MagicIndustries
cd MagicIndustries
git fetch origin --prune
```

### 2. Switch to `1.21.1`

If `1.21.1` does **not** exist yet on GitHub:

```bash
git switch main
git pull --ff-only
git switch -c 1.21.1
```

If `origin/1.21.1` already exists but you do not have it locally:

```bash
git switch --track origin/1.21.1
```

If you already have a local `1.21.1` branch:

```bash
git switch 1.21.1
git pull --ff-only
```

### 3. Copy the port into that fresh clone

Copy **all files from this NeoForge project** into the fresh clone, replacing the old project files, but never delete or overwrite the fresh clone's `.git` directory.

Then run:

```bash
git status
git add -A
git commit -m "Port Magic Industries to NeoForge 1.21.1"
git push -u origin 1.21.1
```

After that, normal future pushes from this branch are just:

```bash
git add -A
git commit -m "Describe your change"
git push
```

## If `git switch` says the branch already exists

Use:

```bash
git switch 1.21.1
```

Do not create it again.

## If Git says `origin/1.21.1` exists but local `1.21.1` does not

Use:

```bash
git fetch origin
git switch --track origin/1.21.1
```

## If your default branch is not `main`

Replace `main` above with the actual base branch, usually `master`, `1.21`, or whatever branch contains the history you want to branch from.

## Avoid these while fixing the branch

- Do not copy the old worktree `.git` pointer into the new project.
- Do not use `git push --force` just to make the branch work.
- Do not delete the GitHub repository and recreate it unless you truly want to discard history.
- Do not commit `.idea/`, `.gradle/`, `build/`, or `run/`; this project's `.gitignore` excludes them.
