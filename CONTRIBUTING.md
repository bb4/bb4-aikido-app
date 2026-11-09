# Contributing to bb4-aikido-app

## Generating HTML Files

The application generates three HTML files that must be committed to the repository:
- `deployment/technique_builder.html` - Interactive technique builder application
- `deployment/all_techniques.html` - Complete listing of all techniques
- `deployment/all_unique.html` - Listing with unique images only

### When to Regenerate

Regenerate these files whenever you modify:
- `deployment/techniques/techniques.xml` - The technique data
- Generator source code in `scala-source/com/barrybecker4/aikido/`

### How to Generate Locally

**Requirements**: JDK 25+

```bash
# Generate the HTML files
./scripts/generate-html.sh

# Verify all files were created
./scripts/verify-generated-html.sh

# Commit the generated files
git add deployment/*.html
git commit -m "Regenerate HTML files"
```

### Automated Generation

The CI workflow includes:
1. **Build verification** - Ensures the generator compiles and runs
2. **HTML generation** - Runs the generator after successful build
3. **File verification** - Confirms all expected files were created

If you don't have JDK 25 locally, the CI will verify generation works correctly.

## Testing

Run the test suite:
```bash
./gradlew test
```

## Build

Build the project:
```bash
./gradlew build
```
