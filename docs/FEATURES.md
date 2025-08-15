# 🚀 Drools Rule Checker - Complete Feature Guide

## 🎯 Overview

The Drools Rule Checker plugin transforms IntelliJ IDEA into a powerful IDE for Drools development, providing the same level of intelligent assistance you expect from modern Java development.

## 🔍 Validation Features

### 1. **Bracket & Parentheses Matching**
```drools
rule "Example"
    when
        Customer(age > 18, (status == "ACTIVE" || status == "PREMIUM")
        // ❌ ERROR: Missing closing parenthesis
    then
        // actions
end
```

**What it catches:**
- Unmatched opening brackets: `(`, `{`, `[`
- Unmatched closing brackets: `)`, `}`, `]`
- Nested bracket mismatches
- Real-time highlighting as you type

### 2. **Java Code Block Validation**
```drools
rule "Java Validation"
    when
        $customer : Customer()
    then
        $customer.setStatus("ACTIVE")  // ❌ Missing semicolon
        System.out.println("Updated")  // ❌ Missing semicolon
        
        if ($customer.getAge() > 65)   // ❌ Missing braces
            $customer.setDiscount(0.1)
end
```

**What it validates:**
- Missing semicolons in Java statements
- Incomplete if/for/while statements
- Proper Java syntax in `then` clauses

### 3. **String Literal Validation**
```drools
rule "String Validation"
    when
        $customer : Customer(name == "John Doe)  // ❌ Unclosed quote
        $order : Order(status == "ACTIVE\z")     // ❌ Invalid escape
    then
        // actions
end
```

**What it detects:**
- Unclosed string literals
- Invalid escape sequences
- Multi-line string issues

### 4. **Rule Structure Validation**
```drools
rule "Structure Example"
    then                    // ❌ Missing 'when' clause
        // actions
    when                    // ❌ Wrong order: 'when' after 'then'
        // conditions
// ❌ Missing 'end' keyword
```

**What it enforces:**
- Proper rule sequence: `rule → when → then → end`
- Required clauses presence
- Correct clause ordering

### 5. **Field Access & Expression Validation**
```drools
rule "Expression Validation"
    when
        $customer : Customer(age = 18)           // ❌ Use '==' not '='
        $order : Order($customer.address.city)   // ⚠️ Complex field access
        $total : Number() from accumulate(...)   // ✅ Valid
    then
        // actions
end
```

**What it validates:**
- Comparison operators (`==` vs `=`)
- Field access patterns
- Complex expression syntax

## 🎨 Syntax Highlighting

### Color-Coded Elements
- **Keywords**: `rule`, `when`, `then`, `end`, `import`, etc.
- **Operators**: `==`, `!=`, `>`, `<`, `&&`, `||`, etc.
- **Strings**: `"quoted text"`
- **Numbers**: `123`, `45.67`
- **Comments**: `// single line` and `/* multi-line */`
- **Variables**: `$customer`, `$order`
- **Punctuation**: `()`, `{}`, `[]`, `;`, `,`, `.`

### Customizable Themes
Access via `Settings → Editor → Color Scheme → Drools`:
- Adjust colors for any token type
- Support for light and dark themes
- Export/import color schemes

## 💡 Auto-completion Features

### 1. **Keyword Completion**
Type partial keywords and get suggestions:
- `ru` → `rule`
- `wh` → `when`
- `th` → `then`

### 2. **Function Completion**
Built-in Drools functions with parameter hints:
- `insert()` → `insert(object)`
- `update()` → `update(object)`
- `retract()` → `retract(object)`

### 3. **Rule Templates**
Quick scaffolding for common patterns:
```drools
rule_template_1 →
rule "Rule Name"
when
    // conditions
then
    // actions
end
```

### 4. **Operator Completion**
Drools-specific operators:
- `matches`
- `contains`
- `memberOf`
- `soundslike`

## 📁 Code Organization

### 1. **Code Folding**
- Collapse rule blocks for better navigation
- Fold multi-line comments
- Fold consecutive single-line comments (3+ lines)

### 2. **Structure View**
- Navigate between rules quickly
- See rule hierarchy
- Jump to specific rule sections

## ⚡ Performance Features

### 1. **Performance Hints**
```drools
rule "Performance Example"
    when
        eval($customer.getAge() > 18 && $customer.getStatus().equals("ACTIVE"))
        // ⚠️ Consider using direct field constraints for better performance
    then
        // actions
end
```

### 2. **Best Practice Suggestions**
- Avoid `eval()` when possible
- Use field constraints instead of complex expressions
- Optimize rule salience usage

## 🔧 Error Recovery

### Real-time Validation
- Errors appear as you type
- No need to compile to see issues
- Precise error positioning
- Helpful error messages with suggestions

### Error Severity Levels
- **🔴 Error**: Syntax errors that prevent compilation
- **🟡 Warning**: Potential issues or improvements
- **🔵 Info**: Style suggestions and optimizations

## 🎯 IDE Integration

### File Type Recognition
- Automatic `.drl` file association
- Custom file icon in project tree
- Proper syntax highlighting activation

### Editor Features
- Line commenting with `Ctrl+/`
- Block commenting with `Ctrl+Shift+/`
- Bracket matching and navigation
- Code formatting (basic)

## 📊 Validation Summary

| Validation Type | Real-time | Error Level | Auto-fix |
|----------------|-----------|-------------|----------|
| Bracket Matching | ✅ | Error | ❌ |
| Missing Semicolons | ✅ | Error | ❌ |
| Unclosed Strings | ✅ | Error | ❌ |
| Rule Structure | ✅ | Error | ❌ |
| Typo Detection | ✅ | Error | ✅ |
| Field Access | ✅ | Warning | ❌ |
| Performance Hints | ✅ | Info | ❌ |

## 🔮 Coming Soon

### Advanced Features (Roadmap)
- **Auto-fix suggestions**: One-click error resolution
- **Refactoring support**: Rename variables, extract rules
- **Advanced parser**: Full grammar-based parsing
- **Semantic analysis**: Type checking and validation
- **Live templates**: More rule templates
- **Integration**: Connect with Drools runtime

---

*This feature guide covers version 1.0.9. Features may vary in different versions.*