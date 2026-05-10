// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.plugin.drool.psi.*;

public class DroolsConstraintListImpl extends ASTWrapperPsiElement implements DroolsConstraintList {

  public DroolsConstraintListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitConstraintList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DroolsConstraintExpr> getConstraintExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsConstraintExpr.class);
  }

}
