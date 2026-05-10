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
import com.plugin.drool.psi.DroolsBindingPattern;
import com.plugin.drool.psi.DroolsBindingVariable;
import com.plugin.drool.psi.DroolsFactPattern;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that removes an unused binding variable prefix from a pattern. Transforms {@code $var :
 * Type(constraints)} into {@code Type(constraints)} by removing the binding variable and the colon
 * separator.
 */
public class RemoveBindingFix implements IntentionAction, LocalQuickFix {

  private final String variableName;

  public RemoveBindingFix(@NotNull String variableName) {
    this.variableName = variableName;
  }

  @NotNull
  @Override
  public String getText() {
    return "Remove unused binding '$" + variableName + "'";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "Drools binding fixes";
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

    DroolsBindingVariable bindingVar =
        PsiTreeUtil.getParentOfType(elementAtCaret, DroolsBindingVariable.class, false);
    if (bindingVar == null) {
      return;
    }

    removeBinding(project, file, bindingVar);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    if (element == null) {
      return;
    }

    // The descriptor element might be the binding variable itself or a child of it
    DroolsBindingVariable bindingVar;
    if (element instanceof DroolsBindingVariable droolsBindingVariable) {
      bindingVar = droolsBindingVariable;
    } else {
      bindingVar = PsiTreeUtil.getParentOfType(element, DroolsBindingVariable.class, false);
    }

    if (bindingVar == null) {
      return;
    }

    PsiFile file = element.getContainingFile();
    removeBinding(project, file, bindingVar);
  }

  private void removeBinding(
      @NotNull Project project, @NotNull PsiFile file, @NotNull DroolsBindingVariable bindingVar) {
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    // The binding pattern structure is: $var : FactPattern
    // We need to replace the entire bindingPattern with just the factPattern text
    PsiElement parent = bindingVar.getParent();
    if (!(parent instanceof DroolsBindingPattern bindingPattern)) {
      return;
    }

    DroolsFactPattern factPattern = bindingPattern.getFactPattern();
    String factPatternText = factPattern.getText();

    int startOffset = bindingPattern.getTextRange().getStartOffset();
    int endOffset = bindingPattern.getTextRange().getEndOffset();

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.replaceString(startOffset, endOffset, factPatternText);
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
