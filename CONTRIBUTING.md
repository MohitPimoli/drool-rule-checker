# 🤝 Contributing to Drools Rule Checker

Thank you for your interest in contributing to the Drools Rule Checker plugin! We welcome contributions from the community and are grateful for any help you can provide.

## 🎯 Ways to Contribute

### 🐛 **Report Bugs**
- Use the [GitHub Issues](https://github.com/MohitPimoli/drool-rule-checker/issues) page
- Search existing issues before creating a new one
- Provide detailed information about the bug
- Include steps to reproduce the issue

### 💡 **Suggest Features**
- Use [GitHub Discussions](https://github.com/MohitPimoli/drool-rule-checker/discussions) for feature requests
- Explain the use case and benefits
- Provide examples if possible

### 🔧 **Submit Code Changes**
- Fork the repository
- Create a feature branch
- Make your changes
- Submit a pull request

### 📝 **Improve Documentation**
- Fix typos or unclear explanations
- Add examples and use cases
- Improve code comments
- Update README or guides

## 🚀 Getting Started

### Prerequisites
- **IntelliJ IDEA**: 2024.2+ (Community or Ultimate)
- **Java**: JDK 17 or higher
- **Gradle**: 8.0+ (included via wrapper)
- **Git**: For version control

### Development Setup

1. **Fork & Clone**
   ```bash
   git clone https://github.com/YOUR_USERNAME/drool-rule-checker.git
   cd drool-rule-checker
   ```

2. **Open in IntelliJ**
   - Open the project in IntelliJ IDEA
   - Wait for Gradle sync to complete
   - Ensure JDK 17+ is configured

3. **Run in Development Mode**
   ```bash
   ./gradlew runIde
   ```
   This opens a new IntelliJ instance with your plugin loaded.

4. **Build Plugin**
   ```bash
   ./gradlew buildPlugin
   ```
   Generated ZIP will be in `build/distributions/`

## 📋 Development Guidelines

### Code Style
- Follow existing code patterns and conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise
- Use proper indentation (2 spaces)

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

### Adding New Features

#### 1. **New Validation Rules**
- Add patterns to `Pattern.java`
- Implement validation in `DroolsAnnotator.java`
- Test with various Drools code samples

#### 2. **New Token Types**
- Add to `DroolsTokenTypes.java`
- Update `DroolsLexer.java` to recognize tokens
- Add highlighting in `DroolsSyntaxHighlighter.java`

#### 3. **New Auto-completion**
- Add keywords/functions to `DroolsConstants.java`
- Implement completion in `DroolsCompletionContributor.java`

### Testing Your Changes

1. **Manual Testing**
   ```bash
   ./gradlew runIde
   ```
   - Create test `.drl` files
   - Verify syntax highlighting works
   - Test validation features
   - Check auto-completion

2. **Build Verification**
   ```bash
   ./gradlew buildPlugin
   ```
   - Ensure plugin builds without errors
   - Test the generated ZIP file

3. **Test Cases to Cover**
   - Valid Drools rules
   - Invalid/incomplete rules
   - Edge cases (empty files, comments only)
   - Performance with large files

## 📝 Pull Request Process

### Before Submitting
- [ ] Code follows project style guidelines
- [ ] Changes have been tested manually
- [ ] No new compiler warnings or errors
- [ ] Documentation updated if needed
- [ ] Commit messages are clear and descriptive

### PR Template
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Performance improvement

## Testing
- [ ] Tested manually in development IDE
- [ ] Verified plugin builds successfully
- [ ] No regression in existing features

## Screenshots (if applicable)
Add screenshots showing the changes in action
```

### Review Process
1. **Automated Checks**: GitHub Actions will run basic checks
2. **Code Review**: Maintainers will review your code
3. **Testing**: Changes will be tested in different scenarios
4. **Merge**: Once approved, changes will be merged

## 🐛 Bug Report Template

When reporting bugs, please include:

```markdown
**Bug Description**
Clear description of what the bug is

**Steps to Reproduce**
1. Go to '...'
2. Click on '....'
3. See error

**Expected Behavior**
What you expected to happen

**Screenshots**
If applicable, add screenshots

**Environment**
- IntelliJ IDEA version: [e.g. 2024.2.1]
- Plugin version: [e.g. 1.0.10]
- OS: [e.g. Windows 11, macOS 14, Ubuntu 22.04]
- Java version: [e.g. JDK 17]

**Additional Context**
Any other context about the problem
```

## 💡 Feature Request Template

```markdown
**Feature Description**
Clear description of the feature you'd like

**Use Case**
Explain why this feature would be useful

**Proposed Solution**
Describe how you envision this working

**Alternatives Considered**
Other solutions you've considered

**Additional Context**
Any other context or screenshots
```

## 🏆 Recognition

Contributors will be:
- Listed in the project README
- Mentioned in release notes
- Added to the contributors section

## 📞 Getting Help

- **Questions**: Use [GitHub Discussions](https://github.com/MohitPimoli/drool-rule-checker/discussions)
- **Chat**: Email mohitpimoli31@gmail.com
- **Documentation**: Check existing docs and code comments

## 📋 Code of Conduct

### Our Pledge
We pledge to make participation in our project a harassment-free experience for everyone, regardless of age, body size, disability, ethnicity, gender identity and expression, level of experience, nationality, personal appearance, race, religion, or sexual identity and orientation.

### Our Standards
- Using welcoming and inclusive language
- Being respectful of differing viewpoints
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

### Enforcement
Instances of abusive, harassing, or otherwise unacceptable behavior may be reported by contacting the project team at mohitpimoli31@gmail.com.

## 🎉 Thank You!

Your contributions help make the Drools development experience better for everyone. Whether you're fixing a typo, adding a feature, or reporting a bug, every contribution matters!

---

**Happy Contributing!** 🚀