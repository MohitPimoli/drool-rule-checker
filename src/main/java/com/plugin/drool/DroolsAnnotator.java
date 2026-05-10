package com.plugin.drool;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.fixes.AddImportFix;
import com.plugin.drool.fixes.RemoveBindingFix;
import com.plugin.drool.fixes.RemoveUnusedImportFix;
import com.plugin.drool.fixes.RenameDuplicateRuleFix;
import com.plugin.drool.psi.DroolsAttributeValue;
import com.plugin.drool.psi.DroolsBindingVariable;
import com.plugin.drool.psi.DroolsClassName;
import com.plugin.drool.psi.DroolsConstraintExpr;
import com.plugin.drool.psi.DroolsConstraintList;
import com.plugin.drool.psi.DroolsConstraintOperator;
import com.plugin.drool.psi.DroolsDateEffectiveAttribute;
import com.plugin.drool.psi.DroolsDateExpiresAttribute;
import com.plugin.drool.psi.DroolsDeclareBlock;
import com.plugin.drool.psi.DroolsExpressionContent;
import com.plugin.drool.psi.DroolsFactPattern;
import com.plugin.drool.psi.DroolsFunctionDef;
import com.plugin.drool.psi.DroolsGlobalDecl;
import com.plugin.drool.psi.DroolsImportPath;
import com.plugin.drool.psi.DroolsImportStatement;
import com.plugin.drool.psi.DroolsRuleAttribute;
import com.plugin.drool.psi.DroolsRuleAttributes;
import com.plugin.drool.psi.DroolsRuleBlock;
import com.plugin.drool.psi.DroolsRuleName;
import com.plugin.drool.psi.DroolsSalienceAttribute;
import com.plugin.drool.psi.DroolsThenClause;
import com.plugin.drool.psi.DroolsTypeName;
import com.plugin.drool.psi.DroolsWhenClause;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.drool.util.DroolsConstants.isJavaLangClass;

/**
 * PSI-based annotator for Drools DRL files.
 *
 * <p>Uses typed PSI element checks to validate rule structure, attributes, imports, bindings,
 * constraints, and Java syntax in then-clauses. Each validation method registers annotations with
 * associated quick-fix actions.
 */
public class DroolsAnnotator implements Annotator {

  private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile("\"\\d{1,2}-\\w{3}-\\d{4}\"");

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (element instanceof DroolsRuleBlock rule) {
      validateRuleStructure(rule, holder);
      validateRuleAttributes(rule, holder);
      validateDuplicateRuleName(rule, holder);
    } else if (element instanceof DroolsFactPattern pattern) {
      validateFactPatternType(pattern, holder);
      validateConstraints(pattern, holder);
    } else if (element instanceof DroolsImportStatement importStmt) {
      validateImportResolution(importStmt, holder);
      validateDuplicateImport(importStmt, holder);
      validateUnusedImport(importStmt, holder);
    } else if (element instanceof DroolsBindingVariable binding) {
      validateBindingUsage(binding, holder);
    } else if (element instanceof DroolsThenClause thenClause) {
      validateJavaSyntax(thenClause, holder);
    } else if (element instanceof DroolsGlobalDecl global) {
      validateGlobalType(global, holder);
    }
  }

  // ========== Rule Structure Validation ==========

  /**
   * Validates that a rule block has the required when/then/end clauses in correct order. Reports
   * errors for missing clauses or incorrect clause ordering.
   */
  private void validateRuleStructure(
      @NotNull DroolsRuleBlock rule, @NotNull AnnotationHolder holder) {
    DroolsWhenClause whenClause = rule.getWhenClause();
    DroolsThenClause thenClause = rule.getThenClause();

    // Check for missing when clause
    if (whenClause == null) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Rule is missing 'when' clause")
          .range(rule.getFirstChild() != null ? rule.getFirstChild() : rule)
          .withFix(createNoOpFix("Add 'when' clause"))
          .create();
      return;
    }

    // Check for missing then clause
    if (thenClause == null) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Rule is missing 'then' clause")
          .range(rule.getFirstChild() != null ? rule.getFirstChild() : rule)
          .withFix(createNoOpFix("Add 'then' clause"))
          .create();
      return;
    }

    // Check clause order: when must come before then
    if (whenClause.getTextOffset() > thenClause.getTextOffset()) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "'when' clause must come before 'then' clause")
          .range(whenClause)
          .create();
    }

    // Check for missing 'end' keyword — if the rule text doesn't end with 'end'
    String ruleText = rule.getText().trim();
    if (!ruleText.endsWith("end")) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Rule is missing 'end' keyword")
          .range(rule.getLastChild() != null ? rule.getLastChild() : rule)
          .withFix(createNoOpFix("Add 'end' keyword"))
          .create();
    }
  }

  // ========== Rule Attributes Validation ==========

  /** Validates rule attribute values: salience must be numeric, date formats must be valid. */
  private void validateRuleAttributes(
      @NotNull DroolsRuleBlock rule, @NotNull AnnotationHolder holder) {
    DroolsRuleAttributes attributes = rule.getRuleAttributes();
    if (attributes == null) {
      return;
    }

    for (DroolsRuleAttribute attribute : attributes.getRuleAttributeList()) {
      validateSalienceAttribute(attribute, holder);
      validateDateAttribute(attribute.getDateEffectiveAttribute(), "date-effective", holder);
      validateDateAttribute(attribute.getDateExpiresAttribute(), "date-expires", holder);
    }
  }

  private void validateSalienceAttribute(
      @NotNull DroolsRuleAttribute attribute, @NotNull AnnotationHolder holder) {
    DroolsSalienceAttribute salience = attribute.getSalienceAttribute();
    if (salience == null) {
      return;
    }

    DroolsAttributeValue value = salience.getAttributeValue();
    if (value == null) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Salience attribute requires a value")
          .range(salience)
          .create();
      return;
    }

    // Salience must be numeric
    PsiElement numberElement = value.getNumber();
    if (numberElement == null) {
      // Value is present but not a number — check if it's a string or identifier
      String valueText = value.getText().trim();
      if (!valueText.isEmpty() && !isNumeric(valueText)) {
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Salience value must be numeric, found: '" + valueText + "'")
            .range(value)
            .create();
      }
    }
  }

  private void validateDateAttribute(
      @Nullable PsiElement dateAttr, @NotNull String attrName, @NotNull AnnotationHolder holder) {
    if (dateAttr == null) {
      return;
    }

    // Get the attribute value from the date attribute
    DroolsAttributeValue value = null;
    if (dateAttr instanceof DroolsDateEffectiveAttribute effective) {
      value = effective.getAttributeValue();
    } else if (dateAttr instanceof DroolsDateExpiresAttribute expires) {
      value = expires.getAttributeValue();
    }

    if (value == null) {
      return;
    }

    PsiElement stringElement = value.getString();
    if (stringElement != null) {
      String dateStr = stringElement.getText();
      // Drools date format: "dd-MMM-yyyy" e.g., "01-Jan-2024"
      if (!DATE_FORMAT_PATTERN.matcher(dateStr).matches()) {
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Invalid date format for '"
                    + attrName
                    + "'. Expected format: \"dd-MMM-yyyy\" (e.g., \"01-Jan-2024\")")
            .range(value)
            .create();
      }
    }
  }

  // ========== Duplicate Rule Name Validation ==========

  /**
   * Checks for duplicate rule names within the same file. Reports a warning on each duplicate
   * occurrence after the first.
   */
  private void validateDuplicateRuleName(
      @NotNull DroolsRuleBlock rule, @NotNull AnnotationHolder holder) {
    DroolsRuleName ruleName = rule.getRuleName();
    if (ruleName == null) {
      return;
    }

    String nameStr = getNameString(rule);
    if (nameStr.isEmpty()) {
      return;
    }

    PsiFile file = rule.getContainingFile();
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return;
    }

    List<DroolsRuleBlock> allRules = droolsFile.getRules();
    boolean foundFirst = false;
    for (DroolsRuleBlock otherRule : allRules) {
      String otherName = getNameString(otherRule);
      if (nameStr.equals(otherName)) {
        if (!foundFirst) {
          // This is the first occurrence — skip it
          foundFirst = true;
        } else if (otherRule == rule) {
          // This is a duplicate
          int suffix = 2;
          String suggestedName = nameStr + "_" + suffix;
          holder
              .newAnnotation(HighlightSeverity.WARNING, "Duplicate rule name '" + nameStr + "'")
              .range(ruleName)
              .withFix(new RenameDuplicateRuleFix(suggestedName))
              .create();
          return;
        }
      }
    }
  }

  // ========== Fact Pattern Type Validation ==========

  /**
   * Validates that the class referenced in a fact pattern can be resolved via the classpath. Uses
   * DroolsResolutionCache for efficient lookups.
   */
  private void validateFactPatternType(
      @NotNull DroolsFactPattern pattern, @NotNull AnnotationHolder holder) {
    DroolsClassName className = pattern.getClassName();

    String classText = className.getText().trim();
    if (classText.isEmpty()) {
      return;
    }

    // Skip validation for java.lang classes and Drools built-ins
    if (isJavaLangClass(classText)) {
      return;
    }

    // Try to resolve the class using the resolution cache
    try {
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(pattern.getProject());

      // First check if the class is imported — get the FQN from imports
      String fqn = resolveClassFqn(classText, pattern);
      if (fqn == null) {
        // No import found — the class might still be resolvable by simple name
        fqn = classText;
      }

      PsiClass resolved = cache.resolveClass(fqn, pattern);
      if (resolved == null && !fqn.equals(classText)) {
        // Try with just the simple name in case it's in the default package
        resolved = cache.resolveClass(classText, pattern);
      }

      if (resolved == null) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Cannot resolve class '" + classText + "'")
            .range(className)
            .withFix(new AddImportFix(classText))
            .create();
      }
    } catch (Exception e) {
      // Fail-open: if resolution fails due to indexing, don't report false positives
    }
  }

  // ========== Constraint Validation ==========

  /**
   * Validates constraints within a fact pattern for unbalanced parentheses and missing operators.
   */
  private void validateConstraints(
      @NotNull DroolsFactPattern pattern, @NotNull AnnotationHolder holder) {
    DroolsConstraintList constraintList = pattern.getConstraintList();
    if (constraintList == null) {
      return;
    }

    for (DroolsConstraintExpr constraint : constraintList.getConstraintExprList()) {
      validateConstraintParentheses(constraint, holder);
      validateConstraintOperator(constraint, holder);
    }
  }

  private void validateConstraintParentheses(
      @NotNull DroolsConstraintExpr constraint, @NotNull AnnotationHolder holder) {
    String text = constraint.getText();
    int depth = 0;
    for (char c : text.toCharArray()) {
      if (c == '(') depth++;
      else if (c == ')') depth--;
      if (depth < 0) {
        holder
            .newAnnotation(
                HighlightSeverity.ERROR, "Unbalanced parentheses in constraint expression")
            .range(constraint)
            .create();
        return;
      }
    }
    if (depth != 0) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Unbalanced parentheses in constraint expression")
          .range(constraint)
          .create();
    }
  }

  private void validateConstraintOperator(
      @NotNull DroolsConstraintExpr constraint, @NotNull AnnotationHolder holder) {
    // A constraint expression should have at least one operator
    // unless it's a simple boolean field reference or method call
    List<DroolsConstraintOperator> operators = constraint.getConstraintOperatorList();
    List<DroolsExpressionContent> expressions = constraint.getExpressionContentList();

    // If there are multiple expression parts but no operator, it's likely missing one
    if (operators.isEmpty() && expressions.size() > 1) {
      holder
          .newAnnotation(
              HighlightSeverity.ERROR, "Missing comparison operator in constraint expression")
          .range(constraint)
          .create();
    }
  }

  // ========== Import Resolution Validation ==========

  /** Validates that an import statement references a resolvable class on the classpath. */
  private void validateImportResolution(
      @NotNull DroolsImportStatement importStmt, @NotNull AnnotationHolder holder) {
    DroolsImportPath importPath = importStmt.getImportPath();
    if (importPath == null) {
      return;
    }

    String pathText = importPath.getText().trim();
    if (pathText.isEmpty()) {
      holder.newAnnotation(HighlightSeverity.ERROR, "Empty import path").range(importStmt).create();
      return;
    }

    // Skip wildcard imports — just verify the package portion
    PsiElement operator = importPath.getOperator();
    boolean isWildcard = operator != null && "*".equals(operator.getText());

    if (isWildcard) {
      // For wildcard imports, we could validate the package exists
      // but this is less critical — skip for now
      return;
    }

    // Try to resolve the fully qualified class name
    try {
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(importStmt.getProject());
      PsiClass resolved = cache.resolveClass(pathText, importStmt);
      if (resolved == null) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Cannot resolve class '" + pathText + "'")
            .range(importPath)
            .withFix(new AddImportFix(pathText))
            .create();
      }
    } catch (Exception e) {
      // Fail-open during indexing
    }
  }

  // ========== Duplicate Import Validation ==========

  /**
   * Checks for duplicate import statements within the same file. Reports a warning on each
   * duplicate after the first.
   */
  private void validateDuplicateImport(
      @NotNull DroolsImportStatement importStmt, @NotNull AnnotationHolder holder) {
    DroolsImportPath importPath = importStmt.getImportPath();
    if (importPath == null) {
      return;
    }

    String pathText = importPath.getText().trim();
    if (pathText.isEmpty()) {
      return;
    }

    PsiFile file = importStmt.getContainingFile();
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return;
    }

    List<DroolsImportStatement> allImports = droolsFile.getImports();
    boolean foundFirst = false;
    for (DroolsImportStatement other : allImports) {
      DroolsImportPath otherPath = other.getImportPath();
      if (otherPath == null) continue;

      String otherText = otherPath.getText().trim();
      if (pathText.equals(otherText)) {
        if (!foundFirst) {
          foundFirst = true;
        } else if (other == importStmt) {
          holder
              .newAnnotation(HighlightSeverity.WARNING, "Duplicate import '" + pathText + "'")
              .range(importStmt)
              .withFix(new RemoveUnusedImportFix())
              .create();
          return;
        }
      }
    }
  }

  // ========== Unused Import Validation ==========

  /**
   * Checks whether an import is actually used in the file. Reports a weak warning for unused
   * imports.
   */
  private void validateUnusedImport(
      @NotNull DroolsImportStatement importStmt, @NotNull AnnotationHolder holder) {
    DroolsImportPath importPath = importStmt.getImportPath();
    if (importPath == null) {
      return;
    }

    String pathText = importPath.getText().trim();
    if (pathText.isEmpty()) {
      return;
    }

    // Wildcard imports are always considered "used"
    PsiElement operator = importPath.getOperator();
    if (operator != null && "*".equals(operator.getText())) {
      return;
    }

    // Extract simple class name from the fully qualified path
    String simpleClassName =
        pathText.contains(".") ? pathText.substring(pathText.lastIndexOf('.') + 1) : pathText;

    PsiFile file = importStmt.getContainingFile();
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return;
    }

    // Check if the class is used in any rule, global, or other construct
    if (!isClassUsedInFile(simpleClassName, droolsFile)) {
      holder
          .newAnnotation(HighlightSeverity.WEAK_WARNING, "Unused import '" + pathText + "'")
          .range(importStmt)
          .withFix(new RemoveUnusedImportFix())
          .create();
    }
  }

  // ========== Binding Variable Usage Validation ==========

  /**
   * Validates that a binding variable is actually used within the same rule. Reports a warning for
   * unused bindings.
   */
  private void validateBindingUsage(
      @NotNull DroolsBindingVariable binding, @NotNull AnnotationHolder holder) {
    String varName = binding.getIdentifier().getText();
    if (varName == null || varName.isEmpty()) {
      return;
    }

    // Find the containing rule block
    DroolsRuleBlock rule = PsiTreeUtil.getParentOfType(binding, DroolsRuleBlock.class);
    if (rule == null) {
      return;
    }

    // Check if the binding variable is referenced elsewhere in the rule
    String bindingRef = "$" + varName;
    String ruleText = rule.getText();

    // Count occurrences of the binding reference in the rule text
    // The declaration itself is one occurrence, so we need at least 2
    int count = countOccurrences(ruleText, bindingRef);
    if (count <= 1) {
      holder
          .newAnnotation(
              HighlightSeverity.WARNING, "Binding variable '$" + varName + "' is never used")
          .range(binding)
          .withFix(new RemoveBindingFix(varName))
          .create();
    }
  }

  // ========== Java Syntax Validation in Then-Clause ==========

  /**
   * Validates Java syntax in then-clause content. Checks for unbalanced braces in the overall
   * then-clause text.
   */
  private void validateJavaSyntax(
      @NotNull DroolsThenClause thenClause, @NotNull AnnotationHolder holder) {
    String thenText = thenClause.getText();
    if (thenText == null || thenText.isEmpty()) {
      return;
    }

    // Strip the 'then' keyword prefix to get just the content
    int thenKeywordEnd = thenText.indexOf("then");
    if (thenKeywordEnd >= 0) {
      thenText = thenText.substring(thenKeywordEnd + 4);
    }

    // Validate balanced braces in the then-clause content
    validateThenClauseBraces(thenClause, thenText, holder);
  }

  private void validateThenClauseBraces(
      @NotNull DroolsThenClause thenClause,
      @NotNull String text,
      @NotNull AnnotationHolder holder) {
    int depth = 0;
    for (char c : text.toCharArray()) {
      if (c == '{') depth++;
      else if (c == '}') depth--;
      if (depth < 0) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Unbalanced braces in then-clause")
            .range(thenClause)
            .create();
        return;
      }
    }
    if (depth != 0) {
      holder
          .newAnnotation(HighlightSeverity.ERROR, "Unbalanced braces in then-clause")
          .range(thenClause)
          .create();
    }
  }

  // ========== Global Type Validation ==========

  /** Validates that the type referenced in a global declaration can be resolved. */
  private void validateGlobalType(
      @NotNull DroolsGlobalDecl global, @NotNull AnnotationHolder holder) {
    DroolsTypeName typeName = global.getTypeName();
    if (typeName == null) {
      return;
    }

    DroolsClassName className = typeName.getClassName();

    String typeText = className.getText().trim();
    if (typeText.isEmpty() || isJavaLangClass(typeText)) {
      return;
    }

    // Try to resolve the type
    try {
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(global.getProject());
      String fqn = resolveClassFqn(typeText, global);
      if (fqn == null) {
        fqn = typeText;
      }

      PsiClass resolved = cache.resolveClass(fqn, global);
      if (resolved == null && !fqn.equals(typeText)) {
        resolved = cache.resolveClass(typeText, global);
      }

      if (resolved == null) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Cannot resolve type '" + typeText + "'")
            .range(className)
            .withFix(new AddImportFix(typeText))
            .create();
      }
    } catch (Exception e) {
      // Fail-open during indexing
    }
  }

  // ========== Helper Methods ==========

  /** Resolves a simple class name to its fully qualified name using the file's imports. */
  @Nullable
  private String resolveClassFqn(@NotNull String simpleClassName, @NotNull PsiElement context) {
    PsiFile file = context.getContainingFile();
    if (!(file instanceof DroolsPsiFile droolsFile)) {
      return null;
    }

    for (DroolsImportStatement importStmt : droolsFile.getImports()) {
      DroolsImportPath importPath = importStmt.getImportPath();
      if (importPath == null) continue;

      String pathText = importPath.getText().trim();
      if (pathText.endsWith("." + simpleClassName)) {
        return pathText;
      }
    }
    return null;
  }

  /** Checks if a class name is used anywhere in the file (excluding the import itself). */
  private boolean isClassUsedInFile(@NotNull String simpleClassName, @NotNull DroolsPsiFile file) {
    // Check in rule blocks (when-clause fact patterns and then-clause)
    for (DroolsRuleBlock rule : file.getRules()) {
      if (isClassUsedInRule(simpleClassName, rule)) {
        return true;
      }
    }

    // Check in global declarations
    for (DroolsGlobalDecl global : file.getGlobals()) {
      DroolsTypeName typeName = global.getTypeName();
      if (typeName != null) {
        DroolsClassName className = typeName.getClassName();
        if (simpleClassName.equals(className.getText().trim())) {
          return true;
        }
      }
    }

    // Check in declare blocks
    for (DroolsDeclareBlock declare : file.getDeclares()) {
      if (declare.getText().contains(simpleClassName)) {
        return true;
      }
    }

    // Check in function definitions
    for (DroolsFunctionDef function : file.getFunctions()) {
      if (function.getText().contains(simpleClassName)) {
        return true;
      }
    }

    return false;
  }

  /** Checks if a class name is used within a rule block. */
  private boolean isClassUsedInRule(
      @NotNull String simpleClassName, @NotNull DroolsRuleBlock rule) {
    // Check when-clause fact patterns
    DroolsWhenClause whenClause = rule.getWhenClause();
    if (whenClause != null) {
      Collection<DroolsFactPattern> patterns =
          PsiTreeUtil.findChildrenOfType(whenClause, DroolsFactPattern.class);
      for (DroolsFactPattern pattern : patterns) {
        DroolsClassName className = pattern.getClassName();
        if (simpleClassName.equals(className.getText().trim())) {
          return true;
        }
      }
    }

    // Check then-clause for class usage
    DroolsThenClause thenClause = rule.getThenClause();
    return thenClause != null && thenClause.getText().contains(simpleClassName);
  }

  /** Gets the rule name as a plain string, stripping quotes if present. */
  @NotNull
  private String getNameString(@NotNull DroolsRuleBlock rule) {
    DroolsRuleName ruleName = rule.getRuleName();
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

  /** Counts occurrences of a substring in a string. */
  private int countOccurrences(@NotNull String text, @NotNull String sub) {
    int count = 0;
    int idx = 0;
    while ((idx = text.indexOf(sub, idx)) != -1) {
      // Ensure it's a word boundary (not part of a longer identifier)
      int end = idx + sub.length();
      boolean startBoundary = idx == 0 || !Character.isLetterOrDigit(text.charAt(idx - 1));
      boolean endBoundary = end >= text.length() || !Character.isLetterOrDigit(text.charAt(end));
      if (startBoundary && endBoundary) {
        count++;
      }
      idx = end;
    }
    return count;
  }

  /** Checks if a string represents a numeric value (integer or negative integer). */
  private boolean isNumeric(@NotNull String str) {
    if (str.isEmpty()) return false;
    int start = 0;
    if (str.charAt(0) == '-' || str.charAt(0) == '+') {
      if (str.length() == 1) return false;
      start = 1;
    }
    for (int i = start; i < str.length(); i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Creates a no-op IntentionAction placeholder for structural fixes. These will be fully
   * implemented in Task 12.
   */
  @NotNull
  private IntentionAction createNoOpFix(@NotNull String text) {
    return new IntentionAction() {
      @NotNull
      @Override
      public String getText() {
        return text;
      }

      @NotNull
      @Override
      public String getFamilyName() {
        return "Drools structure fixes";
      }

      @Override
      public boolean isAvailable(
          @NotNull com.intellij.openapi.project.Project project,
          com.intellij.openapi.editor.Editor editor,
          PsiFile file) {
        return true;
      }

      @Override
      public void invoke(
          @NotNull com.intellij.openapi.project.Project project,
          com.intellij.openapi.editor.Editor editor,
          PsiFile file) {
        // Placeholder — will be implemented in Task 12
      }

      @Override
      public boolean startInWriteAction() {
        return true;
      }
    };
  }
}
