package com.plugin.drool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.PsiType;
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
import com.plugin.drool.psi.DroolsTypes;
import com.plugin.drool.util.DotAccessExpressionResolver;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.plugin.drool.util.DroolsConstants.IDEA_RULES;
import static com.plugin.drool.util.DroolsConstants.JAVA_DOT_LANG_DOT;

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

  /** Matches IDENTIFIER leaves in imports, fact patterns, globals, and type names. */
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

      // Only register references on IDENTIFIER leaf elements for class names
      if (element.getNode() == null
          || element.getNode().getElementType() != DroolsTypes.IDENTIFIER) {
        return PsiReference.EMPTY_ARRAY;
      }

      // For class names in fact patterns, globals, type names (outside imports)
      DroolsClassName className =
          PsiTreeUtil.getParentOfType(element, DroolsClassName.class, false);
      if (className != null) {
        // Only register a reference on the LAST IDENTIFIER leaf of the class name
        // (the simple class name). For FQN like com.foo.Bar, only "Bar" gets a reference.
        // For a single-segment class name like "AlloyDbBackupInfo", the leaf IS the last.
        if (!isLastIdentifierLeaf(element, className)) {
          return PsiReference.EMPTY_ARRAY;
        }
        // Use the simple class name (the leaf text) as the resolution input
        String simpleClassName = element.getText();
        if (simpleClassName != null && !simpleClassName.isEmpty()) {
          return new PsiReference[] {new DroolsClassReference(element, simpleClassName)};
        }
      }

      return PsiReference.EMPTY_ARRAY;
    }

    /**
     * Determines whether the given element is the last IDENTIFIER leaf within a DroolsClassName.
     * For a class name like "com.foo.Bar", the IDENTIFIER leaves are "com", "foo", "Bar" — only
     * "Bar" (the last one) should get a reference.
     */
    private boolean isLastIdentifierLeaf(
        @NotNull PsiElement element, @NotNull DroolsClassName className) {
      PsiElement lastIdentifier = null;
      for (PsiElement child = className.getFirstChild();
          child != null;
          child = child.getNextSibling()) {
        if (child.getNode() != null && child.getNode().getElementType() == DroolsTypes.IDENTIFIER) {
          lastIdentifier = child;
        }
      }
      return element.equals(lastIdentifier);
    }
  }

  /**
   * Resolves method calls after dot to PsiMethod. Uses the shared {@link
   * DotAccessExpressionResolver} to correctly reconstruct $-prefixed expressions (including chained
   * member access) before the dot.
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

      // Find the dot element preceding this method/field name
      PsiElement dot = findPrecedingDot(element);
      if (dot == null) {
        return PsiReference.EMPTY_ARRAY;
      }

      // Use the shared helper to reconstruct the full expression before the dot.
      // This correctly handles $-prefixed bindings (DOLLAR + IDENTIFIER) and chained access.
      String precedingText = DotAccessExpressionResolver.reconstructExpressionBeforeDot(dot);
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
      super(element, calculateTextRange(element));
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
      PsiElement navigationElement = psiClass.getNavigationElement();
      return navigationElement != null ? navigationElement : psiClass;
    }

    @Override
    public Object @NotNull [] getVariants() {
      return EMPTY_ARRAY;
    }

    private static TextRange calculateTextRange(PsiElement element) {
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
          PsiElement nav = method.getNavigationElement();
          return nav != null ? nav : method;
        }
      }

      // Also check fields (for field access after dot)
      for (PsiField field : resolvedClass.getAllFields()) {
        if (field.getName().equals(methodName)) {
          PsiElement nav = field.getNavigationElement();
          return nav != null ? nav : field;
        }
      }

      return null;
    }

    @Override
    public Object @NotNull [] getVariants() {
      return EMPTY_ARRAY;
    }

    /**
     * Resolves the type of the preceding expression text. Supports: - Simple $-binding: "$x" ->
     * looks up binding, resolves to bound type - Chained access: "$x.member" -> resolves $x to its
     * type, then resolves "member" as a property/field/getter return type on that class -
     * Multi-level chain: "$x.a.b" -> resolves iteratively - Class name: "Integer" -> resolves to
     * the class directly (for static access) - Non-$ identifier: "localVar" -> tries class
     * resolution
     */
    private PsiClass resolveType(String text, PsiElement context, Project project) {
      if (text == null || text.isEmpty()) return null;

      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);

      // Handle $-prefixed expressions (bindings and chained access)
      if (text.startsWith("$")) {
        DroolsRuleBlock ruleBlock = PsiTreeUtil.getParentOfType(context, DroolsRuleBlock.class);
        if (ruleBlock != null) {
          return resolveBindingExpression(text, ruleBlock, context, project);
        }
        return null;
      }

      // Try to resolve as a class name directly (for static access like Integer.)
      return resolveClassName(text, context, project);
    }

    /**
     * Resolves a $-prefixed expression, handling both simple bindings and chained member access.
     * For "$x", resolves the binding to its bound type. For "$x.member", resolves $x first, then
     * resolves "member" as a property/field/getter return type on the resolved class. For "$x.a.b",
     * resolves iteratively through each segment.
     */
    private PsiClass resolveBindingExpression(
        String text, DroolsRuleBlock ruleBlock, PsiElement context, Project project) {
      // Split on dots to handle chained access: "$x.member.sub" -> ["$x", "member", "sub"]
      String[] segments = text.split("\\.");
      if (segments.length == 0) return null;

      // First segment must be the $-binding variable name
      String bindingName = segments[0]; // e.g. "$x" or "$alloyDbBackup"
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);
      Map<String, DroolsBindingVariable> bindings = cache.getBindingsForRule(ruleBlock);
      DroolsBindingVariable binding = bindings.get(bindingName);
      if (binding == null) return null;

      // Resolve the binding to its bound type
      PsiClass currentClass = resolveBindingToClass(binding, context, project);
      if (currentClass == null) return null;

      // If there are chained segments (e.g. "$x.member"), resolve each one iteratively
      for (int i = 1; i < segments.length; i++) {
        String memberName = segments[i];
        currentClass = resolveMemberType(currentClass, memberName, context, project);
        if (currentClass == null) return null;
      }

      return currentClass;
    }

    /** Resolves a binding variable to its bound PsiClass by looking at the fact pattern. */
    private PsiClass resolveBindingToClass(
        DroolsBindingVariable binding, PsiElement context, Project project) {
      PsiElement parent = binding.getParent();
      if (parent instanceof DroolsBindingPattern bindingPattern) {
        DroolsFactPattern factPattern = bindingPattern.getFactPattern();
        if (factPattern != null) {
          String className = factPattern.getClassName().getText();
          return resolveClassName(className, context, project);
        }
      }
      return null;
    }

    /**
     * Resolves a member name (property/field/getter) on a given class to the PsiClass of its return
     * type. Used for chained access like "$x.address" where we need to find the type of "address"
     * on the class that $x is bound to.
     */
    private PsiClass resolveMemberType(
        PsiClass ownerClass, String memberName, PsiElement context, Project project) {
      // Try getter methods: getXxx() or isXxx()
      String capitalizedName =
          memberName.isEmpty()
              ? memberName
              : Character.toUpperCase(memberName.charAt(0)) + memberName.substring(1);
      String getterName = "get" + capitalizedName;
      String booleanGetterName = "is" + capitalizedName;

      for (PsiMethod method : ownerClass.getAllMethods()) {
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (method.hasModifierProperty(PsiModifier.STATIC)) continue;
        if (method.getParameterList().getParametersCount() != 0) continue;

        String name = method.getName();
        if (name.equals(getterName) || name.equals(booleanGetterName)) {
          PsiType returnType = method.getReturnType();
          if (returnType instanceof PsiClassType classType) {
            PsiClass resolved = classType.resolve();
            if (resolved != null) return resolved;
          }
        }
      }

      // Try direct method match (e.g. member name IS the method name)
      for (PsiMethod method : ownerClass.getAllMethods()) {
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (method.hasModifierProperty(PsiModifier.STATIC)) continue;
        if (method.getParameterList().getParametersCount() != 0) continue;
        if (method.getName().equals(memberName)) {
          PsiType returnType = method.getReturnType();
          if (returnType instanceof PsiClassType classType) {
            PsiClass resolved = classType.resolve();
            if (resolved != null) return resolved;
          }
        }
      }

      // Try public fields
      for (PsiField field : ownerClass.getAllFields()) {
        if (!field.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (field.hasModifierProperty(PsiModifier.STATIC)) continue;
        if (field.getName().equals(memberName)) {
          PsiType fieldType = field.getType();
          if (fieldType instanceof PsiClassType classType) {
            PsiClass resolved = classType.resolve();
            if (resolved != null) return resolved;
          }
        }
      }

      return null;
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
      resolved = cache.resolveClass(JAVA_DOT_LANG_DOT + className, context);
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
