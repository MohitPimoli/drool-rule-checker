package com.plugin.drool;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

class DroolsPsiElement extends ASTWrapperPsiElement {
  public DroolsPsiElement(@NotNull ASTNode node) {
    super(node);
  }
}
