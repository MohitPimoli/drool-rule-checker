// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.plugin.drool.psi.mixin.DroolsRuleBlockMixin;
import com.plugin.drool.psi.*;

public class DroolsRuleBlockImpl extends DroolsRuleBlockMixin implements DroolsRuleBlock {

  public DroolsRuleBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitRuleBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DroolsRuleAttributes getRuleAttributes() {
    return findChildByClass(DroolsRuleAttributes.class);
  }

  @Override
  @Nullable
  public DroolsRuleName getRuleName() {
    return findChildByClass(DroolsRuleName.class);
  }

  @Override
  @Nullable
  public DroolsThenClause getThenClause() {
    return findChildByClass(DroolsThenClause.class);
  }

  @Override
  @Nullable
  public DroolsWhenClause getWhenClause() {
    return findChildByClass(DroolsWhenClause.class);
  }

}
