package com.plugin.drool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.plugin.drool.psi.DroolsBindingPattern;
import com.plugin.drool.psi.DroolsBindingVariable;
import com.plugin.drool.psi.DroolsClassName;
import com.plugin.drool.psi.DroolsConstraintExpr;
import com.plugin.drool.psi.DroolsExpressionContent;
import com.plugin.drool.psi.DroolsFactPattern;
import com.plugin.drool.psi.DroolsImportPath;
import com.plugin.drool.psi.DroolsImportStatement;
import com.plugin.drool.psi.DroolsRuleBlock;
import com.plugin.drool.psi.DroolsThenClause;
import com.plugin.drool.psi.DroolsTypeName;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.drool.util.DroolsConstants.IDEA_RULES;

/**
 * Provides PsiReference implementations for navigable elements in Drools files. Enables
 * go-to-definition (Ctrl+Click) for class names, method calls, and binding variables.
 *
 * <p>Satisfies Requirements: 10.1, 10.2, 10.3, 10.4, 10.5
 */
public class DroolsReferenceContributor extends PsiReferenceContributor {

  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    // Class references in imports, fact patterns, globals
    registrar.registerReferenceProvider(classNamePattern(), new DroolsClassReferenceProvider());
    // Method references after dot
    registrar.registerReferenceProvider(methodCallPattern(), new DroolsMethodReferenceProvider());
    // Binding variable references ($varName)
    registrar.registerReferenceProvider(bindingRefPattern(), new DroolsBindingReferenceProvider());
  }

  // --- Element Pattern Definitions ---

  /** Matches class name elements in imports, fact patterns, globals, and type names. */
  private static ElementPattern<PsiElement> classNamePattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .andOr(
            PlatformPatterns.psiElement().inside(DroolsClassName.class),
            PlatformPatterns.psiElement().inside(DroolsImportPath.class),
            PlatformPatterns.psiElement().inside(DroolsTypeName.class));
  }

  /** Matches elements after a dot (method/field access positions). */
  private static ElementPattern<PsiElement> methodCallPattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .afterLeaf(PlatformPatterns.psiElement().withText("."));
  }

  /** Matches binding variable references (identifiers starting with $ in when/then clauses). */
  private static ElementPattern<PsiElement> bindingRefPattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .andOr(
            PlatformPatterns.psiElement().inside(DroolsThenClause.class),
            PlatformPatterns.psiElement().inside(DroolsConstraintExpr.class),
            PlatformPatterns.psiElement().inside(DroolsExpressionContent.class));
  }

  // --- Reference Providers ---

  /**
   * Resolves class names in imports, fact patterns, and globals to Java PsiClass. Handles
   * decompiled class navigation for JAR-only classes.
   *
   * <p>Requirements: 10.1, 10.2, 10.5
   */
  private static class DroolsClassReferenceProvider extends PsiReferenceProvider {
    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement element, @NotNull ProcessingContext context) {
      String text = element.getText();
      if (text == null || text.isEmpty() || text.equals(IDEA_RULES)) {
        return PsiReference.EMPTY_ARRAY;
      }

      // For import paths, resolve the full qualified name
      DroolsImportPath importPath = PsiTreeUtil.getParentOfType(element, DroolsImportPath.class);
      if (importPath != null) {
        String fullPath = importPath.getText();
        if (fullPath != null && !fullPath.isEmpty() && !fullPath.endsWith("*")) {
          return new PsiReference[] {new DroolsClassReference(element, fullPath)};
        }
        return PsiReference.EMPTY_ARRAY;
      }

      // For class names in fact patterns, globals, type names
      DroolsClassName className =
          PsiTreeUtil.getParentOfType(element, DroolsClassName.class, false);
      if (className != null) {
        String classText = className.getText();
        if (classText != null && !classText.isEmpty()) {
          return new PsiReference[] {new DroolsClassReference(element, classText)};
        }
      }

      return PsiReference.EMPTY_ARRAY;
    }
  }

  /**
   * Resolves method calls after dot to PsiMethod.
   *
   * <p>Requirement: 10.3
   */
  private static class DroolsMethodReferenceProvider extends PsiReferenceProvider {
    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement element, @NotNull ProcessingContext context) {
      String text = element.getText();
      if (text == null || text.isEmpty() || text.equals(IDEA_RULES)) {
        return PsiReference.EMPTY_ARRAY;
      }

      // Find the expression before the dot to determine the type
      PsiElement dot = findPrecedingDot(element);
      if (dot == null) {
        return PsiReference.EMPTY_ARRAY;
      }

      PsiElement beforeDot = findExpressionBeforeDot(dot);
      if (beforeDot == null) {
        return PsiReference.EMPTY_ARRAY;
      }

      String precedingText = beforeDot.getText();
      if (precedingText == null || precedingText.isEmpty()) {
        return PsiReference.EMPTY_ARRAY;
      }

      // Strip parentheses from method calls like getText()
      String methodName = text;
      if (methodName.contains("(")) {
        methodName = methodName.substring(0, methodName.indexOf('('));
      }

      if (methodName.isEmpty()) {
        return PsiReference.EMPTY_ARRAY;
      }

      return new PsiReference[] {new DroolsMethodReference(element, precedingText, methodName)};
    }

    private PsiElement findPrecedingDot(PsiElement element) {
      PsiElement prev = element.getPrevSibling();
      while (prev != null) {
        if (prev.getText().equals(".")) return prev;
        if (prev instanceof PsiWhiteSpace) {
          prev = prev.getPrevSibling();
          continue;
        }
        break;
      }
      // Try parent's previous sibling
      PsiElement parent = element.getParent();
      if (parent != null) {
        prev = parent.getPrevSibling();
        while (prev != null) {
          if (prev.getText().equals(".")) return prev;
          if (prev instanceof PsiWhiteSpace) {
            prev = prev.getPrevSibling();
            continue;
          }
          break;
        }
      }
      return null;
    }

    private PsiElement findExpressionBeforeDot(PsiElement dot) {
      PsiElement prev = dot.getPrevSibling();
      while (prev instanceof PsiWhiteSpace) {
        prev = prev.getPrevSibling();
      }
      return prev;
    }
  }

  /**
   * Resolves $varName references to their declaration within the same rule.
   *
   * <p>Requirement: 10.4
   */
  private static class DroolsBindingReferenceProvider extends PsiReferenceProvider {
    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement element, @NotNull ProcessingContext context) {
      String text = element.getText();
      if (text == null || !text.startsWith("$") || text.length() <= 1) {
        return PsiReference.EMPTY_ARRAY;
      }

      if (text.equals(IDEA_RULES)) {
        return PsiReference.EMPTY_ARRAY;
      }

      // Don't provide references for binding variable declarations themselves
      if (PsiTreeUtil.getParentOfType(element, DroolsBindingVariable.class) != null) {
        return PsiReference.EMPTY_ARRAY;
      }

      // Must be inside a rule block
      DroolsRuleBlock ruleBlock = PsiTreeUtil.getParentOfType(element, DroolsRuleBlock.class);
      if (ruleBlock == null) {
        return PsiReference.EMPTY_ARRAY;
      }

      return new PsiReference[] {new DroolsBindingReference(element, text, ruleBlock)};
    }
  }

  // --- PsiReference Implementations ---

  /**
   * Reference that resolves a class name to a Java PsiClass. Supports navigation to source or
   * decompiled class for JAR-only classes.
   */
  private static class DroolsClassReference extends PsiReferenceBase<PsiElement> {
    private final String className;

    DroolsClassReference(@NotNull PsiElement element, @NotNull String className) {
      super(element, calculateTextRange(element, className));
      this.className = className;
    }

    @Override
    @Nullable
    public PsiElement resolve() {
      Project project = getElement().getProject();
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);

      // Try resolving as fully qualified name first
      PsiClass resolved = cache.resolveClass(className, getElement());
      if (resolved != null) {
        return getNavigableElement(resolved);
      }

      // Try resolving using imports from the file
      PsiFile file = getElement().getContainingFile();
      if (file instanceof DroolsPsiFile droolsFile) {
        for (DroolsImportStatement importStmt : droolsFile.getImports()) {
          DroolsImportPath importPath = importStmt.getImportPath();
          if (importPath != null) {
            String path = importPath.getText();
            if (path != null) {
              if (path.endsWith("." + className) || path.equals(className)) {
                resolved = cache.resolveClass(path, getElement());
                if (resolved != null) {
                  return getNavigableElement(resolved);
                }
              }
              // Handle wildcard imports
              if (path.endsWith(".*")) {
                String packageName = path.substring(0, path.length() - 2);
                resolved = cache.resolveClass(packageName + "." + className, getElement());
                if (resolved != null) {
                  return getNavigableElement(resolved);
                }
              }
            }
          }
        }
      }

      // Try java.lang
      resolved = cache.resolveClass("java.lang." + className, getElement());
      if (resolved != null) {
        return getNavigableElement(resolved);
      }

      return null;
    }

    /**
     * Returns the navigable element for a PsiClass. For classes from JARs (decompiled), returns the
     * class itself which IntelliJ will display in the decompiled class view.
     */
    @NotNull
    private PsiElement getNavigableElement(@NotNull PsiClass psiClass) {
      // IntelliJ handles decompiled class navigation automatically.
      // PsiClass from a JAR will navigate to the decompiled view.
      return psiClass.getNavigationElement();
      //  PsiElement navigationElement = psiClass.getNavigationElement();-
      //      return Objects.isNull(navigationElement) ? psiClass : navigationElement;-
    }

    @Override
    public Object @NotNull [] getVariants() {
      return EMPTY_ARRAY;
    }

    private static TextRange calculateTextRange(PsiElement element, String className) {
      String elementText = element.getText();
      if (elementText != null && elementText.contains(className)) {
        int start = elementText.indexOf(className);
        return new TextRange(start, start + className.length());
      }
      return new TextRange(0, element.getTextLength());
    }
  }

  /** Reference that resolves a method name to a PsiMethod on the resolved type. */
  private static class DroolsMethodReference extends PsiReferenceBase<PsiElement> {
    private final String precedingExpression;
    private final String methodName;

    DroolsMethodReference(
        @NotNull PsiElement element,
        @NotNull String precedingExpression,
        @NotNull String methodName) {
      super(element, new TextRange(0, element.getTextLength()));
      this.precedingExpression = precedingExpression;
      this.methodName = methodName;
    }

    @Override
    @Nullable
    public PsiElement resolve() {
      Project project = getElement().getProject();
      PsiClass resolvedClass = resolveType(precedingExpression, getElement(), project);
      if (resolvedClass == null) {
        return null;
      }

      // Find the method by name
      for (PsiMethod method : resolvedClass.getAllMethods()) {
        if (method.getName().equals(methodName)) {
          //          PsiElement nav = method.getNavigationElement();-
          //          return nav != null ? nav : method;-
          return method.getNavigationElement();
        }
      }

      // Also check fields (for field access after dot)
      for (PsiField field : resolvedClass.getAllFields()) {
        if (field.getName().equals(methodName)) {
          //          PsiElement nav =  field.getNavigationElement();-
          //          return nav != null ? nav : field;-
          return field.getNavigationElement();
        }
      }

      return null;
    }

    @Override
    public Object @NotNull [] getVariants() {
      return EMPTY_ARRAY;
    }

    private PsiClass resolveType(String text, PsiElement context, Project project) {
      if (text == null || text.isEmpty()) return null;

      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);

      // Try to resolve as a binding variable first
      if (text.startsWith("$")) {
        DroolsRuleBlock ruleBlock = PsiTreeUtil.getParentOfType(context, DroolsRuleBlock.class);
        if (ruleBlock != null) {
          Map<String, DroolsBindingVariable> bindings = cache.getBindingsForRule(ruleBlock);
          DroolsBindingVariable binding = bindings.get(text);
          if (binding != null) {
            PsiElement parent = binding.getParent();
            if (parent instanceof DroolsBindingPattern bindingPattern) {
              DroolsFactPattern factPattern = bindingPattern.getFactPattern();
              String typeName = factPattern.getClassName().getText();
              return resolveClassName(typeName, context, project);
            }
          }
        }
      }

      // Try to resolve as a class name directly
      return resolveClassName(text, context, project);
    }

    private PsiClass resolveClassName(String className, PsiElement context, Project project) {
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);

      // Try fully qualified name first
      PsiClass resolved = cache.resolveClass(className, context);
      if (resolved != null) return resolved;

      // Try with imports from the file
      PsiFile file = context.getContainingFile();
      if (file instanceof DroolsPsiFile droolsFile) {
        for (DroolsImportStatement importStmt : droolsFile.getImports()) {
          DroolsImportPath importPath = importStmt.getImportPath();
          if (importPath != null) {
            String path = importPath.getText();
            if (path != null) {
              if (path.endsWith("." + className) || path.equals(className)) {
                resolved = cache.resolveClass(path, context);
                if (resolved != null) return resolved;
              }
              if (path.endsWith(".*")) {
                String packageName = path.substring(0, path.length() - 2);
                resolved = cache.resolveClass(packageName + "." + className, context);
                if (resolved != null) return resolved;
              }
            }
          }
        }
      }

      // Try java.lang
      resolved = cache.resolveClass("java.lang." + className, context);
      return resolved;
    }
  }

  /**
   * Reference that resolves a $varName to its binding variable declaration within the same rule
   * block.
   */
  private static class DroolsBindingReference extends PsiReferenceBase<PsiElement> {
    private final String variableName;
    private final DroolsRuleBlock ruleBlock;

    DroolsBindingReference(
        @NotNull PsiElement element,
        @NotNull String variableName,
        @NotNull DroolsRuleBlock ruleBlock) {
      super(element, new TextRange(0, element.getTextLength()));
      this.variableName = variableName;
      this.ruleBlock = ruleBlock;
    }

    @Override
    @Nullable
    public PsiElement resolve() {
      Project project = getElement().getProject();
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);
      Map<String, DroolsBindingVariable> bindings = cache.getBindingsForRule(ruleBlock);

      DroolsBindingVariable declaration = bindings.get(variableName);
      if (declaration != null && declaration.isValid()) {
        return declaration;
      }

      return null;
    }

    @Override
    public Object @NotNull [] getVariants() {
      // Could return all available binding variables for completion,
      // but completion is handled by DroolsCompletionContributor
      return EMPTY_ARRAY;
    }
  }
}
