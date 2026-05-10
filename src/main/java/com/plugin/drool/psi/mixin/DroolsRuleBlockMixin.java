package com.plugin.drool.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.psi.DroolsRuleAttributes;
import com.plugin.drool.psi.DroolsRuleBlock;
import com.plugin.drool.psi.DroolsRuleName;
import com.plugin.drool.psi.DroolsThenClause;
import com.plugin.drool.psi.DroolsWhenClause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Mixin implementation for DroolsRuleBlock providing typed navigation methods. Generated Impl class
 * will extend this instead of ASTWrapperPsiElement directly.
 */
public abstract class DroolsRuleBlockMixin extends ASTWrapperPsiElement implements DroolsRuleBlock {

  protected DroolsRuleBlockMixin(@NotNull ASTNode node) {
    super(node);
  }

  /** Returns the rule name PSI element. */
  @Nullable
  public DroolsRuleName getRuleName() {
    return PsiTreeUtil.getChildOfType(this, DroolsRuleName.class);
  }

  /** Returns the rule attributes block, or null if no attributes are defined. */
  @Nullable
  public DroolsRuleAttributes getAttributes() {
    return PsiTreeUtil.getChildOfType(this, DroolsRuleAttributes.class);
  }

  /** Returns the when clause of this rule, or null if missing (error state). */
  @Nullable
  public DroolsWhenClause getWhenClause() {
    return PsiTreeUtil.getChildOfType(this, DroolsWhenClause.class);
  }

  /** Returns the then clause of this rule, or null if missing (error state). */
  @Nullable
  public DroolsThenClause getThenClause() {
    return PsiTreeUtil.getChildOfType(this, DroolsThenClause.class);
  }

  /**
   * Returns the rule name as a plain string, stripping quotes if present. Returns empty string if
   * the rule name element is missing.
   */
  @NotNull
  public String getNameString() {
    DroolsRuleName ruleName = getRuleName();
    if (ruleName == null) {
      return "";
    }
    String text = ruleName.getText();
    if (text == null) {
      return "";
    }
    // Strip surrounding quotes if present
    if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
      return text.substring(1, text.length() - 1);
    }
    return text;
  }
}
