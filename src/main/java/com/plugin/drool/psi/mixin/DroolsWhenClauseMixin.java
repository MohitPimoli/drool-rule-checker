package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.psi.*;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** Mixin implementation for DroolsWhenClause providing typed navigation methods. */
public abstract class DroolsWhenClauseMixin extends ASTWrapperPsiElement
    implements DroolsWhenClause {

  protected DroolsWhenClauseMixin(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Returns all patterns (fact patterns, binding patterns, conditional elements) in this when
   * clause.
   */
  @NotNull
  public List<DroolsPattern> getPatterns() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsPattern.class);
  }

  /**
   * Returns all binding variables declared in this when clause. Searches recursively through all
   * patterns to find binding variables.
   */
  @NotNull
  public List<DroolsBindingVariable> getBindings() {
    List<DroolsBindingVariable> bindings = new ArrayList<>();
    List<DroolsPattern> patterns = getPatterns();
    for (DroolsPattern pattern : patterns) {
      DroolsBindingPattern bindingPattern = pattern.getBindingPattern();
      if (bindingPattern != null) {
        bindings.add(bindingPattern.getBindingVariable());
      }
    }
    return bindings;
  }
}
