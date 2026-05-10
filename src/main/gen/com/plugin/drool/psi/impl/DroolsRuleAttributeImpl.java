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

public class DroolsRuleAttributeImpl extends ASTWrapperPsiElement implements DroolsRuleAttribute {

  public DroolsRuleAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitRuleAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DroolsAgendaGroupAttribute getAgendaGroupAttribute() {
    return findChildByClass(DroolsAgendaGroupAttribute.class);
  }

  @Override
  @Nullable
  public DroolsAutoFocusAttribute getAutoFocusAttribute() {
    return findChildByClass(DroolsAutoFocusAttribute.class);
  }

  @Override
  @Nullable
  public DroolsDateEffectiveAttribute getDateEffectiveAttribute() {
    return findChildByClass(DroolsDateEffectiveAttribute.class);
  }

  @Override
  @Nullable
  public DroolsDateExpiresAttribute getDateExpiresAttribute() {
    return findChildByClass(DroolsDateExpiresAttribute.class);
  }

  @Override
  @Nullable
  public DroolsDialectAttribute getDialectAttribute() {
    return findChildByClass(DroolsDialectAttribute.class);
  }

  @Override
  @Nullable
  public DroolsDurationAttribute getDurationAttribute() {
    return findChildByClass(DroolsDurationAttribute.class);
  }

  @Override
  @Nullable
  public DroolsEnabledAttribute getEnabledAttribute() {
    return findChildByClass(DroolsEnabledAttribute.class);
  }

  @Override
  @Nullable
  public DroolsExtendsAttribute getExtendsAttribute() {
    return findChildByClass(DroolsExtendsAttribute.class);
  }

  @Override
  @Nullable
  public DroolsLockOnActiveAttribute getLockOnActiveAttribute() {
    return findChildByClass(DroolsLockOnActiveAttribute.class);
  }

  @Override
  @Nullable
  public DroolsNoLoopAttribute getNoLoopAttribute() {
    return findChildByClass(DroolsNoLoopAttribute.class);
  }

  @Override
  @Nullable
  public DroolsRuleflowGroupAttribute getRuleflowGroupAttribute() {
    return findChildByClass(DroolsRuleflowGroupAttribute.class);
  }

  @Override
  @Nullable
  public DroolsSalienceAttribute getSalienceAttribute() {
    return findChildByClass(DroolsSalienceAttribute.class);
  }

  @Override
  @Nullable
  public DroolsTimerAttribute getTimerAttribute() {
    return findChildByClass(DroolsTimerAttribute.class);
  }

}
