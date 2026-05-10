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
import com.plugin.drool.psi.DroolsJavaStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that inserts a missing semicolon at the end of a Java statement in the then-clause.
 * Inserts {@code ;} at the end of the statement text (before any trailing whitespace/newline).
 */
public class AddSemicolonFix implements IntentionAction, LocalQuickFix {

  @NotNull
  @Override
  public String getText() {
    return "Add missing semicolon";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "Drools syntax fixes";
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

    DroolsJavaStatement statement =
        PsiTreeUtil.getParentOfType(elementAtCaret, DroolsJavaStatement.class, false);
    if (statement == null) {
      return;
    }

    insertSemicolon(project, file, statement);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    if (element == null) {
      return;
    }

    // The descriptor element might be the statement itself or a child of it
    DroolsJavaStatement statement;
    if (element instanceof DroolsJavaStatement drlJavaStatement) {
      statement = drlJavaStatement;
    } else {
      statement = PsiTreeUtil.getParentOfType(element, DroolsJavaStatement.class, false);
    }

    if (statement == null) {
      return;
    }

    PsiFile file = element.getContainingFile();
    insertSemicolon(project, file, statement);
  }

  private void insertSemicolon(
      @NotNull Project project, @NotNull PsiFile file, @NotNull DroolsJavaStatement statement) {
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    // Find the end of the meaningful text in the statement (before trailing whitespace)
    // int endOffset = statement.getTextRange().getEndOffset();-
    String text = statement.getText();

    // Trim trailing whitespace to find where to insert the semicolon
    int trimmedLength = text.length();
    while (trimmedLength > 0 && Character.isWhitespace(text.charAt(trimmedLength - 1))) {
      trimmedLength--;
    }

    int insertOffset = statement.getTextRange().getStartOffset() + trimmedLength;

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.insertString(insertOffset, ";");
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
