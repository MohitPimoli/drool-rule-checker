# Drools Rule Checker

An IntelliJ IDEA plugin that adds basic syntax highlighting, linting, and editor assistance for **Drools** (`.drl`) files.

## Features

* 🔹 File type recognition for `.drl`
* 🔹 Syntax highlighting (keywords, strings, numbers, comments, operators)
* 🔹 On-the-fly annotations for common mistakes (e.g., missing `when/then/end`, typos like `wen`, `thn`)
* 🔹 Simple code completion stubs (if enabled in `DroolsCompletionContributor`)
* 🔹 Folding for long blocks (if enabled in `DroolsFoldingBuilder`)

## Installation

### From source (development)

1. Clone the repo

   ```bash
   git clone https://github.com/<you>/drool-rule-checker.git
   cd drool-rule-checker
   ```
2. Run in IDE sandbox

   ```bash
   ./gradlew runIde
   ```
3. Build distribution zip

   ```bash
   ./gradlew buildPlugin
   ```

   The plugin zip will be under `build/distributions/`.

### Install locally in IntelliJ

* `Settings/Preferences → Plugins → ⚙ → Install Plugin from Disk…`
* Select the ZIP from `build/distributions/`
* Restart IDE.

## Usage

* Open any `.drl` file; the plugin activates automatically.
* Errors and warnings appear inline via the annotator.
* Use standard IntelliJ “Toggle Line Comment” for comments.

## Project Structure (simplified)

```
src/main/java/com/plugin/drool/
  ├─ DroolsLanguage.java                // Language singleton
  ├─ DroolsFileType.java                // Maps .drl to language + file icon
  ├─ DroolsLexer.java                   // Regex-based lexer
  ├─ DroolsParser.java                  // (Stub/minimal) PSI parser
  ├─ DroolsParserDefinition.java        // Wires lexer/parser + token sets
  ├─ DroolsSyntaxHighlighter*.java      // Token → text attributes
  ├─ DroolsAnnotator.java               // Quick syntax validations
  ├─ DroolsCompletionContributor.java   // (Optional) completions
  └─ util/                              // Patterns & constants
src/main/resources/
  ├─ META-INF/
  │   ├─ plugin.xml
  │   ├─ pluginIcon.svg                 // Marketplace/Installed Plugins icon
  │   └─ pluginIcon_dark.svg            // (optional)
  └─ assets/icons/
      ├─ Drool.png                      // 16×16 file icon
      └─ Drool@2x.png                   // 32×32 (HiDPI)
```

## Plugin Icon (Marketplace / Installed Plugins)

Place icons here:

* `src/main/resources/META-INF/pluginIcon.svg`
* `src/main/resources/META-INF/pluginIcon_dark.svg` (optional)

> The small file tab / Project view icon is separate: keep `assets/icons/Drool.png` (16×16) and `Drool@2x.png` (32×32), referenced by `DroolsFileType#getIcon()`.

## Build Configuration

This project uses the **Gradle IntelliJ Plugin**. Typical `build.gradle` snippets:

```gradle
plugins {
    id "java"
    id "org.jetbrains.intellij" version "1.17.2"
}

intellij {
    version = "2023.3"    // target IDE
    plugins = []          // add deps if needed, e.g., "java"
}

tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}
```

## Roadmap

* More accurate lexer/parser rules
* Quick fixes for common issues
* Rule template/live templates
* Better completion for patterns/constraints

## Contributing

Issues and PRs are welcome!
Please include a short description and screenshots where relevant.

## License

MIT (see `LICENSE`).

---

**Tip:** If your icon doesn’t show on the Marketplace/Installed tab, ensure `pluginIcon.svg` exists under `META-INF/` and rebuild with `./gradlew buildPlugin`.
