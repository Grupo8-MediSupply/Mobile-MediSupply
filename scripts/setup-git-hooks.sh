#!/bin/bash

# Setup Git hooks for Mobile MediSupply

echo "🔧 Setting up Git hooks..."

# Create hooks directory if it doesn't exist
mkdir -p .git/hooks

# Pre-commit hook
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash

echo "🔍 Running pre-commit checks..."

# Check for Android lint issues
./gradlew lintDebug --quiet

if [ $? -ne 0 ]; then
    echo "❌ Lint check failed. Please fix the issues before committing."
    exit 1
fi

# Run unit tests
./gradlew testDebugUnitTest --quiet

if [ $? -ne 0 ]; then
    echo "❌ Unit tests failed. Please fix the tests before committing."
    exit 1
fi

echo "✅ Pre-commit checks passed!"
EOF

# Commit message hook
cat > .git/hooks/commit-msg << 'EOF'
#!/bin/bash

# Check commit message format
commit_regex='^(feat|fix|docs|style|refactor|perf|test|chore|ci|build)(\(.+\))?: .{1,50}'

if ! grep -qE "$commit_regex" "$1"; then
    echo "❌ Invalid commit message format!"
    echo "Format: <type>[optional scope]: <description>"
    echo "Example: feat(auth): add login functionality"
    exit 1
fi

echo "✅ Commit message format is valid!"
EOF

# Make hooks executable
chmod +x .git/hooks/pre-commit
chmod +x .git/hooks/commit-msg

# Setup commit template
git config commit.template .gitmessage

echo "✅ Git hooks configured successfully!"
echo "📝 Commit template configured!"
echo ""
echo "Usage:"
echo "  ./gradlew setupGitFlow    # Initialize git flow"
echo "  git flow feature start    # Start new feature"
echo "  git flow feature finish   # Finish feature"
echo ""
echo "Remember to follow conventional commits format! 🚀"
