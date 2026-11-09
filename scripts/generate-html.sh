#!/bin/bash
set -e

echo "Generating HTML files..."
./gradlew run --args="deployment/techniques/techniques.xml"

echo ""
echo "Verifying generated files exist..."
MISSING_FILES=()

if [ ! -f "deployment/technique_builder.html" ]; then
    MISSING_FILES+=("deployment/technique_builder.html")
fi

if [ ! -f "deployment/all_techniques.html" ]; then
    MISSING_FILES+=("deployment/all_techniques.html")
fi

if [ ! -f "deployment/all_unique.html" ]; then
    MISSING_FILES+=("deployment/all_unique.html")
fi

if [ ${#MISSING_FILES[@]} -ne 0 ]; then
    echo "ERROR: The following generated files are missing:"
    for file in "${MISSING_FILES[@]}"; do
        echo "  - $file"
    done
    exit 1
fi

echo "All generated HTML files are present."
echo ""
echo "Generated files:"
ls -lh deployment/technique_builder.html deployment/all_techniques.html deployment/all_unique.html
