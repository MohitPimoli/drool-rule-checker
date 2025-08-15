# 🔥 Drools Rule Checker - JetBrains Marketplace Description

## Short Description (160 characters max)
Professional IDE support for Drools (.drl) files with syntax highlighting, real-time validation, auto-completion, and intelligent error detection.

## Full Description (HTML format for marketplace)

```html
<h2>🔥 Transform Your Drools Development Experience</h2>

<p>Get professional IDE support for Drools Rule Language (.drl) files with comprehensive features that match modern Java development standards.</p>

<h3>✨ Key Features</h3>
<ul>
    <li><strong>🎨 Advanced Syntax Highlighting</strong> - Beautiful color coding for keywords, operators, strings, numbers, comments, and punctuation</li>
    <li><strong>🔍 Real-time Validation</strong> - Instant error detection as you type:
        <ul>
            <li>Missing brackets, parentheses, and braces</li>
            <li>Unclosed quotes and invalid escape sequences</li>
            <li>Missing semicolons in Java code blocks</li>
            <li>Invalid rule structure validation</li>
            <li>Common typos (wen → when, thn → then)</li>
        </ul>
    </li>
    <li><strong>💡 Smart Auto-completion</strong> - Context-aware suggestions for keywords, functions, and rule templates</li>
    <li><strong>📁 Code Organization</strong> - Code folding for rule blocks and comments</li>
    <li><strong>⚡ Performance Hints</strong> - Optimization suggestions for better rule performance</li>
    <li><strong>🎨 Customizable Colors</strong> - Personalize syntax highlighting in IDE settings</li>
</ul>

<h3>🚀 Perfect For</h3>
<ul>
    <li>Business Rules Developers</li>
    <li>Java Developers working with Drools</li>
    <li>Decision Management Teams</li>
    <li>Anyone writing .drl files</li>
</ul>

<h3>📋 Supported Drools Features</h3>
<ul>
    <li>Rule definitions with attributes (salience, no-loop, etc.)</li>
    <li>When-Then-End clause validation</li>
    <li>Java code blocks in 'then' clauses</li>
    <li>Field constraints and expressions</li>
    <li>Function calls and built-in operators</li>
    <li>Comments (single-line and multi-line)</li>
</ul>

<h3>💻 Example</h3>
<pre><code>rule "Customer Discount"
    salience 100
    when
        $customer : Customer(age > 18, status == "ACTIVE")
        $order : Order(total > 1000.00) from $customer.orders
    then
        $order.setDiscount(0.15);
        update($order);
        System.out.println("Discount applied!");
end</code></pre>

<p>The plugin validates syntax, highlights errors, and provides intelligent suggestions as you type.</p>

<h3>🔧 Installation</h3>
<ol>
    <li>Install from JetBrains Marketplace</li>
    <li>Restart IntelliJ IDEA</li>
    <li>Open any .drl file and enjoy enhanced editing!</li>
</ol>

<h3>🐛 Support</h3>
<p>Report issues or request features on <a href="https://github.com/MohitPimoli/drool-rule-checker/issues">GitHub</a></p>

<p><em>Made with ❤️ for the Drools community</em></p>
```

## Tags for Marketplace
- drools
- rules
- business-rules
- drl
- syntax-highlighting
- validation
- auto-completion
- ide
- java
- decision-engine

## Categories
- **Primary**: Languages
- **Secondary**: Code tools, Productivity

## Screenshots Needed

### 1. Main Feature Screenshot
- Show a .drl file with syntax highlighting
- Include error detection (red underlines)
- Show auto-completion popup
- Caption: "Professional syntax highlighting and real-time validation"

### 2. Error Detection Screenshot
- Show various types of errors being detected
- Include error tooltips/messages
- Caption: "Comprehensive error detection catches issues as you type"

### 3. Auto-completion Screenshot
- Show completion popup with keywords and functions
- Caption: "Smart auto-completion for keywords, functions, and templates"

### 4. Color Customization Screenshot
- Show the color settings page
- Caption: "Customize syntax colors to match your theme"

## Version History Template

```
Version 1.0.9 - Enhanced Syntax Validation
• 🆕 Advanced bracket matching validation
• 🆕 Java code block validation with semicolon detection
• 🆕 Enhanced string literal validation
• 🆕 Field access pattern validation
• 🆕 Function call syntax validation
• 🎨 Improved syntax highlighting for punctuation
• ⚡ Performance improvements

Version 1.0.8 - Foundation Release
• ✅ Basic syntax highlighting for .drl files
• ✅ Rule structure validation
• ✅ Common typo detection
• ✅ Auto-completion for keywords
• ✅ Code folding support
• ✅ Customizable color settings
```

## Pricing
- **Free** (Open Source)

## License
- MIT License

## Compatibility
- IntelliJ IDEA Community/Ultimate 2024.2+
- Other JetBrains IDEs with IntelliJ Platform 2024.2+

## Vendor Information
- **Name**: Mohit Pimoli
- **Email**: mohitpimoli31@gmail.com
- **Website**: https://github.com/MohitPimoli
- **Support**: https://github.com/MohitPimoli/drool-rule-checker/issues

## Additional Resources
- **Source Code**: https://github.com/MohitPimoli/drool-rule-checker
- **Documentation**: https://github.com/MohitPimoli/drool-rule-checker/blob/main/README.md
- **Issue Tracker**: https://github.com/MohitPimoli/drool-rule-checker/issues
- **Changelog**: https://github.com/MohitPimoli/drool-rule-checker/releases