// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.plugin.drool.psi.mixin.DroolsConstraintExprMixin;
import com.plugin.drool.psi.*;

public class DroolsConstraintExprImpl extends DroolsConstraintExprMixin implements DroolsConstraintExpr {

  public DroolsConstraintExprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitConstraintExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DroolsClassName> getClassNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsClassName.class);
  }

  @Override
  @NotNull
  public List<DroolsConstraintOperator> getConstraintOperatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsConstraintOperator.class);
  }

  @Override
  @NotNull
  public List<DroolsExpressionContent> getExpressionContentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsExpressionContent.class);
  }

  @Override
  @NotNull
  public List<DroolsLiteral> getLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsLiteral.class);
  }

}
