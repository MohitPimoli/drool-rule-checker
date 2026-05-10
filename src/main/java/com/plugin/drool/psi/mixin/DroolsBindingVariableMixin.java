package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.plugin.drool.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Mixin implementation for DroolsBindingVariable providing typed navigation methods. */
public abstract class DroolsBindingVariableMixin extends ASTWrapperPsiElement
    implements DroolsBindingVariable {

  protected DroolsBindingVariableMixin(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Returns the variable name (without the $ prefix). For example, for "$person : Person()",
   * returns "person".
   */
  @NotNull
  public String getVariableName() {
    PsiElement identifier = getIdentifier();
    return identifier.getText();
  }

  /**
   * Returns the fact pattern that this variable is bound to. The binding pattern structure is:
   * bindingVariable COLON factPattern So the fact pattern is a sibling within the parent
   * bindingPattern.
   */
  @Nullable
  public DroolsFactPattern getBoundPattern() {
    PsiElement parent = getParent();
    if (parent instanceof DroolsBindingPattern droolsBindingPattern) {
      return droolsBindingPattern.getFactPattern();
    }
    return null;
  }
}
