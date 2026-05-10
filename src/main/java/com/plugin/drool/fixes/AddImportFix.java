package com.plugin.drool.fixes;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.plugin.drool.DroolsPsiFile;
import com.plugin.drool.psi.DroolsImportStatement;
import com.plugin.drool.psi.DroolsPackageDecl;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Quick-fix that inserts an import statement for an unresolved class. Inserts {@code import
 * fully.qualified.ClassName;} after the last existing import, or after the package declaration if
 * no imports exist, or at the beginning of the file.
 */
public class AddImportFix implements IntentionAction, LocalQuickFix {

  private final String fullyQualifiedClassName;

  public AddImportFix(@NotNull String fullyQualifiedClassName) {
    this.fullyQualifiedClassName = fullyQualifiedClassName;
  }

  @NotNull
  @Override
  public String getText() {
    return "Add import for '" + fullyQualifiedClassName + "'";
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
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return;
    }
    insertImport(project, droolsFile);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiFile file = descriptor.getPsiElement().getContainingFile();
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return;
    }
    insertImport(project, droolsFile);
  }

  private void insertImport(@NotNull Project project, @NotNull DroolsPsiFile droolsFile) {
    Document document = PsiDocumentManager.getInstance(project).getDocument(droolsFile);
    if (document == null) {
      return;
    }

    String importLine = "import " + fullyQualifiedClassName + ";";
    int insertOffset = findInsertOffset(droolsFile, document);

    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    document.insertString(insertOffset, importLine + "\n");
    PsiDocumentManager.getInstance(project).commitDocument(document);
  }

  /**
   * Determines the offset at which to insert the new import statement. Strategy: 1. After the last
   * existing import statement (on a new line) 2. After the package declaration (on a new line) 3.
   * At the beginning of the file
   */
  private int findInsertOffset(@NotNull DroolsPsiFile droolsFile, @NotNull Document document) {
    // Try to insert after the last import
    List<DroolsImportStatement> imports = droolsFile.getImports();
    if (!imports.isEmpty()) {
      DroolsImportStatement lastImport = imports.get(imports.size() - 1);
      int endOffset = lastImport.getTextRange().getEndOffset();
      // Move to the start of the next line
      int lineNumber = document.getLineNumber(endOffset);
      if (lineNumber + 1 < document.getLineCount()) {
        return document.getLineStartOffset(lineNumber + 1);
      }
      return endOffset;
    }

    // Try to insert after the package declaration
    DroolsPackageDecl packageDecl = droolsFile.getPackageDecl();
    if (packageDecl != null) {
      int endOffset = packageDecl.getTextRange().getEndOffset();
      int lineNumber = document.getLineNumber(endOffset);
      if (lineNumber + 1 < document.getLineCount()) {
        return document.getLineStartOffset(lineNumber + 1);
      }
      return endOffset;
    }

    // Insert at the beginning of the file
    return 0;
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }
}
