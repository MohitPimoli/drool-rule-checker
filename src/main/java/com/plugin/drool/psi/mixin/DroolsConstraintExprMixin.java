package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.plugin.drool.psi.DroolsConstraintExpr;
import com.plugin.drool.psi.DroolsConstraintOperator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.drool.psi.DroolsTypes.DOLLAR;
import static com.plugin.drool.psi.DroolsTypes.IDENTIFIER;
import static com.plugin.drool.psi.DroolsTypes.THIS_KEYWORD;

/**
 * Mixin implementation for DroolsConstraintExpr providing typed navigation methods.
 *
 * <p>A constraint expression typically has the form: fieldName operator value For example: "age >
 * 18", "name == \"John\"", "salary >= 50000"
 */
public abstract class DroolsConstraintExprMixin extends ASTWrapperPsiElement
    implements DroolsConstraintExpr {

  protected DroolsConstraintExprMixin(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Returns the field name (the left-hand side of the constraint). This is typically the first
   * identifier token in the constraint expression. Returns null if no field name can be determined.
   */
  @Nullable
  public String getFieldName() {
    // The first IDENTIFIER or THIS_KEYWORD in the constraint is the field name
    PsiElement child = getFirstChild();
    while (child != null) {
      if (child.getNode().getElementType() == IDENTIFIER
          || child.getNode().getElementType() == THIS_KEYWORD) {
        return child.getText();
      }
      // Skip DOLLAR prefix for binding references
      if (child.getNode().getElementType() == DOLLAR) {
        child = child.getNextSibling();
        if (child != null && child.getNode().getElementType() == IDENTIFIER) {
          return "$" + child.getText();
        }
        continue;
      }
      child = child.getNextSibling();
    }
    return null;
  }

  /**
   * Returns the operator text of the constraint (e.g., "==", ">", "!=", "matches", "contains").
   * Returns null if no operator is found.
   */
  @Nullable
  public String getOperator() {
    List<DroolsConstraintOperator> operators = getConstraintOperatorList();
    if (!operators.isEmpty()) {
      return operators.get(0).getText();
    }
    return null;
  }

  /**
   * Returns the value expression (the right-hand side of the constraint) as text. Returns null if
   * no value expression can be determined.
   */
  @Nullable
  public String getValueExpr() {
    List<DroolsConstraintOperator> operators = getConstraintOperatorList();
    if (operators.isEmpty()) {
      return null;
    }
    // Everything after the first operator is the value expression
    PsiElement operator = operators.get(0);
    PsiElement next = operator.getNextSibling();
    if (next == null) {
      return null;
    }
    // Collect all text after the operator
    StringBuilder sb = new StringBuilder();
    while (next != null) {
      sb.append(next.getText());
      next = next.getNextSibling();
    }
    String result = sb.toString().trim();
    return result.isEmpty() ? null : result;
  }
}
