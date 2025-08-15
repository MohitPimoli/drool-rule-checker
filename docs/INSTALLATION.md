# 📥 Installation Guide - Drools Rule Checker

## 🚀 Quick Installation (Recommended)

### From JetBrains Marketplace
1. **Open IntelliJ IDEA**
2. **Navigate to Plugins**:
   - `File → Settings → Plugins` (Windows/Linux)
   - `IntelliJ IDEA → Preferences → Plugins` (macOS)
3. **Search for Plugin**:
   - Click on "Marketplace" tab
   - Search for "Drools Rule Checker"
4. **Install**:
   - Click "Install" button
   - Wait for download to complete
5. **Restart IDE**:
   - Click "Restart IDE" when prompted
   - Plugin will be active after restart

### Verification
After restart, open any `.drl` file to verify installation:
- File should have syntax highlighting
- Drools icon should appear in file tab
- Error detection should work in real-time

## 🔧 Manual Installation

### Option 1: From GitHub Releases
1. **Download Plugin**:
   - Go to [GitHub Releases](https://github.com/MohitPimoli/drool-rule-checker/releases)
   - Download the latest `.zip` file (e.g., `drool-rule-checker-1.0.9.zip`)

2. **Install in IntelliJ**:
   - Open IntelliJ IDEA
   - Go to `Settings → Plugins`
   - Click gear icon (⚙️) → "Install Plugin from Disk..."
   - Select the downloaded ZIP file
   - Click "OK"

3. **Restart IDE**:
   - Restart IntelliJ IDEA
   - Plugin will be active after restart

### Option 2: Build from Source
```bash
# Clone repository
git clone https://github.com/MohitPimoli/drool-rule-checker.git
cd drool-rule-checker

# Build plugin
./gradlew buildPlugin

# Install the generated ZIP
# File will be in: build/distributions/drool-rule-checker-1.0.9.zip
```

Then follow the manual installation steps above with the generated ZIP file.

## 🔍 System Requirements

### Supported IDEs
- **IntelliJ IDEA Community Edition** 2024.2+
- **IntelliJ IDEA Ultimate Edition** 2024.2+
- **Other JetBrains IDEs** (with IntelliJ Platform 2024.2+):
  - PyCharm Professional
  - WebStorm
  - PhpStorm
  - RubyMine
  - CLion
  - GoLand

### System Requirements
- **Java**: JDK 17 or higher
- **Memory**: Minimum 2GB RAM (4GB+ recommended)
- **Disk Space**: ~10MB for plugin files
- **OS**: Windows, macOS, Linux (any OS supported by IntelliJ)

## ⚙️ Configuration

### Initial Setup
No additional configuration required! The plugin works out of the box:
- `.drl` files are automatically recognized
- Syntax highlighting is enabled by default
- All validation features are active

### Optional Customization

#### 1. Customize Syntax Colors
```
Settings → Editor → Color Scheme → Drools
```
Available customizations:
- Keywords (rule, when, then, etc.)
- Identifiers (variable names)
- Strings and numbers
- Comments
- Operators and punctuation
- Brackets and parentheses

#### 2. Adjust Error Highlighting
```
Settings → Editor → Inspections → Drools
```
You can:
- Enable/disable specific validations
- Change error severity levels
- Configure inspection scopes

#### 3. File Association (if needed)
```
Settings → Editor → File Types
```
The plugin automatically associates `.drl` files, but you can:
- Add additional file extensions
- Modify file type patterns
- Change file icons

## 🔄 Updates

### Automatic Updates
- IntelliJ will notify you of plugin updates
- Updates are installed automatically from the marketplace
- Restart IDE when prompted

### Manual Update Check
```
Settings → Plugins → Installed → Drools Rule Checker → Update
```

### Version History
Check [GitHub Releases](https://github.com/MohitPimoli/drool-rule-checker/releases) for:
- Version changelog
- New features
- Bug fixes
- Breaking changes

## 🐛 Troubleshooting

### Common Issues

#### Plugin Not Working
**Symptoms**: No syntax highlighting, no error detection
**Solutions**:
1. Verify plugin is enabled: `Settings → Plugins → Installed`
2. Check file extension is `.drl`
3. Restart IntelliJ IDEA
4. Reinstall plugin if needed

#### Syntax Highlighting Missing
**Symptoms**: File opens but no colors
**Solutions**:
1. Check file type association: `Settings → Editor → File Types`
2. Verify `.drl` is associated with "Drools" file type
3. Try closing and reopening the file

#### Performance Issues
**Symptoms**: IDE becomes slow with `.drl` files
**Solutions**:
1. Increase IDE memory: `Help → Change Memory Settings`
2. Disable unused plugins: `Settings → Plugins`
3. Check file size (very large files may be slow)

#### Error Detection Not Working
**Symptoms**: No red underlines for errors
**Solutions**:
1. Check inspections are enabled: `Settings → Editor → Inspections → Drools`
2. Verify error highlighting is enabled: `Settings → Editor → General → Error Highlighting`
3. Try typing in the file to trigger validation

### Getting Help

#### 1. Check Documentation
- [Feature Guide](FEATURES.md)
- [GitHub README](../README.md)
- [Development Guide](../DEVELOPMENT.md)

#### 2. Search Existing Issues
- [GitHub Issues](https://github.com/MohitPimoli/drool-rule-checker/issues)
- Look for similar problems and solutions

#### 3. Report New Issues
If you can't find a solution:
1. Go to [GitHub Issues](https://github.com/MohitPimoli/drool-rule-checker/issues)
2. Click "New Issue"
3. Provide:
   - IntelliJ IDEA version
   - Plugin version
   - Operating system
   - Steps to reproduce
   - Screenshots if applicable

#### 4. Contact Support
- **Email**: mohitpimoli31@gmail.com
- **GitHub Discussions**: [Project Discussions](https://github.com/MohitPimoli/drool-rule-checker/discussions)

## 🔓 Uninstallation

### Remove Plugin
1. Go to `Settings → Plugins → Installed`
2. Find "Drools Rule Checker"
3. Click gear icon → "Uninstall"
4. Restart IntelliJ IDEA

### Clean Removal
The plugin doesn't create additional files outside IntelliJ, so uninstalling removes everything. Your `.drl` files will remain unchanged but will lose syntax highlighting.

---

## 📋 Installation Checklist

- [ ] IntelliJ IDEA 2024.2+ installed
- [ ] Java 17+ available
- [ ] Plugin installed from marketplace or manually
- [ ] IDE restarted after installation
- [ ] `.drl` file opens with syntax highlighting
- [ ] Error detection works (try typing invalid syntax)
- [ ] Auto-completion works (try typing "ru" and press Ctrl+Space)

**🎉 You're ready to develop Drools rules with enhanced IDE support!**