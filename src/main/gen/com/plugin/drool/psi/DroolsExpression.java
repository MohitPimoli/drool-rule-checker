// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DroolsExpression extends PsiElement {

  @NotNull
  List<DroolsBlockContent> getBlockContentList();

  @NotNull
  List<DroolsExpressionContent> getExpressionContentList();

}
