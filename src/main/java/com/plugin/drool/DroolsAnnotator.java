package com.plugin.drool;

import static com.plugin.drool.util.DroolsConstants.RULE_SPACE;
import static com.plugin.drool.util.Pattern.CONSTRAINT_EXPRESSION_REGEX;
import static com.plugin.drool.util.Pattern.FIELD_ACCESS_REGEX;
import static com.plugin.drool.util.Pattern.FUNCTION_CALL_REGEX;
import static com.plugin.drool.util.Pattern.INCOMPLETE_STATEMENT;
import static com.plugin.drool.util.Pattern.INVALID_ESCAPE_REGEX;
import static com.plugin.drool.util.Pattern.JAVA_CODE_BLOCK;
import static com.plugin.drool.util.Pattern.MISSING_SEMICOLON;
import static com.plugin.drool.util.Pattern.RULE_ATTRIBUTES_REGEX;
import static com.plugin.drool.util.Pattern.RULE_NAME_REGEX;
import static com.plugin.drool.util.Pattern.TYPO_REGEX;
import static com.plugin.drool.util.Pattern.UNCLOSED_STRING_REGEX;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

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

        // Temporarily disabled to avoid false positives with Java assignments
        // validateConstraintExpressions(element, holder, text);
        validateFieldAccess(element, holder, text);
        validateStringLiterals(element, holder, text);
        validateFunctionCalls(element, holder, text);
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

  /**
   * Validates constraint expressions in when clauses - only in Drools constraints, not Java code
   */
  private void validateConstraintExpressions(
      PsiElement element, AnnotationHolder holder, String text) {

    // Only validate constraint expressions in 'when' clauses, not in 'then' clauses (Java code)
    if (!text.contains("when") || text.contains("then")) {
      return;
    }

    // Extract only the 'when' clause part
    int whenIndex = text.indexOf("when");
    int thenIndex = text.indexOf("then");
    if (whenIndex == -1) return;

    String whenClause;
    if (thenIndex != -1 && thenIndex > whenIndex) {
      whenClause = text.substring(whenIndex, thenIndex);
    } else {
      whenClause = text.substring(whenIndex);
    }

    Pattern constraintPattern = Pattern.compile(CONSTRAINT_EXPRESSION_REGEX);
    Matcher matcher = constraintPattern.matcher(whenClause);

    while (matcher.find()) {
      String constraint = matcher.group(0);

      // Check for invalid operators - only in Drools constraint context
      // Skip if it looks like a Java assignment (has semicolon or is in parentheses for method
      // calls)
      if (constraint.contains("=")
          && !constraint.contains("==")
          && !constraint.contains("!=")
          && !constraint.contains("<=")
          && !constraint.contains(">=")
          && !constraint.contains(";") // Skip Java statements
          && !isInMethodCall(constraint)) { // Skip method parameters

        int start = element.getTextOffset() + whenIndex + matcher.start();
        int end = element.getTextOffset() + whenIndex + matcher.end();

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Use '==' for comparison, not '='")
            .range(TextRange.create(start, end))
            .create();
      }
    }
  }

  private boolean isInMethodCall(String constraint) {
    // Check if this constraint is inside a method call (has parentheses around it)
    return constraint.trim().startsWith("(")
        || constraint.contains("(") && constraint.contains(")");
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
}
