// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DroolsConstraintExpr extends PsiElement {

  @NotNull
  List<DroolsClassName> getClassNameList();

  @NotNull
  List<DroolsConstraintOperator> getConstraintOperatorList();

  @NotNull
  List<DroolsExpressionContent> getExpressionContentList();

  @NotNull
  List<DroolsLiteral> getLiteralList();

}
