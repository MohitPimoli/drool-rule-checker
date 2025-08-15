package com.plugin.drool;

import static com.plugin.drool.util.Pattern.COMMENT_BLOCK;
import static com.plugin.drool.util.Pattern.RULE_BLOCK;
import static com.plugin.drool.util.Pattern.SINGLE_LINE_COMMENT;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DroolsFoldingBuilder extends FoldingBuilderEx {

  @NotNull
  @Override
  public FoldingDescriptor @NotNull [] buildFoldRegions(
      @NotNull PsiElement root, @NotNull Document document, boolean quick) {
    List<FoldingDescriptor> descriptors = new ArrayList<>();
    String text = document.getText();

    // Find rule blocks
    findRuleBlocks(descriptors, root, text);

    // Find comment blocks
    findCommentBlocks(descriptors, root, text);

    return descriptors.toArray(new FoldingDescriptor[0]);
  }

  private void findRuleBlocks(List<FoldingDescriptor> descriptors, PsiElement root, String text) {
    Pattern rulePattern = Pattern.compile(RULE_BLOCK, Pattern.MULTILINE);
    Matcher matcher = rulePattern.matcher(text);

    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();

      if (end - start > 20) { // Only fold if the rule is reasonably long
        TextRange range = TextRange.create(start, end);

        // Capture the rule name here, while the matcher state is valid
        String ruleName = "unknown";
        try {
          if (matcher.groupCount() >= 1 && matcher.group(1) != null) {
            ruleName = matcher.group(1);
          }
        } catch (IllegalStateException e) {
          // If group extraction fails, use default name
          ruleName = "rule";
        }

        // Create a final variable for use in lambda
        final String finalRuleName = ruleName;

        descriptors.add(
            new FoldingDescriptor(root.getNode(), range) {
              @Override
              public String getPlaceholderText() {
                return "rule \"" + finalRuleName + "\" { ... }";
              }
            });
      }
    }
  }

  private void findCommentBlocks(
      List<FoldingDescriptor> descriptors, PsiElement root, String text) {
    // Multi-line comments
    Pattern multiCommentPattern = Pattern.compile(COMMENT_BLOCK, Pattern.MULTILINE);
    Matcher matcher = multiCommentPattern.matcher(text);

    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();

      if (end - start > 30) { // Only fold longer comments
        TextRange range = TextRange.create(start, end);
        descriptors.add(
            new FoldingDescriptor(root.getNode(), range) {
              @Override
              public String getPlaceholderText() {
                return "/* ... */";
              }
            });
      }
    }

    // Consecutive single-line comments
    Pattern lineCommentPattern = Pattern.compile(SINGLE_LINE_COMMENT, Pattern.MULTILINE);
    matcher = lineCommentPattern.matcher(text);

    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();

      // Safely extract the matched group
      String matchedGroup = "";
      try {
        if (matcher.groupCount() >= 1 && matcher.group(1) != null) {
          matchedGroup = matcher.group(1);
        }
      } catch (IllegalStateException e) {
        // If group extraction fails, use the entire match
        matchedGroup = matcher.group(0);
      }

      long lineCount = matchedGroup.chars().filter(ch -> ch == '\n').count() + 1;

      if (lineCount > 3) { // Fold if more than 3 consecutive comment lines
        TextRange range = TextRange.create(start, end);

        // Capture line count for lambda
        final long finalLineCount = lineCount;

        descriptors.add(
            new FoldingDescriptor(root.getNode(), range) {
              @Override
              public String getPlaceholderText() {
                return "// ... (" + finalLineCount + " lines)";
              }
            });
      }
    }
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return "{ ... }";
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return false; // Don't collapse by default
  }
}
