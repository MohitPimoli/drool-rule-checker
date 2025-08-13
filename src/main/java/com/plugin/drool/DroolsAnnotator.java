package com.plugin.drool;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.plugin.drool.util.Pattern.END_BLOCK_REGEX;
import static com.plugin.drool.util.Pattern.RULE_BLOCK_REGEX;
import static com.plugin.drool.util.Pattern.RULE_NAME_REGEX;
import static com.plugin.drool.util.Pattern.TYPO_REGEX;
import static com.plugin.drool.util.Pattern.WHEN_THEN_BLOCK_REGEX;

public class DroolsAnnotator implements Annotator {

  // Patterns for validation
  private static final Pattern RULE_PATTERN = Pattern.compile(RULE_BLOCK_REGEX);
  private static final Pattern WHEN_THEN_PATTERN =
      Pattern.compile(WHEN_THEN_BLOCK_REGEX, Pattern.DOTALL);
  private static final Pattern END_PATTERN = Pattern.compile(END_BLOCK_REGEX);

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    String text = element.getText();

    // Check for common Drools syntax errors
    validateRuleStructure(element, holder, text);
    validateQuotes(element, holder, text);
    validateKeywordUsage(element, holder, text);
  }

  private void validateRuleStructure(PsiElement element, AnnotationHolder holder, String text) {
    // Check if 'rule' is followed by 'when' and 'then'
    if (RULE_PATTERN.matcher(text).find() && !WHEN_THEN_PATTERN.matcher(text).find()) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Rule must contain 'when' clause")
          .range(element.getTextRange())
          .create();
    }

    if (WHEN_THEN_PATTERN.matcher(text).find() && !WHEN_THEN_PATTERN.matcher(text).find()) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Missing 'then' clause after 'when'")
          .range(element.getTextRange())
          .create();
    }

    // Check for missing 'end' keyword
    if (RULE_PATTERN.matcher(text).find() && !END_PATTERN.matcher(text).find()) {
      holder
          .newAnnotation(HighlightSeverity.WARNING, "Rule should end with 'end' keyword")
          .range(element.getTextRange())
          .create();
    }
  }

  private void validateQuotes(PsiElement element, AnnotationHolder holder, String text) {
    // Check for unmatched quotes in rule names
    Pattern ruleNamePattern = Pattern.compile(RULE_NAME_REGEX);
    Matcher matcher = ruleNamePattern.matcher(text);

    if (matcher.find() && !text.substring(matcher.start()).contains("\"")) {
      int start = element.getTextOffset() + matcher.start();
      int end = element.getTextOffset() + matcher.end();

      holder
          .newAnnotation(HighlightSeverity.ERROR, "Unclosed quote in rule name")
          .range(TextRange.create(start, end))
          .create();
    }
  }

  private void validateKeywordUsage(PsiElement element, AnnotationHolder holder, String text) {
    // Highlight deprecated or incorrect usage
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
