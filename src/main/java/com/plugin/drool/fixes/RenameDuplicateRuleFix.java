package com.plugin.drool.fixes;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.DroolsPsiFile;
import com.plugin.drool.psi.DroolsRuleBlock;
import com.plugin.drool.psi.DroolsRuleName;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that renames a duplicate rule by replacing the rule name text with a suggested name
 * (typically the original name with a numeric suffix like _2, _3, etc.).
 */
public class RenameDuplicateRuleFix implements IntentionAction, LocalQuickFix {

  private final String suggestedName;

  public RenameDuplicateRuleFix(@NotNull String suggestedName) {
    this.suggestedName = suggestedName;
  }

  @NotNull
  @Override
  public String getText() {
    return "Rename rule to '" + suggestedName + "'";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "Drools rule fixes";
  }

  @NotNull
  @Override
  public String getName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return file instanceof DroolsPsiFile;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) {
    if (editor == null || !(file instanceof DroolsPsiFile)) {
      return;
    }

    int offset = editor.getCaretModel().getOffset();
    PsiElement elementAtCaret = file.findElementAt(offset);
    if (elementAtCaret == null) {
      return;
    }

    DroolsRuleName ruleName =
        PsiTreeUtil.getParentOfType(elementAtCaret, DroolsRuleName.class, false);
    if (ruleName == null) {
      // Try to find the rule block and get its name
      DroolsRuleBlock ruleBlock =
          PsiTreeUtil.getParentOfType(elementAtCaret, DroolsRuleBlock.class, false);
      if (ruleBlock != null) {
        ruleName = ruleBlock.getRuleName();
      }
    }

    if (ruleName != null) {
      replaceRuleName(project, file, ruleName);
    }
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    if (element == null) {
      return;
    }

    // The descriptor element might be the rule name itself or a child of it
    DroolsRuleName ruleName;
    if (element instanceof DroolsRuleName droolsRuleName) {
      ruleName = droolsRuleName;
    } else {
      ruleName = PsiTreeUtil.getParentOfType(element, DroolsRuleName.class, false);
      if (ruleName == null) {
        // Try to find via rule block
        DroolsRuleBlock ruleBlock =
            PsiTreeUtil.getParentOfType(element, DroolsRuleBlock.class, false);
        if (ruleBlock != null) {
          ruleName = ruleBlock.getRuleName();
        }
      }
    }

    if (ruleName == null) {
      return;
    }

    PsiFile file = element.getContainingFile();
    replaceRuleName(project, file, ruleName);
  }

  private void replaceRuleName(
      @NotNull Project project, @NotNull PsiFile file, @NotNull DroolsRuleName ruleName) {
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    int startOffset = ruleName.getTextRange().getStartOffset();
    int endOffset = ruleName.getTextRange().getEndOffset();

    // Determine the replacement text — preserve quoting style if the original was quoted
    String originalText = ruleName.getText();
    String replacement;
    if (originalText != null && originalText.startsWith("\"") && originalText.endsWith("\"")) {
      replacement = "\"" + suggestedName + "\"";
    } else {
      replacement = suggestedName;
    }

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.replaceString(startOffset, endOffset, replacement);
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
