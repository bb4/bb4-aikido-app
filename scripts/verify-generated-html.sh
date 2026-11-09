#!/bin/bash
set -e

echo "Verifying generated HTML files exist..."
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
    echo "ERROR: The following generated files are missing from deployment/:"
    for file in "${MISSING_FILES[@]}"; do
        echo "  - $file"
    done
    echo ""
    echo "These files are referenced by:"
    echo "  - deployment/aikido_index.html"
    echo "  - deployment/aikido_grammar.html"
    echo "  - README.md"
    echo ""
    echo "To generate them, run: ./scripts/generate-html.sh"
    exit 1
fi

echo "✓ All generated HTML files are present"
ls -lh deployment/technique_builder.html deployment/all_techniques.html deployment/all_unique.html
