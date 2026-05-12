# Drools Rule Checker — IntelliJ IDEA Plugin for DRL Files

[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://github.com/MohitPimoli/drool-rule-checker/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-2024.2+-purple.svg)](https://www.jetbrains.com/idea/)
[![Java](https://img.shields.io/badge/Java-17+-red.svg)](https://openjdk.org/)

**Full language support for Drools Rule Language (.drl) files in IntelliJ IDEA** — grammar-based parsing, context-aware code completion, go-to-definition, real-time validation, quick fixes, and more.

The most comprehensive Drools IDE plugin available. Write Drools rules with the same level of intelligent assistance you get for Java.

---

## Why Drools Rule Checker?

If you write Drools rules in IntelliJ IDEA, you've probably noticed there's no built-in support for `.drl` files. No syntax highlighting, no completion, no error checking. This plugin fixes that.

**Drools Rule Checker** gives you:
- A proper **grammar-based parser** that understands DRL structure
- **Code completion** that knows your project's classes, methods, and fields
- **Go-to-definition** — Ctrl+Click on class names to jump to Java source
- **Real-time validation** — see errors as you type, not at deploy time
- **Quick fixes** — one-click solutions for common problems

---

## Features

### Grammar-Based Parser with Error Recovery
Built on Grammar-Kit BNF and JFlex, the parser produces a full AST for all DRL constructs. If one rule has a syntax error, the rest of the file still parses correctly.

- Package declarations, imports, globals
- Rule blocks with attributes (salience, no-loop, agenda-group, etc.)
- When-clause patterns with fact types, binding variables, and constraints
- Conditional elements (not, exists, forall, from, collect, accumulate, eval)
- Then-clause Java/MVEL action code
- Function definitions, type declarations, queries

### Context-Aware Code Completion

| Context | What's Suggested |
|---------|-----------------|
| File top level | `package`, `import`, `global`, `rule`, `function`, `declare`, `query` |
| Inside rule (before when) | `salience`, `no-loop`, `agenda-group`, `ruleflow-group`, `timer`, etc. |
| Inside when-clause | `and`, `or`, `not`, `exists`, `forall`, `from`, `accumulate`, `eval` |
| Inside then-clause | `insert`, `update`, `modify`, `retract`, `delete`, `drools.halt` |
| After `$variable.` | Methods and fields of the bound type |
| At type positions | Classes from project classpath with auto-import |
| Inside constraints | Bean properties of the fact type |

### Go-To-Definition (Ctrl+Click)
- Click on a **class name** → navigate to Java source or decompiled class
- Click on a **method call** → navigate to the method definition
- Click on a **binding variable** (`$customer`) → navigate to its declaration in the when-clause

### Real-Time Validation
| Check | Severity |
|-------|----------|
| Missing when/then/end clauses | Error |
| Incorrect clause order | Error |
| Unresolvable class in fact pattern | Error |
| Unresolvable import | Error |
| Unbalanced parentheses in constraints | Error |
| Invalid salience value (non-numeric) | Error |
| Invalid date format in attributes | Error |
| Unresolvable global type | Error |
| Duplicate rule names | Warning |
| Unused binding variables | Warning |
| Duplicate imports | Warning |
| Unused imports | Weak Warning |

### Quick Fixes
- **Add missing import** — one click to insert the import statement
- **Remove unused import** — clean up unused imports
- **Rename duplicate rule** — auto-append numeric suffix
- **Add missing semicolon** — insert `;` at the right position
- **Remove unused binding** — convert `$var : Type()` to `Type()`

### Structure View
Outline panel showing all rules, queries, functions, and declarations. Click to navigate.

### Code Folding
Collapse rule blocks, import groups, declare blocks, queries, functions, and multi-line comments.

### Bracket Matching & Commenter
- Automatic bracket matching for `()`, `{}`, `[]`
- Toggle line comments (`//`) and block comments (`/* */`) with keyboard shortcuts

---

## Installation

### From JetBrains Marketplace (Recommended)
1. Open IntelliJ IDEA
2. Go to **File → Settings → Plugins**
3. Search for **"Drools Rule Checker"**
4. Click **Install** and restart

### Manual Installation
1. Download the latest `.zip` from [GitHub Releases](https://github.com/MohitPimoli/drool-rule-checker/releases)
2. Go to **Settings → Plugins → ⚙️ → Install Plugin from Disk...**
3. Select the ZIP file and restart

---

## Usage Example

```drools
package com.example.rules;

import com.example.model.Customer;
import com.example.model.Order;

global org.slf4j.Logger log;

rule "Apply Premium Discount"
    salience 100
    no-loop true
    when
        $customer : Customer(loyaltyTier == "PREMIUM", active == true)
        $order : Order(total > 500.00) from $customer.getOrders()
    then
        double discount = $order.getTotal() * 0.20;
        $order.setDiscount(discount);
        update($order);
        log.info("Applied 20% discount of {} to order {}", discount, $order.getId());
end
```

With this plugin installed, you get:
- Syntax highlighting for all elements
- Completion after `$customer.` showing `Customer` methods
- Completion after `$order.` showing `Order` methods
- Ctrl+Click on `Customer` navigates to the Java class
- Warning if `$customer` is declared but never used
- Error if `Customer` class can't be resolved on classpath

---

## Supported Drools Constructs

- ✅ Package declarations
- ✅ Import statements (including wildcard)
- ✅ Global declarations
- ✅ Rule definitions with all attributes
- ✅ When-clause patterns and constraints
- ✅ Binding variables (`$var : Type()`)
- ✅ Conditional elements (not, exists, forall, from, collect, accumulate, eval)
- ✅ Then-clause Java/MVEL code
- ✅ Function definitions
- ✅ Type declarations (declare blocks)
- ✅ Query definitions

---

## Building from Source

### Prerequisites
- Java 17+
- Gradle 8.0+ (wrapper included)
- IntelliJ IDEA 2024.2+ (for running/debugging)

### Build Commands
```bash
git clone https://github.com/MohitPimoli/drool-rule-checker.git
cd drool-rule-checker

# Build the plugin
./gradlew clean buildPlugin

# Run in a sandboxed IDE for testing
./gradlew runIde

# Run tests
./gradlew test
```

### Project Structure
```
src/main/
├── grammars/
│   ├── Drools.bnf          # Grammar-Kit BNF grammar
│   └── Drools.flex         # JFlex lexer specification
├── gen/                     # Generated parser and PSI classes
├── java/com/plugin/drool/
│   ├── DroolsLanguage.java
│   ├── DroolsParserDefinition.java
│   ├── DroolsLexerAdapter.java
│   ├── DroolsAnnotator.java           # PSI-based validation
│   ├── DroolsCompletionContributor.java # Context-aware completion
│   ├── DroolsReferenceContributor.java  # Go-to-definition
│   ├── DroolsStructureViewFactory.java  # Outline panel
│   ├── DroolsFoldingBuilder.java        # Code folding
│   ├── DroolsResolutionCache.java       # Class resolution cache
│   ├── psi/mixin/                       # Typed PSI mixins
│   └── fixes/                           # Quick-fix actions
└── resources/META-INF/
    └── plugin.xml
```

---

## Compatibility

| IDE | Version |
|-----|---------|
| IntelliJ IDEA Community | 2024.2+ |
| IntelliJ IDEA Ultimate | 2024.2+ |

Requires the Java plugin to be enabled (for classpath resolution and go-to-definition).

---

## Contributing

Contributions welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

- 🐛 [Report bugs](https://github.com/MohitPimoli/drool-rule-checker/issues)
- 💡 [Request features](https://github.com/MohitPimoli/drool-rule-checker/issues)
- 🔧 Submit pull requests

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

## Links

- [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/26requirement-drool-rule-checker)
- [GitHub Repository](https://github.com/MohitPimoli/drool-rule-checker)
- [Issue Tracker](https://github.com/MohitPimoli/drool-rule-checker/issues)
- [Changelog](CHANGELOG.md)

---

**Keywords**: Drools, DRL, IntelliJ IDEA plugin, Drools IDE, Drools editor, Drools syntax highlighting, Drools code completion, Drools validation, rule engine, business rules, JBoss Drools, KIE, .drl files, Drools IntelliJ, Drools JetBrains
