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
import com.plugin.drool.DroolsPsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that inserts a missing semicolon at the end of the current line in the then-clause.
 * Inserts {@code ;} at the end of the line where the caret is positioned.
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

    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    int offset = editor.getCaretModel().getOffset();
    int lineNumber = document.getLineNumber(offset);
    int lineEnd = document.getLineEndOffset(lineNumber);

    // Find the end of meaningful text on this line (before trailing whitespace)
    String lineText =
        document.getText(
            new com.intellij.openapi.util.TextRange(
                document.getLineStartOffset(lineNumber), lineEnd));
    int trimmedLength = lineText.length();
    while (trimmedLength > 0 && Character.isWhitespace(lineText.charAt(trimmedLength - 1))) {
      trimmedLength--;
    }

    int insertOffset = document.getLineStartOffset(lineNumber) + trimmedLength;

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.insertString(insertOffset, ";");
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    if (element == null) {
      return;
    }

    PsiFile file = element.getContainingFile();
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    // Insert semicolon at the end of the element's text
    int endOffset = element.getTextRange().getEndOffset();
    String text = element.getText();
    int trimmedLength = text.length();
    while (trimmedLength > 0 && Character.isWhitespace(text.charAt(trimmedLength - 1))) {
      trimmedLength--;
    }

    int insertOffset = element.getTextRange().getStartOffset() + trimmedLength;

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.insertString(insertOffset, ";");
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
