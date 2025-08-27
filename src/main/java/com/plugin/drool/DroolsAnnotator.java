package com.plugin.drool;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

import static com.plugin.drool.util.DroolsConstants.IMPORT;
import static com.plugin.drool.util.DroolsConstants.RULE_SPACE;
import static com.plugin.drool.util.DroolsConstants.isJavaLangClass;
import static com.plugin.drool.util.DroolsConstants.shouldSkipImportValidation;
import static com.plugin.drool.util.Pattern.FIELD_ACCESS_REGEX;
import static com.plugin.drool.util.Pattern.FUNCTION_CALL_REGEX;
import static com.plugin.drool.util.Pattern.IMPORT_STATEMENT_REGEX;
import static com.plugin.drool.util.Pattern.INCOMPLETE_STATEMENT;
import static com.plugin.drool.util.Pattern.INVALID_ESCAPE_REGEX;
import static com.plugin.drool.util.Pattern.JAVA_CODE_BLOCK;
import static com.plugin.drool.util.Pattern.MISSING_SEMICOLON;
import static com.plugin.drool.util.Pattern.NEW_CLASS_NAME_REGEX;
import static com.plugin.drool.util.Pattern.RULE_ATTRIBUTES_REGEX;
import static com.plugin.drool.util.Pattern.RULE_NAME_REGEX;
import static com.plugin.drool.util.Pattern.STATIC_CALL_REGEX;
import static com.plugin.drool.util.Pattern.TYPE_REGEX;
import static com.plugin.drool.util.Pattern.TYPO_REGEX;
import static com.plugin.drool.util.Pattern.UNCLOSED_STRING_REGEX;

public class DroolsAnnotator implements Annotator {
  private static final Pattern RULE_NAME_PATTERN = Pattern.compile(RULE_NAME_REGEX);

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    String text = element.getText();

    // Only validate if we have substantial content (avoid validating single characters/tokens)
    if (text.trim().length() < 3) {
      return;
    }

    // Apply validations to file-level elements, substantial text blocks, or any text containing
    // "rule"
    boolean isFileLevel = element.getParent() == null || element.getParent().getParent() == null;
    boolean isSubstantialText = text.length() > 50;
    boolean containsRule = text.contains(RULE_SPACE);

    if (isFileLevel || isSubstantialText || containsRule) {
      // Core validations - always apply to substantial content or rule declarations
      validateCompleteRuleStructure(element, holder, text);
      validateKeywordUsage(element, holder, text);

      // String and bracket validations - only for larger blocks to avoid false positives
      if (text.length() > 20) {
        validateQuotes(element, holder, text);
        validateBracketMatching(element, holder, text);
      }

      // Advanced validations - for any text containing rule declarations
      if (text.contains("rule")) {
        validateRuleAttributes(element, holder, text);

        // Only validate Java code blocks if we have both 'then' and 'end'
        if (text.contains("then") && text.contains("end")) {
          validateJavaCodeBlocks(element, holder, text);
        }

        validateFieldAccess(element, holder, text);
        validateStringLiterals(element, holder, text);
        validateFunctionCalls(element, holder, text);
      }

      // Import validation - only for files with explicit import statements
      if (text.contains(IMPORT) && text.length() > 100) {
        validateImports(element, holder, text);
      }
    }
  }

  private void validateCompleteRuleStructure(
      PsiElement element, AnnotationHolder holder, String text) {

    // Check if this element contains a rule declaration (more flexible check)
    if (!text.contains(RULE_SPACE)) {
      return; // Not a rule, skip validation
    }

    // Use simple contains() checks instead of regex for better reliability
    boolean hasRuleKeyword = text.contains(RULE_SPACE);
    boolean hasWhenKeyword = text.contains("when");
    boolean hasThenKeyword = text.contains("then");
    boolean hasEndKeyword = text.contains("end");

    // Only validate if we actually have a rule declaration
    // For substantial text blocks, validate rule structure
    if (hasRuleKeyword && text.length() > 30) {
      // Check for missing components in order of importance
      if (!hasWhenKeyword) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Rule must contain 'when' clause")
            .range(element.getTextRange())
            .create();
        return; // Don't check further if 'when' is missing
      }

      if (!hasThenKeyword) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Rule must contain 'then' clause after 'when'")
            .range(element.getTextRange())
            .create();
        return; // Don't check further if 'then' is missing
      }

      if (!hasEndKeyword) {
        holder
            .newAnnotation(HighlightSeverity.WARNING, "Rule should end with 'end' keyword")
            .range(element.getTextRange())
            .create();
      }

      // Additional structural validation
      validateRuleOrder(element, holder, text);
    }
  }

  private void validateRuleOrder(PsiElement element, AnnotationHolder holder, String text) {
    // Check if 'when' comes before 'then'
    int whenIndex = text.indexOf("when");
    int thenIndex = text.indexOf("then");
    int endIndex = text.lastIndexOf("end");

    if (whenIndex != -1 && thenIndex != -1 && whenIndex > thenIndex) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "'when' clause must come before 'then' clause")
          .range(element.getTextRange())
          .create();
    }

    if (thenIndex != -1 && endIndex != -1 && thenIndex > endIndex) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "'then' clause must come before 'end' keyword")
          .range(element.getTextRange())
          .create();
    }
  }

  private void validateQuotes(PsiElement element, AnnotationHolder holder, String text) {
    Matcher matcher = RULE_NAME_PATTERN.matcher(text);

    while (matcher.find()) {
      // Check for unclosed quotes in rule names
      String ruleName = matcher.group(1); // Quoted name
      if (ruleName != null) {
        // Rule name is quoted, check if properly closed
        String fullMatch = matcher.group(0);

        // Check if it starts with rule and quote, and find the closing quote
        if (fullMatch.matches("rule\\s+\".*")) {
          int firstQuoteIndex = fullMatch.indexOf("\"");
          int secondQuoteIndex = fullMatch.indexOf("\"", firstQuoteIndex + 1);

          // If there's no second quote, it's unclosed
          if (secondQuoteIndex == -1) {
            int start = element.getTextOffset() + matcher.start();
            int end = element.getTextOffset() + matcher.end();

            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unclosed quote in rule name")
                .range(TextRange.create(start, end))
                .create();
          }
        }
      }
    }
  }

  private void validateRuleAttributes(PsiElement element, AnnotationHolder holder, String text) {
    // Validate rule attributes like salience, no-loop, etc.
    Pattern attributePattern = Pattern.compile(RULE_ATTRIBUTES_REGEX);
    Matcher matcher = attributePattern.matcher(text);

    while (matcher.find()) {
      String attribute = matcher.group(0);

      // Validate salience values are numeric
      if (attribute.trim().startsWith("salience") && !attribute.matches("salience\\s+\\d+.*")) {
        int start = element.getTextOffset() + matcher.start();
        int end = element.getTextOffset() + matcher.end();

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Salience value must be numeric")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  private void validateKeywordUsage(PsiElement element, AnnotationHolder holder, String text) {
    // Keep existing validation logic
    if (text.contains("eval(") && text.contains("==")) {
      holder
          .newAnnotation(
              HighlightSeverity.WEAK_WARNING,
              "Consider using direct field constraints instead of eval() for better performance")
          .range(element.getTextRange())
          .create();
    }

    // Check for common typos
    Pattern typoPattern = Pattern.compile(TYPO_REGEX);
    Matcher typoMatcher = typoPattern.matcher(text);

    while (typoMatcher.find()) {
      int start = element.getTextOffset() + typoMatcher.start();
      int end = element.getTextOffset() + typoMatcher.end();

      String suggestion = getSuggestion(typoMatcher.group(1));
      holder
          .newAnnotation(
              HighlightSeverity.ERROR,
              "Unknown keyword '" + typoMatcher.group(1) + "'. Did you mean '" + suggestion + "'?")
          .range(TextRange.create(start, end))
          .create();
    }
  }

  private String getSuggestion(String typo) {
    return switch (typo) {
      case "wen" -> "when";
      case "thn" -> "then";
      case "edn" -> "end";
      case "ruel" -> "rule";
      default -> "unknown";
    };
  }

  /** Validates bracket matching (parentheses, braces, brackets) - more conservative */
  private void validateBracketMatching(PsiElement element, AnnotationHolder holder, String text) {
    // Skip validation for very short text or if it's just a single bracket
    if (text.length() < 10 || text.trim().length() <= 2) {
      return;
    }

    // Skip if text is inside strings or comments
    if (text.trim().startsWith("\"")
        || text.trim().startsWith("//")
        || text.trim().startsWith("/*")) {
      return;
    }

    Deque<Character> stack = new ArrayDeque<>();
    Deque<Integer> positions = new ArrayDeque<>();
    boolean inString = false;
    boolean inComment = false;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);

      // Handle string literals
      if (c == '"' && (i == 0 || text.charAt(i - 1) != '\\')) {
        inString = !inString;
        continue;
      }

      // Handle comments
      if (!inString && i < text.length() - 1) {
        if (text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
          inComment = true;
          continue;
        }
        if (text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
          inComment = true;
          continue;
        }
      }
      if (inComment
          && i < text.length() - 1
          && text.charAt(i) == '*'
          && text.charAt(i + 1) == '/') {
        inComment = false;
        i++; // skip the '/'
        continue;
      }
      if (inComment && c == '\n') {
        inComment = false; // end of line comment
        continue;
      }

      // Skip bracket validation inside strings or comments
      if (inString || inComment) {
        continue;
      }

      validateText(element, holder, c, stack, positions, i);
    }

    // Only report unmatched opening brackets if we have substantial unmatched content
    while (!stack.isEmpty()) {
      char unclosed = stack.pop();
      int pos = positions.pop();

      // Only report if there's substantial content after the opening bracket
      if (pos < text.length() - 15) {
        String message =
            switch (unclosed) {
              case '(' -> "Unmatched opening parenthesis '('";
              case '{' -> "Unmatched opening brace '{'";
              case '[' -> "Unmatched opening bracket '['";
              default -> "Unmatched opening bracket";
            };
        createBracketError(element, holder, pos, message);
      }
    }
  }

  private void validateText(
      PsiElement element,
      AnnotationHolder holder,
      char c,
      Deque<Character> stack,
      Deque<Integer> positions,
      int i) {

    switch (c) {
      case '(', '{', '[':
        stack.push(c);
        positions.push(i);
        break;

      case ')':
        if (stack.isEmpty() || stack.peek() != '(') {
          createBracketError(element, holder, i, "Unmatched closing parenthesis ')'");
        } else {
          stack.pop();
          positions.pop();
        }
        break;

      case '}':
        if (stack.isEmpty() || stack.peek() != '{') {
          createBracketError(element, holder, i, "Unmatched closing brace '}'");
        } else {
          stack.pop();
          positions.pop();
        }
        break;

      case ']':
        if (stack.isEmpty() || stack.peek() != '[') {
          createBracketError(element, holder, i, "Unmatched closing bracket ']'");
        } else {
          stack.pop();
          positions.pop();
        }
        break;
      default:
    }
  }

  private void createBracketError(
      PsiElement element, AnnotationHolder holder, int position, String message) {
    int start = element.getTextOffset() + position;
    int end = start + 1;
    holder
        .newAnnotation(HighlightSeverity.ERROR, message)
        .range(TextRange.create(start, end))
        .create();
  }

  /** Validates Java code blocks in 'then' clauses - more conservative */
  private void validateJavaCodeBlocks(PsiElement element, AnnotationHolder holder, String text) {
    // Only validate if we have a complete rule with 'then' and 'end'
    if (!text.contains("then") || !text.contains("end")) {
      return;
    }

    Pattern javaBlockPattern = Pattern.compile(JAVA_CODE_BLOCK);
    Matcher matcher = javaBlockPattern.matcher(text);

    while (matcher.find()) {
      String javaCode = matcher.group(1);
      // Only validate substantial Java code blocks
      if (javaCode.trim().length() > 10) {
        validateJavaStatements(element, holder, javaCode, matcher.start(1));
      }
    }
  }

  private void validateJavaStatements(
      PsiElement element, AnnotationHolder holder, String javaCode, int offset) {

    // Skip validation for very short code blocks
    if (javaCode.trim().length() < 15) {
      return;
    }

    // Check for missing semicolons - but be more conservative
    Pattern missingSemicolonPattern = Pattern.compile(MISSING_SEMICOLON);
    Matcher matcher = missingSemicolonPattern.matcher(javaCode);

    while (matcher.find()) {
      String matchedText = matcher.group(0);
      // Only flag if it really looks like a statement that needs a semicolon
      if (matchedText.contains("(")
          && matchedText.contains(")")
          && !matchedText.trim().endsWith(";")) {
        int start = element.getTextOffset() + offset + matcher.start();
        int end = element.getTextOffset() + offset + matcher.end();

        holder
            .newAnnotation(HighlightSeverity.WARNING, "Consider adding semicolon ';'")
            .range(TextRange.create(start, end))
            .create();
      }
    }

    // Check for incomplete statements - more conservative
    Pattern incompletePattern = Pattern.compile(INCOMPLETE_STATEMENT);
    matcher = incompletePattern.matcher(javaCode);

    while (matcher.find()) {
      String matchedText = matcher.group(0);
      // Only flag obvious incomplete statements
      if (matchedText.length() > 10) {
        int start = element.getTextOffset() + offset + matcher.start();
        int end = element.getTextOffset() + offset + matcher.end();

        holder
            .newAnnotation(
                HighlightSeverity.WARNING,
                "Incomplete statement - consider adding braces or semicolon")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  /** Validates field access patterns ($variable.field) */
  private void validateFieldAccess(PsiElement element, AnnotationHolder holder, String text) {
    Pattern fieldAccessPattern = Pattern.compile(FIELD_ACCESS_REGEX);
    Matcher matcher = fieldAccessPattern.matcher(text);

    while (matcher.find()) {
      String fieldAccess = matcher.group(0);

      // Check for multiple dots (invalid field access)
      if (fieldAccess.split("\\.").length > 2) {
        int start = element.getTextOffset() + matcher.start();
        int end = element.getTextOffset() + matcher.end();

        holder
            .newAnnotation(
                HighlightSeverity.WARNING, "Complex field access - consider using getter methods")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  /** Enhanced string literal validation - more conservative */
  private void validateStringLiterals(PsiElement element, AnnotationHolder holder, String text) {
    // Skip validation for short text or if it's just a quote
    if (text.length() < 5 || text.trim().equals("\"")) {
      return;
    }

    // Only validate complete lines or substantial text blocks
    if (!text.contains("\n") && text.length() < 20) {
      return;
    }

    // Check for unclosed strings - only in substantial text blocks
    Pattern unclosedStringPattern = Pattern.compile(UNCLOSED_STRING_REGEX);
    Matcher matcher = unclosedStringPattern.matcher(text);

    while (matcher.find()) {
      int start = element.getTextOffset() + matcher.start();
      int end = element.getTextOffset() + matcher.end();

      holder
          .newAnnotation(HighlightSeverity.ERROR, "Unclosed string literal")
          .range(TextRange.create(start, end))
          .create();
    }

    // Check for invalid escape sequences - only in complete strings
    if (text.contains("\"") && text.lastIndexOf("\"") > text.indexOf("\"")) {
      Pattern invalidEscapePattern = Pattern.compile(INVALID_ESCAPE_REGEX);
      matcher = invalidEscapePattern.matcher(text);

      while (matcher.find()) {
        int start = element.getTextOffset() + matcher.start();
        int end = element.getTextOffset() + matcher.end();

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Invalid escape sequence")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  /** Validates function call syntax - more conservative */
  private void validateFunctionCalls(PsiElement element, AnnotationHolder holder, String text) {
    // Skip validation for short text
    if (text.length() < 10) {
      return;
    }

    Pattern functionCallPattern = Pattern.compile(FUNCTION_CALL_REGEX);
    Matcher matcher = functionCallPattern.matcher(text);

    while (matcher.find()) {
      String functionCall = matcher.group(0);

      // Only validate complete function calls (not partial ones being typed)
      // and Check if this looks like a complete function call that's missing closing paren
      if (functionCall.length() > 5
          && !functionCall.trim().endsWith(")")
          && functionCall.contains("(")
          && !functionCall.endsWith("(")) {

        int start = element.getTextOffset() + matcher.start();
        int end = element.getTextOffset() + matcher.end();

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Missing closing parenthesis in function call")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  /** Validates import statements and checks for missing imports for used classes */
  private void validateImports(PsiElement element, AnnotationHolder holder, String text) {
    // Skip validation for short text blocks or if no substantial content
    if (text.length() < 50) {
      return;
    }

    // Only validate if this looks like a complete file or substantial block
    boolean hasPackageOrImport = text.contains("package ") || text.contains(IMPORT);
    boolean hasRule = text.contains("rule ");

    // Only validate imports if we have a substantial Drools file
    if (!hasPackageOrImport && !hasRule) {
      return;
    }

    // Validate import syntax
    validateImportSyntax(element, holder, text);

    // Enable missing import detection with improved logic
    Set<String> importedClasses = extractImportedClasses(text);
    Set<String> usedClasses = extractUsedClasses(text);
    checkMissingImports(element, holder, text, importedClasses, usedClasses);

    // Validate that imported classes can be resolved
    validateImportResolution(element, holder, text);
  }

  /** Extracts all imported classes from import statements */
  private Set<String> extractImportedClasses(String text) {
    Set<String> importedClasses = new HashSet<>();

    Pattern importPattern = Pattern.compile(IMPORT_STATEMENT_REGEX);
    Matcher matcher = importPattern.matcher(text);

    while (matcher.find()) {
      String importPath = matcher.group(1);

      if (!importPath.endsWith(".*")) {
        // Extract class name from full path (e.g., java.util.List -> List)
        String className = importPath.substring(importPath.lastIndexOf('.') + 1);
        importedClasses.add(className);
      }
      // Wildcard import - we'll be lenient and assume all classes are available
      // In a real implementation, you'd want to resolve the package contents
    }
    return importedClasses;
  }

  /** Extracts all used class names from the file content (more conservative) */
  private Set<String> extractUsedClasses(String text) {
    Set<String> usedClasses = new HashSet<>();

    // 1. Look for "new ClassName(" patterns - most reliable
    Pattern newClassPattern = Pattern.compile(NEW_CLASS_NAME_REGEX);
    Matcher matcher = newClassPattern.matcher(text);

    while (matcher.find()) {
      String className = matcher.group(1);
      if (!shouldSkipImportValidation(className)) {
        usedClasses.add(className);
      }
    }

    // 2. Look for static method calls - ClassName.methodName()
    Pattern staticCallPattern = Pattern.compile(STATIC_CALL_REGEX);
    matcher = staticCallPattern.matcher(text);

    while (matcher.find()) {
      String className = matcher.group(1);
      if (!shouldSkipImportValidation(className) && !text.contains("$" + className)) {
        usedClasses.add(className);
      }
    }

    // 3. Look for type declarations in Drools - ClassName(field == value)
    Pattern typeConstraintPattern = Pattern.compile(TYPE_REGEX);
    matcher = typeConstraintPattern.matcher(text);

    while (matcher.find()) {
      String className = matcher.group(1);
      // Only add if it's in a 'when' clause and not a method call
      int position = matcher.start();
      String beforeClass = text.substring(Math.max(0, position - 20), position);

      // Check if it's likely a Drools type constraint (not a method call)
      if (!beforeClass.contains("new ")
          && !beforeClass.contains(".")
          && !shouldSkipImportValidation(className)) {
        usedClasses.add(className);
      }
    }

    return usedClasses;
  }

  /** Checks for missing imports and creates error annotations */
  private void checkMissingImports(
      PsiElement element,
      AnnotationHolder holder,
      String text,
      Set<String> importedClasses,
      Set<String> usedClasses) {

    for (String usedClass : usedClasses) {
      if (!importedClasses.contains(usedClass) && !isJavaLangClass(usedClass)) {
        // Find all occurrences of this class in the text
        Pattern classOccurrencePattern = Pattern.compile("\\b" + Pattern.quote(usedClass) + "\\b");
        Matcher matcher = classOccurrencePattern.matcher(text);

        while (matcher.find()) {
          // Skip if it's in an import statement or package declaration
          if (isInImportOrPackage(text, matcher.start())) {
            continue;
          }

          int start = element.getTextOffset() + matcher.start();
          int end = element.getTextOffset() + matcher.end();

          holder
              .newAnnotation(
                  HighlightSeverity.ERROR,
                  "Cannot resolve symbol '" + usedClass + "'. Add import statement.")
              .range(TextRange.create(start, end))
              .create();
        }
      }
    }
  }

  /** Validates import statement syntax (more conservative) */
  private void validateImportSyntax(PsiElement element, AnnotationHolder holder, String text) {
    // Only validate explicit import lines to avoid false positives
    String[] lines = text.split("\n");

    for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
      String line = lines[lineIndex].trim();

      // Only validate lines that clearly start with "import"
      if (line.startsWith(IMPORT)) {
        // Check for missing semicolon at end of import line
        if (!line.endsWith(";")) {
          // Find the position in the original text
          int lineStart = 0;
          for (int i = 0; i < lineIndex; i++) {
            lineStart += lines[i].length() + 1; // +1 for newline
          }

          int start = element.getTextOffset() + lineStart + line.length();
          int end = start + 1;

          holder
              .newAnnotation(HighlightSeverity.WARNING, "Missing semicolon in import statement")
              .range(TextRange.create(start, end))
              .create();
        }

        // Validate import path format (basic validation)
        String importPath = line.substring(6).trim(); // Remove IMPORT
        if (importPath.endsWith(";")) {
          importPath = importPath.substring(0, importPath.length() - 1);
        }

        // Very basic validation - just check it's not empty and has valid characters
        if (importPath.isEmpty() || !importPath.matches("[a-zA-Z_][a-zA-Z0-9_.*]*")) {
          int lineStart = 0;
          for (int i = 0; i < lineIndex; i++) {
            lineStart += lines[i].length() + 1;
          }

          int start = element.getTextOffset() + lineStart;
          int end = element.getTextOffset() + lineStart + line.length();

          holder
              .newAnnotation(HighlightSeverity.ERROR, "Invalid import statement format")
              .range(TextRange.create(start, end))
              .create();
        }
      }
    }
  }

  /** Checks if a position is within an import or package statement */
  private boolean isInImportOrPackage(String text, int position) {
    // Find the line containing this position
    int lineStart = text.lastIndexOf('\n', position) + 1;
    int lineEnd = text.indexOf('\n', position);
    if (lineEnd == -1) lineEnd = text.length();

    String line = text.substring(lineStart, lineEnd).trim();
    return line.startsWith(IMPORT) || line.startsWith("package ");
  }

  /** Validates that imported classes can actually be resolved (exist in classpath) */
  private void validateImportResolution(PsiElement element, AnnotationHolder holder, String text) {
    String[] lines = text.split("\n");

    for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
      String line = lines[lineIndex].trim();

      if (line.startsWith(IMPORT) && !line.contains(".*")) {
        // Extract the full class path
        String importPath = line.substring(6).trim();
        if (importPath.endsWith(";")) {
          importPath = importPath.substring(0, importPath.length() - 1);
        }

        // Try to resolve the class using IntelliJ's PSI system
        if (!canResolveClass(element, importPath)) {
          // Find the position in the original text
          int lineStart = 0;
          for (int i = 0; i < lineIndex; i++) {
            lineStart += lines[i].length() + 1; // +1 for newline
          }

          // Highlight the class path part of the import
          int importStart = line.indexOf(importPath);
          int start = element.getTextOffset() + lineStart + importStart;
          int end = start + importPath.length();

          holder
              .newAnnotation(HighlightSeverity.ERROR, "Cannot resolve class '" + importPath + "'")
              .range(TextRange.create(start, end))
              .create();
        }
      }
    }
  }

  /**
   * Attempts to resolve a class using IntelliJ's PSI system This includes ALL dependencies: Maven,
   * Gradle, local repos, JARs, etc.
   */
  private boolean canResolveClass(PsiElement element, String fullyQualifiedClassName) {
    try {
      // Use IntelliJ's JavaPsiFacade to resolve classes
      JavaPsiFacade facade = JavaPsiFacade.getInstance(element.getProject());

      // Try multiple search scopes for comprehensive dependency resolution

      // 1. All scope - includes everything (dependencies, JARs, etc.)
      PsiClass psiClass =
          facade.findClass(
              fullyQualifiedClassName, GlobalSearchScope.allScope(element.getProject()));
      if (psiClass != null) {
        return true;
      }

      // 2. Libraries and dependencies scope - for Maven/Gradle dependencies
      Module module = ModuleUtilCore.findModuleForPsiElement(element);
      if (module != null) {
        // Module with dependencies and libraries scope
        psiClass =
            facade.findClass(
                fullyQualifiedClassName,
                GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module));
        if (psiClass != null) {
          return true;
        }

        // Libraries scope only (external dependencies)
        psiClass =
            facade.findClass(
                fullyQualifiedClassName, GlobalSearchScope.moduleWithLibrariesScope(module));
        if (psiClass != null) {
          return true;
        }

        // Project scope (for classes within the project)
        psiClass =
            facade.findClass(
                fullyQualifiedClassName, GlobalSearchScope.projectScope(element.getProject()));
        return psiClass != null;
      }

      return false;
    } catch (Exception e) {
      // If we can't resolve for any reason, assume it's valid to avoid false positives
      // This handles edge cases where PSI might not be fully initialized
      return true;
    }
  }
}
