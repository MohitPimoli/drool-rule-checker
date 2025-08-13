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
        descriptors.add(
            new FoldingDescriptor(root.getNode(), range) {
              @Override
              public String getPlaceholderText() {
                return "rule \"" + matcher.group(1) + "\" { ... }";
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
      long lineCount = matcher.group(1).chars().filter(ch -> ch == '\n').count() + 1;

      if (lineCount > 3) { // Fold if more than 3 consecutive comment lines
        TextRange range = TextRange.create(start, end);
        descriptors.add(
            new FoldingDescriptor(root.getNode(), range) {
              @Override
              public String getPlaceholderText() {
                return "// ... (" + lineCount + " lines)";
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
