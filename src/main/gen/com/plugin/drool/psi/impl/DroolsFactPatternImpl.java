// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.plugin.drool.psi.mixin.DroolsFactPatternMixin;
import com.plugin.drool.psi.*;

public class DroolsFactPatternImpl extends DroolsFactPatternMixin implements DroolsFactPattern {

  public DroolsFactPatternImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitFactPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DroolsClassName getClassName() {
    return findNotNullChildByClass(DroolsClassName.class);
  }

  @Override
  @Nullable
  public DroolsConstraintList getConstraintList() {
    return findChildByClass(DroolsConstraintList.class);
  }

  @Override
  @Nullable
  public DroolsLogicalConnective getLogicalConnective() {
    return findChildByClass(DroolsLogicalConnective.class);
  }

  @Override
  @Nullable
  public DroolsPattern getPattern() {
    return findChildByClass(DroolsPattern.class);
  }

}
