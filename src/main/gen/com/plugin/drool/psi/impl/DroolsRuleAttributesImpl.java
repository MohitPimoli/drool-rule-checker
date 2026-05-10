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

public class DroolsRuleAttributesImpl extends ASTWrapperPsiElement implements DroolsRuleAttributes {

  public DroolsRuleAttributesImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitRuleAttributes(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DroolsRuleAttribute> getRuleAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsRuleAttribute.class);
  }

}
