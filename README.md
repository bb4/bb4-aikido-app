# bb4-aikido-app

A Scala application that generates an HTML/JavaScript aikido technique builder. A [live copy](https://barrybecker4.org/interests/aikido/technique_builder.html) is hosted as static files on [barrybecker4.org](https://barrybecker4.org).

## Prerequisites

- **JDK 25+** (bb4-gradle 2.1)
- **[bb4-common](https://github.com/bb4/bb4-common)** built and installed in the local Maven repository (`mvn install` or `./gradlew publishToMavenLocal` in that repo), because this project resolves dependencies from `mavenLocal()`.

## Build

From the project root:

```bash
./gradlew build
```

This compiles the generator, runs the test suite, and assembles the JAR. CI runs the same command.

## Generate the static site

The runnable main class is `com.barrybecker4.aikido.AikidoAppGenerator`. It reads a technique hierarchy XML file and writes HTML into `deployment/`:

| Output | Purpose |
|--------|---------|
| `deployment/technique_builder.html` | Interactive technique builder (main app) |
| `deployment/all_techniques.html` | Full hierarchy in one page (debugging) |
| `deployment/all_unique.html` | Unique images only (debugging) |

Default input is `deployment/techniques/techniques.xml`. To use another file:

```bash
./gradlew run --args="path/to/techniques.xml"
```

**Content workflow** (see also comments in `AikidoAppGenerator.scala`):

1. Edit `deployment/techniques/techniques.xml` (DTD: `scala-source/com/barrybecker4/aikido/hierarchy.dtd`).
2. Add photos as `deployment/techniques/images/<node-id>.jpg` (one JPEG per `id` attribute).
3. Run `./gradlew run` to regenerate the HTML.

The root `<hierarchy>` element’s `imgpath` attribute must match where images live **relative to the published HTML** (the repo default is `techniques/images/`).

### Local preview

Serve or open the generated pages with paths unchanged—for example:

```bash
cd deployment && python3 -m http.server 8080
```

Then open `http://localhost:8080/technique_builder.html`. Keep `aikido.css`, `images/select.png`, and the other static files in `deployment/` alongside the generated pages.

## Deploy to a static website

There is no server-side code: upload the `deployment/` tree so relative links keep working (`aikido.css`, `techniques/images/…`, sibling `.html` pages, and UI assets under `images/`).

**Typical upload set**

- Generated: `technique_builder.html`, and optionally `all_techniques.html` / `all_unique.html`
- Static pages already in the repo: `aikido_index.html`, `aikido_grammar.html`, `technique_builder_desc.html`, `testing_thoughts.html`, `aikido.css`
- Data: `deployment/techniques/images/*.jpg` (and any other assets referenced by your `imgpath`)

Use any static host (GitHub Pages, S3 + CloudFront, Netlify, FTP, etc.): copy the files into the site’s document root or a subdirectory, preserving the same directory layout as in `deployment/`.

**barrybecker4.org (GitHub Pages via [bb4-website](https://github.com/bb4/bb4-website))**

Published files live under `source/html/barrybecker4/interests/aikido/`. Technique JPEGs on the live site are under `images/techniques/`, so set `imgpath="images/techniques/"` on the root `<hierarchy>` in `techniques.xml` before `./gradlew run`, then copy the updated HTML, CSS, and images into that folder in `bb4-website` and push. The site README describes the [GitHub Pages deploy flow](https://github.com/bb4/bb4-website#steps-to-deploy-using-github-pages).

> **Note:** The bb4 Gradle `deploy` task copies from a `source/html/` tree used by other projects. This app uses `deployment/` instead; `./gradlew run` plus uploading `deployment/` is the intended path for the technique builder.
