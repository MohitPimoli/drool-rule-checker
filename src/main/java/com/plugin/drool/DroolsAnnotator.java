package com.plugin.drool;

import static com.plugin.drool.util.Pattern.END_KEYWORD_REGEX;
import static com.plugin.drool.util.Pattern.RULE_ATTRIBUTES_REGEX;
import static com.plugin.drool.util.Pattern.RULE_KEYWORD_REGEX;
import static com.plugin.drool.util.Pattern.RULE_NAME_REGEX;
import static com.plugin.drool.util.Pattern.THEN_KEYWORD_REGEX;
import static com.plugin.drool.util.Pattern.TYPO_REGEX;
import static com.plugin.drool.util.Pattern.WHEN_KEYWORD_REGEX;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class DroolsAnnotator implements Annotator {
  private static final Pattern RULE_NAME_PATTERN = Pattern.compile(RULE_NAME_REGEX);

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    String text = element.getText();

    // Validate complete rule structure
    validateCompleteRuleStructure(element, holder, text);
    validateQuotes(element, holder, text);
    validateKeywordUsage(element, holder, text);
    validateRuleAttributes(element, holder, text);
  }

  private void validateCompleteRuleStructure(
      PsiElement element, AnnotationHolder holder, String text) {

    // First check if this element contains a rule declaration
    if (!text.matches(RULE_KEYWORD_REGEX)) {
      return; // Not a rule, skip validation
    }

    // Debug: Let's be more specific about what we're checking
    boolean hasRuleKeyword = text.matches(RULE_KEYWORD_REGEX);
    boolean hasWhenKeyword = text.matches(WHEN_KEYWORD_REGEX);
    boolean hasThenKeyword = text.matches(THEN_KEYWORD_REGEX);
    boolean hasEndKeyword = text.matches(END_KEYWORD_REGEX);

    // Only validate if we actually have a rule declaration
    if (hasRuleKeyword) {

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
}
