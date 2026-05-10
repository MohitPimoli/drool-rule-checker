package com.plugin.drool;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.psi.*;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.drool.util.DroolsConstants.UNNAMED;

/**
 * PSI-based folding builder for Drools DRL files. Traverses the PSI tree to find foldable
 * constructs instead of using regex matching.
 */
public class DroolsFoldingBuilder extends FoldingBuilderEx {

  @NotNull
  @Override
  public FoldingDescriptor @NotNull [] buildFoldRegions(
      @NotNull PsiElement root, @NotNull Document document, boolean quick) {
    List<FoldingDescriptor> descriptors = new ArrayList<>();

    // Fold rule blocks
    for (DroolsRuleBlock ruleBlock : PsiTreeUtil.findChildrenOfType(root, DroolsRuleBlock.class)) {
      addRuleBlockFolding(descriptors, ruleBlock);
    }

    // Fold consecutive import statement groups
    addImportGroupFolding(descriptors, root);

    // Fold declare blocks
    for (DroolsDeclareBlock declareBlock :
        PsiTreeUtil.findChildrenOfType(root, DroolsDeclareBlock.class)) {
      addDeclareBlockFolding(descriptors, declareBlock);
    }

    // Fold query definitions
    for (DroolsQueryDef queryDef : PsiTreeUtil.findChildrenOfType(root, DroolsQueryDef.class)) {
      addQueryDefFolding(descriptors, queryDef);
    }

    // Fold function definitions
    for (DroolsFunctionDef functionDef :
        PsiTreeUtil.findChildrenOfType(root, DroolsFunctionDef.class)) {
      addFunctionDefFolding(descriptors, functionDef);
    }

    // Fold multi-line comments
    for (PsiComment comment : PsiTreeUtil.findChildrenOfType(root, PsiComment.class)) {
      addCommentFolding(descriptors, comment);
    }

    return descriptors.toArray(new FoldingDescriptor[0]);
  }

  private void addRuleBlockFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull DroolsRuleBlock ruleBlock) {
    TextRange range = ruleBlock.getTextRange();
    if (range.getLength() < 2) return;

    String ruleName = extractRuleName(ruleBlock);
    descriptors.add(
        new FoldingDescriptor(ruleBlock.getNode(), range, null) {
          @Override
          public String getPlaceholderText() {
            return "rule " + ruleName + " ...";
          }
        });
  }

  private void addImportGroupFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull PsiElement root) {
    List<DroolsImportStatement> imports =
        new ArrayList<>(PsiTreeUtil.findChildrenOfType(root, DroolsImportStatement.class));

    if (imports.size() < 2) return;

    // Find consecutive groups of import statements
    int groupStart = 0;
    while (groupStart < imports.size()) {
      int groupEnd = groupStart;

      // Extend the group while imports are consecutive (only whitespace/comments between them)
      while (groupEnd + 1 < imports.size()
          && areConsecutiveImports(imports.get(groupEnd), imports.get(groupEnd + 1))) {
        groupEnd++;
      }

      // Only fold if there are at least 2 consecutive imports
      if (groupEnd > groupStart) {
        DroolsImportStatement first = imports.get(groupStart);
        DroolsImportStatement last = imports.get(groupEnd);
        TextRange range =
            new TextRange(
                first.getTextRange().getStartOffset(), last.getTextRange().getEndOffset());

        int importCount = groupEnd - groupStart + 1;
        descriptors.add(
            new FoldingDescriptor(first.getNode(), range, null) {
              @Override
              public String getPlaceholderText() {
                return "import ... (" + importCount + " imports)";
              }
            });
      }

      groupStart = groupEnd + 1;
    }
  }

  private boolean areConsecutiveImports(
      @NotNull DroolsImportStatement first, @NotNull DroolsImportStatement second) {
    // Check that there are no non-whitespace, non-comment elements between the two imports
    PsiElement current = first.getNextSibling();
    while (current != null && current != second) {
      if (!(current instanceof com.intellij.psi.PsiWhiteSpace)
          && !(current instanceof PsiComment)) {
        return false;
      }
      current = current.getNextSibling();
    }
    return current == second;
  }

  private void addDeclareBlockFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull DroolsDeclareBlock declareBlock) {
    TextRange range = declareBlock.getTextRange();
    if (range.getLength() < 2) return;

    String name = extractDeclareName(declareBlock);
    descriptors.add(
        new FoldingDescriptor(declareBlock.getNode(), range, null) {
          @Override
          public String getPlaceholderText() {
            return "declare " + name + " ...";
          }
        });
  }

  private void addQueryDefFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull DroolsQueryDef queryDef) {
    TextRange range = queryDef.getTextRange();
    if (range.getLength() < 2) return;

    String name = extractQueryName(queryDef);
    descriptors.add(
        new FoldingDescriptor(queryDef.getNode(), range, null) {
          @Override
          public String getPlaceholderText() {
            return "query " + name + " ...";
          }
        });
  }

  private void addFunctionDefFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull DroolsFunctionDef functionDef) {
    TextRange range = functionDef.getTextRange();
    if (range.getLength() < 2) return;

    String name = extractFunctionName(functionDef);
    descriptors.add(
        new FoldingDescriptor(functionDef.getNode(), range, null) {
          @Override
          public String getPlaceholderText() {
            return "function " + name + " ...";
          }
        });
  }

  private void addCommentFolding(
      @NotNull List<FoldingDescriptor> descriptors, @NotNull PsiComment comment) {
    String text = comment.getText();
    // Only fold multi-line block comments (/* ... */)
    if (text != null && text.startsWith("/*") && text.contains("\n")) {
      TextRange range = comment.getTextRange();
      descriptors.add(
          new FoldingDescriptor(comment.getNode(), range, null) {
            @Override
            public String getPlaceholderText() {
              return "/* ... */";
            }
          });
    }
  }

  @NotNull
  private String extractRuleName(@NotNull DroolsRuleBlock ruleBlock) {
    DroolsRuleName ruleName = ruleBlock.getRuleName();
    if (ruleName == null) return UNNAMED;
    String text = ruleName.getText();
    if (text == null) return UNNAMED;
    // Strip surrounding quotes if present
    if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
      return text.substring(1, text.length() - 1);
    }
    return text;
  }

  @NotNull
  private String extractDeclareName(@NotNull DroolsDeclareBlock declareBlock) {
    PsiElement identifier = declareBlock.getIdentifier();
    if (identifier != null) {
      return identifier.getText();
    }
    return UNNAMED;
  }

  @NotNull
  private String extractQueryName(@NotNull DroolsQueryDef queryDef) {
    DroolsQueryName queryName = queryDef.getQueryName();
    if (queryName == null) return UNNAMED;
    // Try identifier first, then string
    PsiElement identifier = queryName.getIdentifier();
    if (identifier != null) return identifier.getText();
    PsiElement string = queryName.getString();
    if (string != null) {
      String text = string.getText();
      // Strip surrounding quotes if present
      if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
        return text.substring(1, text.length() - 1);
      }
      return text;
    }
    return UNNAMED;
  }

  @NotNull
  private String extractFunctionName(@NotNull DroolsFunctionDef functionDef) {
    PsiElement identifier = functionDef.getIdentifier();
    if (identifier != null) {
      return identifier.getText();
    }
    return UNNAMED;
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return "...";
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return false;
  }
}
