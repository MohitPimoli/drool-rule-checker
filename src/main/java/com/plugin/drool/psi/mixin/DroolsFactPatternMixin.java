package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.psi.DroolsClassName;
import com.plugin.drool.psi.DroolsConstraintExpr;
import com.plugin.drool.psi.DroolsConstraintList;
import com.plugin.drool.psi.DroolsFactPattern;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** Mixin implementation for DroolsFactPattern providing typed navigation methods. */
public abstract class DroolsFactPatternMixin extends ASTWrapperPsiElement
    implements DroolsFactPattern {

  protected DroolsFactPatternMixin(@NotNull ASTNode node) {
    super(node);
  }

  /** Returns the class name element of this fact pattern. */
  @NotNull
  public DroolsClassName getClassName() {
    return findNotNullChildByClass(DroolsClassName.class);
  }

  /**
   * Returns the list of constraint expressions in this fact pattern. Returns an empty list if there
   * are no constraints.
   */
  @NotNull
  public List<DroolsConstraintExpr> getConstraints() {
    DroolsConstraintList constraintList =
        PsiTreeUtil.getChildOfType(this, DroolsConstraintList.class);
    if (constraintList == null) {
      return Collections.emptyList();
    }
    return constraintList.getConstraintExprList();
  }
}
