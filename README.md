# 🔥 Drools Rule Checker

[![JetBrains Plugin](https://img.shields.io/badge/JetBrains-Plugin-orange.svg)](https://plugins.jetbrains.com/plugin/your-plugin-id)
[![Version](https://img.shields.io/badge/version-1.0.9-blue.svg)](https://github.com/MohitPimoli/drool-rule-checker/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Downloads](https://img.shields.io/badge/downloads-1K+-brightgreen.svg)](https://plugins.jetbrains.com/plugin/your-plugin-id)

**Professional IDE support for Drools Rule Language (.drl) files in IntelliJ IDEA**

Transform your Drools development experience with comprehensive IDE features including syntax highlighting, real-time validation, auto-completion, and intelligent error detection - just like you'd expect from modern Java development.

![Plugin Demo](docs/images/demo.gif)

## ✨ Features

### 🎨 **Advanced Syntax Highlighting**
- Beautiful color coding for all Drools elements
- Keywords, operators, strings, numbers, comments
- Punctuation and bracket highlighting
- Customizable color schemes

### 🔍 **Real-time Validation & Error Detection**
- **Bracket Matching**: Detects unmatched `()`, `{}`, `[]`
- **Quote Validation**: Finds unclosed strings and invalid escapes
- **Semicolon Detection**: Missing `;` in Java code blocks
- **Rule Structure**: Validates `rule → when → then → end` sequence
- **Typo Detection**: Catches common mistakes (`wen` → `when`, `thn` → `then`)
- **Expression Validation**: Validates constraint expressions and field access

### 💡 **Smart Auto-completion**
- Context-aware keyword suggestions
- Built-in function completion with parameter hints
- Rule templates for quick scaffolding
- Drools operators and attributes

### 📁 **Code Navigation & Organization**
- Code folding for rule blocks and comments
- Intelligent bracket matching
- Structure view support

## 🚀 Quick Start

### Installation from JetBrains Marketplace
1. Open IntelliJ IDEA
2. Go to `File → Settings → Plugins`
3. Search for "Drools Rule Checker"
4. Click **Install** and restart IDE
5. Open any `.drl` file and enjoy enhanced editing!

### Manual Installation
1. Download the latest release from [GitHub Releases](https://github.com/MohitPimoli/drool-rule-checker/releases)
2. Go to `Settings → Plugins → ⚙️ → Install Plugin from Disk...`
3. Select the downloaded ZIP file
4. Restart IntelliJ IDEA

## 📋 Usage Examples

### ✅ Valid Drools Rule
```drools
rule "Customer Discount"
    salience 100
    no-loop true
    when
        $customer : Customer(age > 18, status == "ACTIVE")
        $order : Order(total > 1000.00) from $customer.orders
    then
        $order.setDiscount(0.15);
        update($order);
        System.out.println("Discount applied to: " + $customer.getName());
end
```

### ❌ Common Errors Detected
```drools
rule "Invalid Rule"
    when
        $customer : Customer(age > 18
        // ❌ Missing closing parenthesis
    then
        $order.setDiscount(0.15)
        // ❌ Missing semicolon
        update($order)
        // ❌ Missing semicolon
end
```

## 🛠️ Development

### Prerequisites
- IntelliJ IDEA 2024.2+
- Java 17+
- Gradle 8.0+

### Building from Source
```bash
# Clone the repository
git clone https://github.com/MohitPimoli/drool-rule-checker.git
cd drool-rule-checker

# Run in development mode
./gradlew runIde

# Build plugin distribution
./gradlew buildPlugin

# Run tests
./gradlew test
```

### Project Structure
```
src/main/java/com/plugin/drool/
├── DroolsLanguage.java              # Language definition
├── DroolsFileType.java              # File type association
├── DroolsLexer.java                 # Tokenization
├── DroolsParser.java                # AST parsing
├── DroolsAnnotator.java             # Real-time validation
├── DroolsSyntaxHighlighter.java     # Syntax highlighting
├── DroolsCompletionContributor.java # Auto-completion
├── DroolsFoldingBuilder.java        # Code folding
└── util/
    ├── DroolsConstants.java         # Language constants
    └── Pattern.java                 # Validation patterns
```

## 🎯 Supported Drools Features

- ✅ Rule definitions with attributes
- ✅ When-Then-End clause validation
- ✅ Java code blocks in actions
- ✅ Field constraints and expressions
- ✅ Built-in functions and operators
- ✅ Comments (single-line and multi-line)
- ✅ Variable binding (`$variable`)
- ✅ Function calls with parameter validation

## 📊 Validation Features

| Feature | Description | Severity |
|---------|-------------|----------|
| Bracket Matching | Unmatched `()`, `{}`, `[]` | Error |
| Missing Semicolons | Java statements without `;` | Error |
| Unclosed Strings | Missing closing quotes | Error |
| Rule Structure | Invalid rule sequence | Error |
| Typos | Common keyword mistakes | Error |
| Field Access | Complex field chains | Warning |
| Performance | `eval()` usage hints | Weak Warning |

## 🎨 Customization

Customize syntax highlighting colors:
1. Go to `Settings → Editor → Color Scheme → Drools`
2. Adjust colors for different token types
3. Apply and enjoy your personalized theme!

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **🐛 Report Bugs**: [Create an issue](https://github.com/MohitPimoli/drool-rule-checker/issues)
2. **💡 Suggest Features**: Share your ideas in discussions
3. **🔧 Submit PRs**: Fork, develop, and submit pull requests
4. **📝 Improve Docs**: Help us improve documentation
5. **⭐ Star the Project**: Show your support!

### Development Guidelines
- Follow existing code style and patterns
- Add tests for new functionality
- Update documentation for API changes
- Ensure backward compatibility

## 📈 Roadmap

### 🔮 Upcoming Features
- [ ] **Advanced Parser**: Full grammar-based parsing
- [ ] **Semantic Analysis**: Type checking and variable resolution
- [ ] **Refactoring Support**: Rename, extract rule, etc.
- [ ] **Live Templates**: Predefined code snippets
- [ ] **Drools Integration**: Runtime validation
- [ ] **Performance Profiling**: Rule execution insights
- [ ] **Unit Test Support**: Test framework integration

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- JetBrains for the excellent IntelliJ Platform SDK
- Drools community for inspiration and feedback
- Contributors who help improve this plugin

## 📞 Support

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/MohitPimoli/drool-rule-checker/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/MohitPimoli/drool-rule-checker/discussions)
- 📧 **Email**: mohitpimoli31@gmail.com
- 🌟 **Rate & Review**: [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/your-plugin-id)

---

<div align="center">

**Made with ❤️ for the Drools community**

[⭐ Star on GitHub](https://github.com/MohitPimoli/drool-rule-checker) • 
[📥 Download Plugin](https://plugins.jetbrains.com/plugin/your-plugin-id) • 
[🐛 Report Issues](https://github.com/MohitPimoli/drool-rule-checker/issues)

</div>