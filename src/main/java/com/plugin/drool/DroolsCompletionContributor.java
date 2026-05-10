package com.plugin.drool;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.plugin.drool.psi.DroolsBindingPattern;
import com.plugin.drool.psi.DroolsBindingVariable;
import com.plugin.drool.psi.DroolsClassName;
import com.plugin.drool.psi.DroolsConstraintList;
import com.plugin.drool.psi.DroolsDeclareBlock;
import com.plugin.drool.psi.DroolsDeclareField;
import com.plugin.drool.psi.DroolsFactPattern;
import com.plugin.drool.psi.DroolsFunctionDef;
import com.plugin.drool.psi.DroolsGlobalDecl;
import com.plugin.drool.psi.DroolsImportPath;
import com.plugin.drool.psi.DroolsImportStatement;
import com.plugin.drool.psi.DroolsPackageDecl;
import com.plugin.drool.psi.DroolsQueryDef;
import com.plugin.drool.psi.DroolsRuleBlock;
import com.plugin.drool.psi.DroolsThenClause;
import com.plugin.drool.psi.DroolsTypeName;
import com.plugin.drool.psi.DroolsWhenClause;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

import static com.plugin.drool.util.DroolsConstants.JAVA_DOT_LANG_DOT;
import static com.plugin.drool.util.DroolsConstants.KEYWORDS;
import static com.plugin.drool.util.DroolsConstants.OBJECT;

/**
 * Context-aware completion contributor for Drools Rule Language files. Uses PSI-based element
 * patterns to determine cursor context and provide appropriate suggestions for keywords, classes,
 * methods, fields, and data types.
 *
 * <p>Satisfies Requirements: 3.1-3.6, 4.1-4.6, 5.1-5.6, 6.1-6.4
 */
public class DroolsCompletionContributor extends CompletionContributor {

  public DroolsCompletionContributor() {
    // Top-level keywords (package, import, global, function, declare, rule, query)
    extend(CompletionType.BASIC, topLevelPattern(), new TopLevelKeywordProvider());
    // Rule attribute keywords (salience, no-loop, agenda-group, etc.)
    extend(CompletionType.BASIC, ruleAttributePattern(), new RuleAttributeProvider());
    // When-clause keywords (and, or, not, exists, forall, from, collect, accumulate, eval)
    extend(CompletionType.BASIC, whenClausePattern(), new WhenClauseProvider());
    // Then-clause functions (insert, insertLogical, update, modify, retract, delete, drools.halt,
    // kcontext)
    extend(CompletionType.BASIC, thenClausePattern(), new ThenClauseProvider());
    // Class name completion at type positions (fact patterns, imports, globals)
    extend(CompletionType.BASIC, classNamePattern(), new ClassNameProvider());
    // Method/field completion after dot
    extend(CompletionType.BASIC, dotAccessPattern(), new MethodFieldProvider());
    // Data type completion at type positions
    extend(CompletionType.BASIC, typePositionPattern(), new DataTypeProvider());
  }

  // --- Element Pattern Definitions ---

  /**
   * Matches cursor positions at the file top level (not inside any rule, declare, query, or
   * function).
   */
  private static ElementPattern<PsiElement> topLevelPattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .andNot(PlatformPatterns.psiElement().inside(DroolsRuleBlock.class))
        .andNot(PlatformPatterns.psiElement().inside(DroolsDeclareBlock.class))
        .andNot(PlatformPatterns.psiElement().inside(DroolsQueryDef.class))
        .andNot(PlatformPatterns.psiElement().inside(DroolsFunctionDef.class));
  }

  /**
   * Matches cursor positions inside a rule block but before the when clause (i.e., in the rule
   * attributes area).
   */
  private static ElementPattern<PsiElement> ruleAttributePattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .inside(DroolsRuleBlock.class)
        .andNot(PlatformPatterns.psiElement().inside(DroolsWhenClause.class))
        .andNot(PlatformPatterns.psiElement().inside(DroolsThenClause.class));
  }

  /** Matches cursor positions inside a when clause. */
  private static ElementPattern<PsiElement> whenClausePattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .inside(DroolsWhenClause.class);
  }

  /** Matches cursor positions inside a then clause. */
  private static ElementPattern<PsiElement> thenClausePattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .inside(DroolsThenClause.class);
  }

  /** Matches cursor positions at class name / type positions (fact patterns, imports, globals). */
  private static ElementPattern<PsiElement> classNamePattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .andOr(
            PlatformPatterns.psiElement().inside(DroolsClassName.class),
            PlatformPatterns.psiElement().inside(DroolsImportPath.class),
            PlatformPatterns.psiElement()
                .inside(DroolsFactPattern.class)
                .andNot(PlatformPatterns.psiElement().inside(DroolsConstraintList.class)));
  }

  /** Matches cursor positions after a dot (for method/field access). */
  private static ElementPattern<PsiElement> dotAccessPattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .afterLeaf(PlatformPatterns.psiElement().withText("."));
  }

  /** Matches cursor positions at type positions in declare fields, function params, globals. */
  private static ElementPattern<PsiElement> typePositionPattern() {
    return PlatformPatterns.psiElement()
        .withLanguage(DroolsLanguage.INSTANCE)
        .andOr(
            PlatformPatterns.psiElement().inside(DroolsTypeName.class),
            PlatformPatterns.psiElement().inside(DroolsDeclareField.class),
            PlatformPatterns.psiElement().inside(DroolsGlobalDecl.class));
  }

  // --- Completion Providers ---

  /**
   * Suggests top-level keywords: package, import, global, function, declare, rule, query.
   * Requirement 3.1
   */
  private static class TopLevelKeywordProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      for (String keyword : KEYWORDS) {
        result.addElement(createKeywordLookup(keyword));
      }
    }
  }

  /**
   * Suggests rule attribute keywords: salience, no-loop, agenda-group, ruleflow-group, auto-focus,
   * lock-on-active, date-effective, date-expires, enabled, duration, timer, dialect. Requirement
   * 3.2
   */
  private static class RuleAttributeProvider extends CompletionProvider<CompletionParameters> {
    private static final String[] RULE_ATTRIBUTES = {
      "salience",
      "no-loop",
      "agenda-group",
      "ruleflow-group",
      "auto-focus",
      "lock-on-active",
      "date-effective",
      "date-expires",
      "enabled",
      "duration",
      "timer",
      "dialect",
      "extends"
    };

    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      for (String attr : RULE_ATTRIBUTES) {
        result.addElement(createKeywordLookup(attr, "attribute"));
      }
      // Also suggest 'when' keyword to start the condition section
      result.addElement(createKeywordLookup("when"));
    }
  }

  /**
   * Suggests when-clause keywords: and, or, not, exists, forall, from, collect, accumulate, eval.
   * Requirement 3.3
   */
  private static class WhenClauseProvider extends CompletionProvider<CompletionParameters> {
    private static final String[] WHEN_KEYWORDS = {
      "and", "or", "not", "exists", "forall", "from", "collect", "accumulate", "eval"
    };

    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      for (String keyword : WHEN_KEYWORDS) {
        result.addElement(createKeywordLookup(keyword));
      }
    }
  }

  /**
   * Suggests then-clause action functions: insert, insertLogical, update, modify, retract, delete,
   * drools.halt, drools.getRule, kcontext. Requirement 3.4
   */
  private static class ThenClauseProvider extends CompletionProvider<CompletionParameters> {
    private static final String[][] THEN_FUNCTIONS = {
      {"insert", "Insert fact into working memory"},
      {"insertLogical", "Insert logical fact into working memory"},
      {"update", "Update fact in working memory"},
      {"modify", "Modify fact with block"},
      {"retract", "Retract fact from working memory"},
      {"delete", "Delete fact from working memory"},
      {"drools.halt", "Halt rule engine execution"},
      {"drools.getRule", "Get current rule reference"},
      {"kcontext", "Knowledge context"}
    };

    private static final java.util.regex.Pattern LOCAL_VAR_PATTERN =
        java.util.regex.Pattern.compile(
            "^\\s*([a-zA-Z][\\w<>\\[\\],\\s.]*)\\s+(\\w+)\\s*=", java.util.regex.Pattern.MULTILINE);

    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      for (String[] func : THEN_FUNCTIONS) {
        String name = func[0];
        //        String description = func[1];-
        if (name.contains(".")) {
          // For drools.halt, drools.getRule — insert as-is
          result.addElement(
              LookupElementBuilder.create(name)
                  .withTypeText("function")
                  .withTailText("()", true)
                  .withBoldness(true)
                  .withIcon(AllIcons.Nodes.Function)
                  .withInsertHandler(
                      (ctx, item) -> {
                        Document doc = ctx.getDocument();
                        int offset = ctx.getTailOffset();
                        doc.insertString(offset, "()");
                        ctx.getEditor().getCaretModel().moveToOffset(offset + 1);
                      }));
        } else if (name.equals("kcontext")) {
          result.addElement(
              LookupElementBuilder.create(name)
                  .withTypeText("KieContext")
                  .withBoldness(true)
                  .withIcon(AllIcons.Nodes.Variable));
        } else {
          result.addElement(
              LookupElementBuilder.create(name)
                  .withTypeText("function")
                  .withTailText("()", true)
                  .withBoldness(true)
                  .withIcon(AllIcons.Nodes.Function)
                  .withInsertHandler(
                      (ctx, item) -> {
                        Document doc = ctx.getDocument();
                        int offset = ctx.getTailOffset();
                        doc.insertString(offset, "()");
                        ctx.getEditor().getCaretModel().moveToOffset(offset + 1);
                      }));
        }
      }

      // Also suggest local variables declared earlier in the then-clause
      DroolsThenClause thenClause =
          PsiTreeUtil.getParentOfType(parameters.getPosition(), DroolsThenClause.class);
      if (thenClause != null) {
        addLocalVariables(thenClause, parameters.getPosition(), result);
      }

      // Also suggest binding variables from the when-clause
      DroolsRuleBlock ruleBlock =
          PsiTreeUtil.getParentOfType(parameters.getPosition(), DroolsRuleBlock.class);
      if (ruleBlock != null) {
        addBindingVariables(ruleBlock, result);
      }
    }

    private void addLocalVariables(
        @NotNull DroolsThenClause thenClause,
        @NotNull PsiElement cursorPosition,
        @NotNull CompletionResultSet result) {
      String thenText = thenClause.getText();
      int cursorOffsetInThen = cursorPosition.getTextOffset() - thenClause.getTextOffset();
      if (cursorOffsetInThen <= 0) return;

      // Only scan text before the cursor position
      String textBeforeCursor =
          thenText.substring(0, Math.min(cursorOffsetInThen, thenText.length()));

      java.util.regex.Matcher matcher = LOCAL_VAR_PATTERN.matcher(textBeforeCursor);
      Set<String> addedVars = new HashSet<>();
      while (matcher.find()) {
        String typeName = matcher.group(1).trim();
        String varName = matcher.group(2);
        if (varName != null && !varName.isEmpty() && addedVars.add(varName)) {
          result.addElement(
              PrioritizedLookupElement.withPriority(
                  LookupElementBuilder.create(varName)
                      .withTypeText(typeName, true)
                      .withIcon(AllIcons.Nodes.Variable),
                  90));
        }
      }
    }

    private void addBindingVariables(
        @NotNull DroolsRuleBlock ruleBlock, @NotNull CompletionResultSet result) {
      Project project = ruleBlock.getProject();
      DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);
      Map<String, DroolsBindingVariable> bindings = cache.getBindingsForRule(ruleBlock);

      for (Map.Entry<String, DroolsBindingVariable> entry : bindings.entrySet()) {
        String varName = entry.getKey();
        DroolsBindingVariable bindingVar = entry.getValue();

        String typeName = OBJECT;
        PsiElement parent = bindingVar.getParent();
        if (parent instanceof DroolsBindingPattern bindingPattern) {
          DroolsFactPattern factPattern =
              PsiTreeUtil.getChildOfType(bindingPattern, DroolsFactPattern.class);
          if (factPattern != null) {
            typeName = factPattern.getClassName().getText();
          }
        }

        result.addElement(
            PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(varName)
                    .withTypeText(typeName, true)
                    .withIcon(AllIcons.Nodes.Variable),
                95));
      }
    }
  }

  /**
   * Suggests class names from the project classpath at type positions. Uses PsiShortNamesCache and
   * JavaPsiFacade to resolve classes. Prioritizes already-imported classes. Implements auto-import:
   * when a class is selected in when-clause, inserts import if not present. Requirements 4.1, 4.2,
   * 4.3, 4.4, 4.5, 4.6
   */
  private static class ClassNameProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      PsiElement position = parameters.getPosition();
      Project project = position.getProject();
      PsiFile file = parameters.getOriginalFile();

      // Collect already-imported class names for prioritization
      Set<String> importedClassNames = getImportedClassNames(file);

      // Get search scope
      GlobalSearchScope scope = getSearchScope(position, project);

      // Use PsiShortNamesCache to get all class names
      PsiShortNamesCache namesCache = PsiShortNamesCache.getInstance(project);
      String[] allClassNames = namesCache.getAllClassNames();

      String prefix = result.getPrefixMatcher().getPrefix();

      for (String className : allClassNames) {
        // Skip if doesn't match prefix at all (performance optimization)
        if (!prefix.isEmpty()
            && !className.toLowerCase().startsWith(prefix.toLowerCase())
            && !matchesCamelCase(className, prefix)) {
          continue;
        }

        PsiClass[] classes = namesCache.getClassesByName(className, scope);
        for (PsiClass psiClass : classes) {
          if (psiClass == null || !psiClass.isValid()) continue;
          String qualifiedName = psiClass.getQualifiedName();
          if (qualifiedName == null) continue;

          // Skip anonymous and local classes
          if (psiClass.getName() == null || psiClass.getName().isEmpty()) continue;

          boolean isImported =
              importedClassNames.contains(qualifiedName)
                  || importedClassNames.contains(className)
                  || isJavaLangClass(qualifiedName);

          Icon icon = getClassIcon(psiClass);

          LookupElementBuilder builder =
              LookupElementBuilder.create(psiClass, className)
                  .withTypeText(getPackageName(qualifiedName), true)
                  .withIcon(icon)
                  .withInsertHandler(new AutoImportInsertHandler(qualifiedName, isImported));

          // Prioritize imported classes
          if (isImported) {
            result.addElement(PrioritizedLookupElement.withPriority(builder, 100));
          } else {
            result.addElement(PrioritizedLookupElement.withPriority(builder, 50));
          }
        }
      }
    }

    private boolean matchesCamelCase(String className, String prefix) {
      if (prefix.isEmpty()) return true;
      StringBuilder camelParts = new StringBuilder();
      for (char c : className.toCharArray()) {
        if (Character.isUpperCase(c)) {
          camelParts.append(c);
        }
      }
      return camelParts.toString().toLowerCase().startsWith(prefix.toLowerCase());
    }

    private boolean isJavaLangClass(String qualifiedName) {
      return qualifiedName.startsWith(JAVA_DOT_LANG_DOT)
          && !qualifiedName.substring(10).contains(".");
    }

    private String getPackageName(String qualifiedName) {
      int lastDot = qualifiedName.lastIndexOf('.');
      return lastDot > 0 ? qualifiedName.substring(0, lastDot) : "";
    }

    private Icon getClassIcon(PsiClass psiClass) {
      if (psiClass.isInterface()) return AllIcons.Nodes.Interface;
      if (psiClass.isEnum()) return AllIcons.Nodes.Enum;
      if (psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) return AllIcons.Nodes.AbstractClass;
      return AllIcons.Nodes.Class;
    }
  }

  /**
   * Resolves the type of the preceding expression and suggests methods/fields. Uses
   * PsiClass.getAllMethods() and PsiClass.getAllFields(). Requirements 5.1, 5.2, 5.3, 5.4, 5.5, 5.6
   */
  private static class MethodFieldProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      PsiElement position = parameters.getPosition();
      Project project = position.getProject();

      // Find the text before the dot to determine the type
      PsiElement dotElement = findPrecedingDot(position);
      if (dotElement == null) return;

      PsiElement beforeDot = findExpressionBeforeDot(dotElement);
      if (beforeDot == null) return;

      String precedingText = beforeDot.getText();
      PsiClass resolvedClass = resolveType(precedingText, position, project);
      if (resolvedClass == null) return;

      boolean inWhenClause = PsiTreeUtil.getParentOfType(position, DroolsWhenClause.class) != null;
      boolean inThenClause = PsiTreeUtil.getParentOfType(position, DroolsThenClause.class) != null;
      boolean inConstraint =
          PsiTreeUtil.getParentOfType(position, DroolsConstraintList.class) != null;
      boolean isStaticContext = isStaticContext(precedingText, position, project);

      if (isStaticContext) {
        // Static context: suggest static methods and fields
        addStaticMembers(resolvedClass, result);
      } else if (inConstraint) {
        // Inside fact pattern constraints: suggest bean properties
        addBeanProperties(resolvedClass, result);
      } else if (inWhenClause) {
        // In when-clause (after binding variable): suggest getters and fields
        addGettersAndFields(resolvedClass, result);
      } else if (inThenClause) {
        // In then-clause: suggest all public methods
        addAllPublicMembers(resolvedClass, result);
      } else {
        // Default: suggest all public members
        addAllPublicMembers(resolvedClass, result);
      }
    }

    private PsiElement findPrecedingDot(PsiElement position) {
      PsiElement prev = position.getPrevSibling();
      while (prev != null) {
        if (prev.getText().equals(".")) return prev;
        if (prev instanceof com.intellij.psi.PsiWhiteSpace) {
          prev = prev.getPrevSibling();
          continue;
        }
        break;
      }
      // Try parent's previous sibling
      PsiElement parent = position.getParent();
      if (parent != null) {
        prev = parent.getPrevSibling();
        while (prev != null) {
          if (prev.getText().equals(".")) return prev;
          if (prev instanceof com.intellij.psi.PsiWhiteSpace) {
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

    private PsiClass resolveType(String text, PsiElement context, Project project) {
      if (text == null || text.isEmpty()) return null;

      // Strip $ prefix for binding variables
      //      String lookupName = text.startsWith("$") ? text : text;-

      // Try to resolve as a binding variable first
      DroolsRuleBlock ruleBlock = PsiTreeUtil.getParentOfType(context, DroolsRuleBlock.class);
      if (ruleBlock != null && text.startsWith("$")) {
        DroolsResolutionCache cache = DroolsResolutionCache.getInstance(project);
        Map<String, DroolsBindingVariable> bindings = cache.getBindingsForRule(ruleBlock);
        DroolsBindingVariable binding = bindings.get(text);
        if (binding != null) {
          // Get the type from the binding's fact pattern
          PsiElement parent = binding.getParent();
          if (parent instanceof DroolsBindingPattern bindingPattern) {
            DroolsFactPattern factPattern =
                PsiTreeUtil.getChildOfType(bindingPattern, DroolsFactPattern.class);
            if (factPattern != null) {
              String className = factPattern.getClassName().getText();
              return resolveClassName(className, context, project);
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
            if (path.endsWith("." + className) || path.equals(className)) {
              resolved = cache.resolveClass(path, context);
              if (resolved != null) return resolved;
            }
          }
        }
      }

      // Try java.lang
      resolved = cache.resolveClass(JAVA_DOT_LANG_DOT + className, context);
      return resolved;
    }

    private boolean isStaticContext(String text, PsiElement context, Project project) {
      if (text.startsWith("$")) return false;
      // If the text resolves to a class directly and is capitalized, it's likely static
      if (!text.isEmpty() && Character.isUpperCase(text.charAt(0))) {
        PsiClass resolved = resolveClassName(text, context, project);
        return resolved != null;
      }
      return false;
    }

    private void addStaticMembers(PsiClass psiClass, CompletionResultSet result) {
      for (PsiMethod method : psiClass.getAllMethods()) {
        if (method.hasModifierProperty(PsiModifier.STATIC)
            && method.hasModifierProperty(PsiModifier.PUBLIC)) {
          result.addElement(createMethodLookup(method));
        }
      }
      for (PsiField field : psiClass.getAllFields()) {
        if (field.hasModifierProperty(PsiModifier.STATIC)
            && field.hasModifierProperty(PsiModifier.PUBLIC)) {
          result.addElement(createFieldLookup(field));
        }
      }
    }

    private void addBeanProperties(PsiClass psiClass, CompletionResultSet result) {
      Set<String> addedProperties = new HashSet<>();
      for (PsiMethod method : psiClass.getAllMethods()) {
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (method.hasModifierProperty(PsiModifier.STATIC)) continue;

        String name = method.getName();
        String propertyName = null;
        if (name.startsWith("get") && name.length() > 3 && method.getParameterList().isEmpty()) {
          propertyName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
        } else if (name.startsWith("is")
            && name.length() > 2
            && method.getParameterList().isEmpty()) {
          propertyName = Character.toLowerCase(name.charAt(2)) + name.substring(3);
        }

        if (propertyName != null && addedProperties.add(propertyName)) {
          PsiType returnType = method.getReturnType();
          String typeText = returnType != null ? returnType.getPresentableText() : OBJECT;
          result.addElement(
              LookupElementBuilder.create(propertyName)
                  .withTypeText(typeText, true)
                  .withIcon(AllIcons.Nodes.Property));
        }
      }
      // Also add public fields
      for (PsiField field : psiClass.getAllFields()) {
        if (!field.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (field.hasModifierProperty(PsiModifier.STATIC)) continue;
        String fieldName = field.getName();
        if (addedProperties.add(fieldName)) {
          result.addElement(createFieldLookup(field));
        }
      }
    }

    private void addGettersAndFields(PsiClass psiClass, CompletionResultSet result) {
      for (PsiMethod method : psiClass.getAllMethods()) {
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (method.hasModifierProperty(PsiModifier.STATIC)) continue;
        String name = method.getName();
        if ((name.startsWith("get") || name.startsWith("is"))
            && method.getParameterList().isEmpty()) {
          result.addElement(createMethodLookup(method));
        }
      }
      for (PsiField field : psiClass.getAllFields()) {
        if (!field.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        if (field.hasModifierProperty(PsiModifier.STATIC)) continue;
        result.addElement(createFieldLookup(field));
      }
    }

    private void addAllPublicMembers(PsiClass psiClass, CompletionResultSet result) {
      for (PsiMethod method : psiClass.getAllMethods()) {
        if (!method.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        // Skip Object methods like wait, notify, etc.
        PsiClass containingClass = method.getContainingClass();
        if (containingClass != null
            && "java.lang.Object".equals(containingClass.getQualifiedName())) {
          String name = method.getName();
          if (name.equals("wait")
              || name.equals("notify")
              || name.equals("notifyAll")
              || name.equals("getClass")
              || name.equals("finalize")
              || name.equals("clone")) {
            continue;
          }
        }
        result.addElement(createMethodLookup(method));
      }
      for (PsiField field : psiClass.getAllFields()) {
        if (!field.hasModifierProperty(PsiModifier.PUBLIC)) continue;
        result.addElement(createFieldLookup(field));
      }
    }

    private LookupElement createMethodLookup(PsiMethod method) {
      String name = method.getName();
      PsiType returnType = method.getReturnType();
      String typeText = returnType != null ? returnType.getPresentableText() : "void";
      String params = buildParameterText(method);

      return LookupElementBuilder.create(method, name)
          .withTypeText(typeText, true)
          .withTailText("(" + params + ")", true)
          .withIcon(
              method.hasModifierProperty(PsiModifier.STATIC)
                  ? AllIcons.Nodes.Static
                  : AllIcons.Nodes.Method)
          .withInsertHandler(
              (ctx, item) -> {
                Document doc = ctx.getDocument();
                int offset = ctx.getTailOffset();
                doc.insertString(offset, "()");
                if (method.getParameterList().getParametersCount() > 0) {
                  ctx.getEditor().getCaretModel().moveToOffset(offset + 1);
                } else {
                  ctx.getEditor().getCaretModel().moveToOffset(offset + 2);
                }
              });
    }

    private LookupElement createFieldLookup(PsiField field) {
      String typeText = field.getType().getPresentableText();
      return LookupElementBuilder.create(field, field.getName())
          .withTypeText(typeText, true)
          .withIcon(
              field.hasModifierProperty(PsiModifier.STATIC)
                  ? AllIcons.Nodes.Static
                  : AllIcons.Nodes.Field);
    }

    private String buildParameterText(PsiMethod method) {
      PsiParameter[] params = method.getParameterList().getParameters();
      if (params.length == 0) return "";
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < params.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(params[i].getType().getPresentableText());
        sb.append(" ").append(params[i].getName());
      }
      return sb.toString();
    }
  }

  /**
   * Suggests primitive types, wrapper types, and project classes at type positions. Requirements
   * 6.1, 6.2, 6.3, 6.4
   */
  private static class DataTypeProvider extends CompletionProvider<CompletionParameters> {
    private static final String[] PRIMITIVE_TYPES = {
      "int", "long", "double", "float", "boolean", "char", "byte", "short", "void"
    };

    private static final String[] WRAPPER_TYPES = {
      "Integer",
      "Long",
      "Double",
      "Float",
      "Boolean",
      "Character",
      "Byte",
      "Short",
      "String",
      OBJECT,
      "Number"
    };

    private static final String[] COMMON_TYPES = {
      "List",
      "Map",
      "Set",
      "Collection",
      "ArrayList",
      "HashMap",
      "HashSet",
      "Date",
      "BigDecimal",
      "BigInteger"
    };

    @Override
    protected void addCompletions(
        @NotNull CompletionParameters parameters,
        @NotNull ProcessingContext context,
        @NotNull CompletionResultSet result) {
      // Add primitive types
      for (String type : PRIMITIVE_TYPES) {
        result.addElement(
            PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(type)
                    .withTypeText("primitive")
                    .withBoldness(true)
                    .withIcon(AllIcons.Nodes.Type),
                200));
      }

      // Add wrapper types
      for (String type : WRAPPER_TYPES) {
        result.addElement(
            PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(type)
                    .withTypeText("java.lang")
                    .withIcon(AllIcons.Nodes.Class),
                150));
      }

      // Add common collection/utility types
      for (String type : COMMON_TYPES) {
        result.addElement(
            PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(type)
                    .withTypeText("java.util")
                    .withIcon(AllIcons.Nodes.Class),
                120));
      }

      // Also add project classes from classpath
      PsiElement position = parameters.getPosition();
      Project project = position.getProject();
      GlobalSearchScope scope = getSearchScope(position, project);
      PsiShortNamesCache namesCache = PsiShortNamesCache.getInstance(project);

      String prefix = result.getPrefixMatcher().getPrefix();
      if (prefix.length() >= 2) {
        String[] classNames = namesCache.getAllClassNames();
        for (String className : classNames) {
          if (!className.toLowerCase().startsWith(prefix.toLowerCase())) continue;
          PsiClass[] classes = namesCache.getClassesByName(className, scope);
          for (PsiClass psiClass : classes) {
            if (psiClass == null || !psiClass.isValid()) continue;
            String qualifiedName = psiClass.getQualifiedName();
            if (qualifiedName == null) continue;
            if (psiClass.getName() == null || psiClass.getName().isEmpty()) continue;

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(psiClass, className)
                        .withTypeText(getPackageName(qualifiedName), true)
                        .withIcon(AllIcons.Nodes.Class),
                    80));
          }
        }
      }
    }

    private String getPackageName(String qualifiedName) {
      int lastDot = qualifiedName.lastIndexOf('.');
      return lastDot > 0 ? qualifiedName.substring(0, lastDot) : "";
    }
  }

  // --- Utility Methods ---

  /** Creates a bold keyword lookup element with "keyword" type text. */
  private static LookupElement createKeywordLookup(String keyword) {
    return createKeywordLookup(keyword, "keyword");
  }

  /** Creates a bold keyword lookup element with custom type text. */
  private static LookupElement createKeywordLookup(String keyword, String typeText) {
    return LookupElementBuilder.create(keyword)
        .withTypeText(typeText)
        .withBoldness(true)
        .withIcon(AllIcons.Nodes.EntryPoints);
  }

  /** Gets the set of fully qualified class names that are already imported in the file. */
  private static Set<String> getImportedClassNames(PsiFile file) {
    Set<String> imported = new HashSet<>();
    if (file instanceof DroolsPsiFile droolsFile) {
      for (DroolsImportStatement importStmt : droolsFile.getImports()) {
        DroolsImportPath importPath = importStmt.getImportPath();
        if (importPath != null) {
          String path = importPath.getText();
          if (path != null && !path.isEmpty()) {
            imported.add(path);
            // Also add the simple name
            int lastDot = path.lastIndexOf('.');
            if (lastDot > 0) {
              imported.add(path.substring(lastDot + 1));
            }
          }
        }
      }
    }
    return imported;
  }

  /** Gets the appropriate search scope for class resolution. */
  private static GlobalSearchScope getSearchScope(PsiElement element, Project project) {
    Module module = ModuleUtilCore.findModuleForPsiElement(element);
    if (module != null) {
      return GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);
    }
    return GlobalSearchScope.allScope(project);
  }

  /**
   * Insert handler that auto-imports a class when selected from completion in a when-clause. If the
   * class is not already imported and not in java.lang, inserts an import statement.
   */
  private static class AutoImportInsertHandler implements InsertHandler<LookupElement> {
    private final String qualifiedName;
    private final boolean alreadyImported;

    AutoImportInsertHandler(String qualifiedName, boolean alreadyImported) {
      this.qualifiedName = qualifiedName;
      this.alreadyImported = alreadyImported;
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
      if (alreadyImported) return;
      if (qualifiedName.startsWith(JAVA_DOT_LANG_DOT)
          && !qualifiedName.substring(10).contains(".")) {
        return; // java.lang classes don't need import
      }

      PsiFile file = context.getFile();
      if (!(file instanceof DroolsPsiFile droolsFile)) return;

      // Check if already imported
      for (DroolsImportStatement importStmt : droolsFile.getImports()) {
        DroolsImportPath importPath = importStmt.getImportPath();
        if (importPath != null && qualifiedName.equals(importPath.getText())) {
          return; // Already imported
        }
      }

      // Insert import statement
      Document document = context.getDocument();
      int insertOffset = findImportInsertOffset(droolsFile, document);
      String importLine = "import " + qualifiedName + ";\n";
      document.insertString(insertOffset, importLine);

      // Commit the document to update PSI
      PsiDocumentManager.getInstance(context.getProject()).commitDocument(document);
    }

    /**
     * Finds the offset where a new import statement should be inserted. Places it after the last
     * existing import, or after the package declaration, or at the beginning of the file.
     */
    private int findImportInsertOffset(DroolsPsiFile file, Document document) {
      List<DroolsImportStatement> imports = file.getImports();
      if (!imports.isEmpty()) {
        // Insert after the last import
        DroolsImportStatement lastImport = imports.get(imports.size() - 1);
        int lineNumber = document.getLineNumber(lastImport.getTextRange().getEndOffset());
        return document.getLineEndOffset(lineNumber) + 1;
      }

      // Insert after package declaration
      DroolsPackageDecl packageDecl = file.getPackageDecl();
      if (packageDecl != null) {
        int lineNumber = document.getLineNumber(packageDecl.getTextRange().getEndOffset());
        return document.getLineEndOffset(lineNumber) + 1;
      }

      // Insert at beginning of file
      return 0;
    }
  }
}
