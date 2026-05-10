package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.plugin.drool.psi.DroolsImportPath;
import com.plugin.drool.psi.DroolsImportStatement;
import org.jetbrains.annotations.NotNull;

/** Mixin implementation for DroolsImportStatement providing typed navigation methods. */
public abstract class DroolsImportStatementMixin extends ASTWrapperPsiElement
    implements DroolsImportStatement {

  protected DroolsImportStatementMixin(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Returns the full import path as a string (e.g., "com.example.MyClass" or "com.example.*").
   * Returns empty string if the import path element is missing.
   */
  @NotNull
  public String getImportPathString() {
    DroolsImportPath importPath = getImportPath();
    if (importPath == null) {
      return "";
    }
    return importPath.getText();
  }

  /** Returns whether this is a wildcard import (ends with .*). */
  public boolean isWildcard() {
    DroolsImportPath importPath = getImportPath();
    if (importPath == null) {
      return false;
    }
    // The grammar defines importPath as: qualifiedName (DOT OPERATOR)?
    // where OPERATOR covers '*' for wildcard imports
    PsiElement operator = importPath.getOperator();
    return operator != null && "*".equals(operator.getText());
  }
}
