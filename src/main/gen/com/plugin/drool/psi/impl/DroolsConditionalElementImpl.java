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

public class DroolsConditionalElementImpl extends ASTWrapperPsiElement implements DroolsConditionalElement {

  public DroolsConditionalElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DroolsVisitor visitor) {
    visitor.visitConditionalElement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DroolsVisitor) accept((DroolsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DroolsAccumElement getAccumElement() {
    return findChildByClass(DroolsAccumElement.class);
  }

  @Override
  @Nullable
  public DroolsEvalElement getEvalElement() {
    return findChildByClass(DroolsEvalElement.class);
  }

  @Override
  @Nullable
  public DroolsExistsElement getExistsElement() {
    return findChildByClass(DroolsExistsElement.class);
  }

  @Override
  @Nullable
  public DroolsForallElement getForallElement() {
    return findChildByClass(DroolsForallElement.class);
  }

  @Override
  @Nullable
  public DroolsFromElement getFromElement() {
    return findChildByClass(DroolsFromElement.class);
  }

  @Override
  @Nullable
  public DroolsNotElement getNotElement() {
    return findChildByClass(DroolsNotElement.class);
  }

}
