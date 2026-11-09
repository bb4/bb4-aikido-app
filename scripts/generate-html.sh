#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "Generating HTML files..."
echo "Project root: $PROJECT_ROOT"

# The generator writes to FileUtil.getHomeDir + "deployment/"
# We need to ensure getHomeDir returns the project root
cd "$PROJECT_ROOT"

# Run the generator - it reads from deployment/techniques/techniques.xml
# and writes to ~/deployment/ (where ~ should be the project root via user.dir)
./gradlew run --args="deployment/techniques/techniques.xml"

# Check if files were generated in the project deployment/ directory
echo ""
echo "Checking for generated files in $PROJECT_ROOT/deployment/..."

MISSING_FILES=()

if [ ! -f "$PROJECT_ROOT/deployment/technique_builder.html" ]; then
    MISSING_FILES+=("deployment/technique_builder.html")
fi

if [ ! -f "$PROJECT_ROOT/deployment/all_techniques.html" ]; then
    MISSING_FILES+=("deployment/all_techniques.html")
fi

if [ ! -f "$PROJECT_ROOT/deployment/all_unique.html" ]; then
    MISSING_FILES+=("deployment/all_unique.html")
fi

# Also check if files were created in user home directory and move them if so
if [ ! -f "$PROJECT_ROOT/deployment/technique_builder.html" ] && [ -f "$HOME/deployment/technique_builder.html" ]; then
    echo "Found generated files in $HOME/deployment/, moving to project..."
    mv "$HOME/deployment/technique_builder.html" "$PROJECT_ROOT/deployment/"
    mv "$HOME/deployment/all_techniques.html" "$PROJECT_ROOT/deployment/"
    mv "$HOME/deployment/all_unique.html" "$PROJECT_ROOT/deployment/"
    MISSING_FILES=()
fi

if [ ${#MISSING_FILES[@]} -ne 0 ]; then
    echo "ERROR: The following generated files are missing:"
    for file in "${MISSING_FILES[@]}"; do
        echo "  - $file"
    done
    echo ""
    echo "Checked locations:"
    echo "  - $PROJECT_ROOT/deployment/"
    echo "  - $HOME/deployment/"
    exit 1
fi

echo "✓ All generated HTML files are present"
echo ""
echo "Generated files:"
ls -lh "$PROJECT_ROOT/deployment/technique_builder.html" \
      "$PROJECT_ROOT/deployment/all_techniques.html" \
      "$PROJECT_ROOT/deployment/all_unique.html"
