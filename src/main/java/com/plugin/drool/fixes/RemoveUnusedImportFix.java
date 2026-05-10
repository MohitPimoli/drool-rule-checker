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
import com.plugin.drool.psi.DroolsImportStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that removes an unused import statement. Deletes the entire import statement line
 * including the trailing newline.
 */
public class RemoveUnusedImportFix implements IntentionAction, LocalQuickFix {

  @NotNull
  @Override
  public String getText() {
    return "Remove unused import";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "Drools import fixes";
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

    DroolsImportStatement importStmt =
        PsiTreeUtil.getParentOfType(elementAtCaret, DroolsImportStatement.class, false);
    if (importStmt == null) {
      return;
    }

    deleteImportStatement(project, file, importStmt);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    if (element == null) {
      return;
    }

    // The descriptor element might be the import statement itself or a child of it
    DroolsImportStatement importStmt;
    if (element instanceof DroolsImportStatement droolsImportStatement) {
      importStmt = droolsImportStatement;
    } else {
      importStmt = PsiTreeUtil.getParentOfType(element, DroolsImportStatement.class, false);
    }

    if (importStmt == null) {
      return;
    }

    PsiFile file = element.getContainingFile();
    deleteImportStatement(project, file, importStmt);
  }

  private void deleteImportStatement(
      @NotNull Project project, @NotNull PsiFile file, @NotNull DroolsImportStatement importStmt) {
    Document document = PsiDocumentManager.getInstance(project).getDocument(file);
    if (document == null) {
      return;
    }

    int startOffset = importStmt.getTextRange().getStartOffset();
    // int endOffset = importStmt.getTextRange().getEndOffset();-

    // Determine the line range to delete (include the newline)
    int startLine = document.getLineNumber(startOffset);
    int lineStartOffset = document.getLineStartOffset(startLine);
    int lineEndOffset;
    if (startLine + 1 < document.getLineCount()) {
      lineEndOffset = document.getLineStartOffset(startLine + 1);
    } else {
      lineEndOffset = document.getTextLength();
    }

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.deleteString(lineStartOffset, lineEndOffset);
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
