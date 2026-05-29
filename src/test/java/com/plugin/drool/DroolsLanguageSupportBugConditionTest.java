package com.plugin.drool;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bug condition exploration property-based test for Drools Language Support.
 *
 * <p>This test encodes the EXPECTED (correct) behavior for three bugs:
 * <ul>
 *   <li>Bug 1: Dot completion on $-prefixed binding variables (then-clause and constraints)</li>
 *   <li>Bug 2: Unterminated string literal detection</li>
 *   <li>Bug 3: Class/method navigation (Ctrl+click) on fact patterns and $-binding method calls</li>
 * </ul>
 *
 * <p><b>CRITICAL</b>: This test is EXPECTED TO FAIL on unfixed code. Failure confirms the bugs exist.
 * DO NOT attempt to fix the test or the code when it fails.
 *
 * <p>Validates: Requirements 1.1, 1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7
 */
public class DroolsLanguageSupportBugConditionTest extends LightJavaCodeInsightFixtureTestCase {

    private static final String FIXTURE_CLASS_NAME = "TestFactType";
    private static final String FIXTURE_PACKAGE = "com.test.fixture";
    private static final String FIXTURE_FQN = FIXTURE_PACKAGE + "." + FIXTURE_CLASS_NAME;

    // Expected bean properties from TestFactType (derived from getters)
    private static final List<String> EXPECTED_BEAN_PROPERTIES = List.of(
            "resourceId", "age", "active", "name"
    );

    // Expected getter method names from TestFactType
    private static final List<String> EXPECTED_GETTER_NAMES = List.of(
            "getResourceId", "getAge", "isActive", "getName"
    );

    // Expected public methods (all public methods including setters)
    private static final List<String> EXPECTED_PUBLIC_METHODS = List.of(
            "getResourceId", "setResourceId", "getAge", "setAge",
            "isActive", "setActive", "getName", "setName"
    );

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Add the fixture Java class to the project so it's resolvable
        myFixture.addClass(
                "package com.test.fixture;\n" +
                "public class TestFactType {\n" +
                "    public String name;\n" +
                "    private String resourceId;\n" +
                "    private int age;\n" +
                "    private boolean active;\n" +
                "    public String getResourceId() { return resourceId; }\n" +
                "    public void setResourceId(String resourceId) { this.resourceId = resourceId; }\n" +
                "    public int getAge() { return age; }\n" +
                "    public void setAge(int age) { this.age = age; }\n" +
                "    public boolean isActive() { return active; }\n" +
                "    public void setActive(boolean active) { this.active = active; }\n" +
                "    public String getName() { return name; }\n" +
                "    public void setName(String name) { this.name = name; }\n" +
                "}\n"
        );
    }

    // ========================================================================
    // Sub-property 1a: Bug 1 - Then-clause dot completion on $-binding
    // ========================================================================

    /**
     * Sub-property 1a (Bug 1, then-clause dot completion):
     * Build a DRL file containing $alloyDbBackup : TestFactType() in the when-clause
     * and $alloyDbBackup.&lt;caret&gt; in the then-clause; invoke completeBasic(); assert
     * getLookupElementStrings() contains the fixture's getter names.
     *
     * <p><b>Will fail on unfixed code (empty or missing suggestions).</b>
     *
     * <p><b>Validates: Requirements 2.1</b>
     */
    public void testSubProperty1a_ThenClauseDotCompletion() {
        // Real-world example from cleaner-alloydb-backup-recommendation-rules.drl
        String bindingName = "alloyDbBackup";
        String className = FIXTURE_CLASS_NAME;

        String drlContent = buildThenClauseDotCompletionDrl(bindingName, className);
        myFixture.configureByText("test_1a_" + bindingName + ".drl", drlContent);

        LookupElement[] completions = myFixture.completeBasic();
        List<String> lookupStrings = myFixture.getLookupElementStrings();

        // Assert that completion contains the expected public methods
        boolean hasExpectedMethods = lookupStrings != null && !lookupStrings.isEmpty()
                && EXPECTED_PUBLIC_METHODS.stream().anyMatch(lookupStrings::contains);

        if (!hasExpectedMethods) {
            fail(String.format(
                    "Bug 1 (then-clause dot completion) confirmed - counterexample:\n" +
                    "Sub-property 1a: lookup result was %s for $%s. with binding $%s : %s()",
                    lookupStrings, bindingName, bindingName, className));
        }
    }

    // ========================================================================
    // Sub-property 1b: Bug 1 - Constraint dot completion on $-binding
    // ========================================================================

    /**
     * Sub-property 1b (Bug 1, constraint dot completion):
     * Caret inside a fact-pattern constraint ($alloyDbBackup.&lt;caret&gt;
     * between ( and )); assert lookup contains the bean property names.
     *
     * <p><b>Will fail on unfixed code.</b>
     *
     * <p><b>Validates: Requirements 1.2</b>
     */
    public void testSubProperty1b_ConstraintDotCompletion() {
        // Real-world example from cleaner-alloydb-backup-recommendation-rules.drl
        String bindingName = "alloyDbBackup";
        String className = FIXTURE_CLASS_NAME;

        String drlContent = buildConstraintDotCompletionDrl(bindingName, className);
        myFixture.configureByText("test_1b_" + bindingName + ".drl", drlContent);

        LookupElement[] completions = myFixture.completeBasic();
        List<String> lookupStrings = myFixture.getLookupElementStrings();

        // Assert that completion contains bean properties
        boolean hasBeanProperties = lookupStrings != null && !lookupStrings.isEmpty()
                && EXPECTED_BEAN_PROPERTIES.stream().anyMatch(lookupStrings::contains);

        if (!hasBeanProperties) {
            fail(String.format(
                    "Bug 1 (constraint dot completion) confirmed - counterexample:\n" +
                    "Sub-property 1b: lookup result was %s for $%s. in constraint with binding $%s : %s()",
                    lookupStrings, bindingName, bindingName, className));
        }
    }

    // ========================================================================
    // Sub-property 1c: Bug 2 - Single-line unterminated string
    // ========================================================================

    /**
     * Sub-property 1c (Bug 2, single-line unterminated string):
     * Build a then-clause containing String s = "Review and delete AlloyDB backups;
     * (no closing "); invoke doHighlighting(); assert at least one
     * HighlightSeverity.ERROR annotation whose message matches (?i)unterminated string.
     *
     * <p><b>Will fail on unfixed code.</b>
     *
     * <p><b>Validates: Requirements 2.3</b>
     */
    public void testSubProperty1c_UnterminatedStringSingleLine() {
        // Real-world example from cleaner-alloydb-backup-recommendation-rules.drl
        String text = "Review and delete AlloyDB backups";

        String drlContent = buildUnterminatedStringDrl(text);
        myFixture.configureByText("test_1c_unterminated.drl", drlContent);

        var highlights = myFixture.doHighlighting();

        boolean hasUnterminatedStringError = highlights.stream().anyMatch(info ->
                info.getSeverity().compareTo(HighlightSeverity.ERROR) >= 0
                        && info.getDescription() != null
                        && info.getDescription().toLowerCase().contains("unterminated string"));

        if (!hasUnterminatedStringError) {
            String errorMessages = highlights.stream()
                    .filter(info -> info.getSeverity().compareTo(HighlightSeverity.ERROR) >= 0)
                    .map(info -> info.getDescription())
                    .collect(Collectors.joining(", "));
            fail(String.format(
                    "Bug 2 (unterminated string, single-line) confirmed - counterexample:\n" +
                    "Sub-property 1c: no 'unterminated string' error for: String s = \"%s; (errors found: [%s])",
                    text, errorMessages));
        }
    }

    // ========================================================================
    // Sub-property 1d: Bug 2 - Lone leading quote
    // ========================================================================

    /**
     * Sub-property 1d (Bug 2, lone leading quote):
     * Build a then-clause containing a single " followed by ; and no other quote on the line;
     * same assertion.
     *
     * <p><b>Will fail on unfixed code.</b>
     *
     * <p><b>Validates: Requirements 2.4</b>
     */
    public void testSubProperty1d_LoneLeadingQuote() {
        // Single concrete case: just a quote followed by semicolon
        String loneQuoteExpr = "\";";

        String drlContent = buildLoneQuoteDrl(loneQuoteExpr);
        myFixture.configureByText("test_1d_lone_quote.drl", drlContent);

        var highlights = myFixture.doHighlighting();

        boolean hasUnterminatedStringError = highlights.stream().anyMatch(info ->
                info.getSeverity().compareTo(HighlightSeverity.ERROR) >= 0
                        && info.getDescription() != null
                        && info.getDescription().toLowerCase().contains("unterminated string"));

        if (!hasUnterminatedStringError) {
            String errorMessages = highlights.stream()
                    .filter(info -> info.getSeverity().compareTo(HighlightSeverity.ERROR) >= 0)
                    .map(info -> info.getDescription())
                    .collect(Collectors.joining(", "));
            fail(String.format(
                    "Bug 2 (lone leading quote) confirmed - counterexample:\n" +
                    "Sub-property 1d: no 'unterminated string' error for lone quote: %s (errors found: [%s])",
                    loneQuoteExpr, errorMessages));
        }
    }

    // ========================================================================
    // Sub-property 1e: Bug 3a - Class name navigation
    // ========================================================================

    /**
     * Sub-property 1e (Bug 3a, class name navigation):
     * For the real-world fact pattern using the fixture class with a matching import,
     * place the caret on the class IDENTIFIER leaf and assert getReferenceAtCaretPosition()
     * returns a non-null reference whose resolve() is the fixture's PsiClass.
     *
     * <p><b>Will fail on unfixed code (null or unresolved).</b>
     *
     * <p><b>Validates: Requirements 2.5</b>
     */
    public void testSubProperty1e_ClassNameNavigation() {
        // Real-world example from cleaner-alloydb-backup-recommendation-rules.drl
        String bindingName = "alloyDbBackup";

        String drlContent = buildClassNavigationDrl(bindingName);
        myFixture.configureByText("test_1e_" + bindingName + ".drl", drlContent);

        PsiReference reference = myFixture.getReferenceAtCaretPosition();

        StringBuilder failures = new StringBuilder();
        if (reference == null) {
            failures.append(String.format(
                    "Sub-property 1e: getReferenceAtCaretPosition() returned null for %s in $%s : %s()",
                    FIXTURE_CLASS_NAME, bindingName, FIXTURE_CLASS_NAME));
        } else {
            PsiElement resolved = reference.resolve();
            if (resolved == null) {
                failures.append(String.format(
                        "Sub-property 1e: reference.resolve() returned null for %s in $%s : %s() (reference text: '%s')",
                        FIXTURE_CLASS_NAME, bindingName, FIXTURE_CLASS_NAME, reference.getCanonicalText()));
            } else if (!(resolved instanceof PsiClass)) {
                failures.append(String.format(
                        "Sub-property 1e: resolved element is not PsiClass for %s, got: %s",
                        FIXTURE_CLASS_NAME, resolved.getClass().getSimpleName()));
            } else {
                PsiClass resolvedClass = (PsiClass) resolved;
                if (!FIXTURE_FQN.equals(resolvedClass.getQualifiedName())) {
                    failures.append(String.format(
                            "Sub-property 1e: resolved to wrong class: expected %s, got %s",
                            FIXTURE_FQN, resolvedClass.getQualifiedName()));
                }
            }
        }

        if (!failures.isEmpty()) {
            fail("Bug 3a (class name navigation) confirmed - counterexample:\n" + failures);
        }
    }

    // ========================================================================
    // Sub-property 1f: Bug 3b - Method navigation on $-binding
    // ========================================================================

    /**
     * Sub-property 1f (Bug 3b, method navigation on $-binding):
     * For $alloyDbBackup.getResourceId() where $alloyDbBackup : TestFactType is declared,
     * place the caret on the method IDENTIFIER leaf and assert the reference resolves to
     * the matching PsiMethod.
     *
     * <p><b>Will fail on unfixed code.</b>
     *
     * <p><b>Validates: Requirements 2.6</b>
     */
    public void testSubProperty1f_MethodNavigationOnBinding() {
        // Real-world example: single concrete case
        String bindingName = "alloyDbBackup";
        String methodName = "getResourceId";

        String drlContent = buildMethodNavigationDrl(bindingName, methodName);
        myFixture.configureByText("test_1f_" + methodName + ".drl", drlContent);

        PsiReference reference = myFixture.getReferenceAtCaretPosition();

        StringBuilder failures = new StringBuilder();
        if (reference == null) {
            failures.append(String.format(
                    "Sub-property 1f: getReferenceAtCaretPosition() returned null for %s in $%s.%s()",
                    methodName, bindingName, methodName));
        } else {
            PsiElement resolved = reference.resolve();
            if (resolved == null) {
                failures.append(String.format(
                        "Sub-property 1f: reference.resolve() returned null for %s in $%s.%s() (reference text: '%s')",
                        methodName, bindingName, methodName, reference.getCanonicalText()));
            } else if (!(resolved instanceof PsiMethod)) {
                failures.append(String.format(
                        "Sub-property 1f: resolved element is not PsiMethod for %s, got: %s",
                        methodName, resolved.getClass().getSimpleName()));
            } else {
                PsiMethod resolvedMethod = (PsiMethod) resolved;
                if (!methodName.equals(resolvedMethod.getName())) {
                    failures.append(String.format(
                            "Sub-property 1f: resolved to wrong method: expected %s, got %s",
                            methodName, resolvedMethod.getName()));
                }
            }
        }

        if (!failures.isEmpty()) {
            fail("Bug 3b (method navigation on $-binding) confirmed - counterexample:\n" + failures);
        }
    }

    // ========================================================================
    // Sub-property 1g: Bug 3 sanity - Import click navigation (should PASS)
    // ========================================================================

    /**
     * Sub-property 1g (Bug 3, sanity, import click):
     * Place the caret on a class IDENTIFIER leaf inside import com.test.fixture.TestFactType;
     * and assert the reference resolves to the PsiClass.
     *
     * <p><b>Should pass on unfixed code (sanity check that import-path navigation is not broken).</b>
     *
     * <p><b>Validates: Requirements 2.7</b>
     */
    public void testSubProperty1g_ImportClickNavigation() {
        String drlContent =
                "package com.test.rules;\n" +
                "import com.test.fixture.Test<caret>FactType;\n" +
                "\n" +
                "rule \"sanity check\"\n" +
                "when\n" +
                "    $x : TestFactType()\n" +
                "then\n" +
                "    System.out.println(\"ok\");\n" +
                "end\n";

        myFixture.configureByText("test_1g_import.drl", drlContent);

        PsiReference reference = myFixture.getReferenceAtCaretPosition();

        // This should work on unfixed code - import-path navigation is not broken
        assertNotNull("Import click: getReferenceAtCaretPosition() should not be null", reference);
        PsiElement resolved = reference.resolve();
        assertNotNull("Import click: reference.resolve() should not be null", resolved);
        assertInstanceOf(resolved, PsiClass.class);
        assertEquals(FIXTURE_FQN, ((PsiClass) resolved).getQualifiedName());
    }

    // ========================================================================
    // Helper methods for building DRL test content
    // ========================================================================

    /**
     * Builds a DRL file with $bindingName : className() in when-clause
     * and $bindingName.&lt;caret&gt; in then-clause for dot completion testing.
     */
    private String buildThenClauseDotCompletionDrl(String bindingName, String className) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test dot completion\"\n" +
                "when\n" +
                "    $" + bindingName + " : " + className + "()\n" +
                "then\n" +
                "    $" + bindingName + ".<caret>\n" +
                "end\n";
    }

    /**
     * Builds a DRL file with $bindingName.&lt;caret&gt; inside a constraint for
     * bean property completion testing.
     */
    private String buildConstraintDotCompletionDrl(String bindingName, String className) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test constraint completion\"\n" +
                "when\n" +
                "    $" + bindingName + " : " + className + "($" + bindingName + ".<caret>)\n" +
                "then\n" +
                "    System.out.println(\"ok\");\n" +
                "end\n";
    }

    /**
     * Builds a DRL file with an unterminated string literal in the then-clause.
     */
    private String buildUnterminatedStringDrl(String text) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test unterminated string\"\n" +
                "when\n" +
                "    $x : TestFactType()\n" +
                "then\n" +
                "    String s = \"" + text + ";\n" +
                "end\n";
    }

    /**
     * Builds a DRL file with a lone leading quote expression in the then-clause.
     */
    private String buildLoneQuoteDrl(String loneQuoteExpr) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test lone quote\"\n" +
                "when\n" +
                "    $x : TestFactType()\n" +
                "then\n" +
                "    String s = " + loneQuoteExpr + "\n" +
                "end\n";
    }

    /**
     * Builds a DRL file with the caret on the class name in a fact pattern for navigation testing.
     */
    private String buildClassNavigationDrl(String bindingName) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test class navigation\"\n" +
                "when\n" +
                "    $" + bindingName + " : <caret>" + FIXTURE_CLASS_NAME + "()\n" +
                "then\n" +
                "    System.out.println(\"ok\");\n" +
                "end\n";
    }

    /**
     * Builds a DRL file with the caret on a method name in a $-binding method call.
     */
    private String buildMethodNavigationDrl(String bindingName, String methodName) {
        return "package com.test.rules;\n" +
                "import " + FIXTURE_FQN + ";\n" +
                "\n" +
                "rule \"test method navigation\"\n" +
                "when\n" +
                "    $" + bindingName + " : " + FIXTURE_CLASS_NAME + "()\n" +
                "then\n" +
                "    $" + bindingName + ".<caret>" + methodName + "();\n" +
                "end\n";
    }

    /**
     * Sanitizes a string for use as a filename (removes special characters).
     */
    private String sanitizeFilename(String text) {
        return text.replaceAll("[^a-zA-Z0-9]", "_").substring(0, Math.min(text.length(), 20));
    }
}
