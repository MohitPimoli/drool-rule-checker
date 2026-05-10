// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.plugin.drool.psi.mixin.DroolsWhenClauseMixin;
import com.plugin.drool.psi.*;

public class DroolsWhenClauseImpl extends DroolsWhenClauseMixin implements DroolsWhenClause {

  public DroolsWhenClauseImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitWhenClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DroolsPattern> getPatternList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsPattern.class);
  }

}
