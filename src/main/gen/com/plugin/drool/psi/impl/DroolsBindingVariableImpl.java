// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.drool.psi.DroolsTypes.*;
import com.plugin.drool.psi.mixin.DroolsBindingVariableMixin;
import com.plugin.drool.psi.*;

public class DroolsBindingVariableImpl extends DroolsBindingVariableMixin implements DroolsBindingVariable {

  public DroolsBindingVariableImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitBindingVariable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return findNotNullChildByType(IDENTIFIER);
  }

}
