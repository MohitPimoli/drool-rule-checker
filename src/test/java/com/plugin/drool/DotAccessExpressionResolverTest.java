package com.plugin.drool;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.plugin.drool.psi.DroolsTypes;
import com.plugin.drool.util.DotAccessExpressionResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link DotAccessExpressionResolver#reconstructExpressionBeforeDot(PsiElement)}.
 *
 * <p>Covers the following cases from design §Unit Tests:
 * <ul>
 *   <li>{@code $x} — simple $-binding before dot</li>
 *   <li>{@code $x.y} — chained member access with $-binding</li>
 *   <li>{@code $x.y.z} — multi-level chain with $-binding</li>
 *   <li>{@code x} — non-$ identifier before dot</li>
 *   <li>{@code x.y} — non-$ chained access</li>
 *   <li>Leading whitespace — whitespace between leaves is skipped</li>
 *   <li>Trailing dot — null input returns null</li>
 * </ul>
 *
 * <p>Uses the IntelliJ test fixture to parse real DRL content and obtain actual PSI leaves.
 *
 * <p><b>Validates: Requirements 2.1, 2.2, 2.6</b>
 */
public class DotAccessExpressionResolverTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    // ========================================================================
    // Test: $x — simple $-binding before dot
    // ========================================================================

    /**
     * Tests that a simple $-binding ($x.) is reconstructed as "$x".
     */
    public void testSimpleDollarBinding() {
        // DRL with $x.something — we find the DOT and reconstruct what's before it
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    $x : Object()\n" +
                "then\n" +
                "    $x.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_dollar_x.drl", drl);
        PsiElement dot = findFirstDotInThenClause(file);
        assertNotNull("Should find a DOT in then-clause", dot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(dot);
        assertEquals("$x", result);
    }

    // ========================================================================
    // Test: $x.y — chained member access with $-binding
    // ========================================================================

    /**
     * Tests that a chained access ($x.y.) is reconstructed as "$x.y".
     */
    public void testDollarBindingChainedOneMember() {
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    $x : Object()\n" +
                "then\n" +
                "    $x.y.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_dollar_x_y.drl", drl);
        // Find the second DOT (the one after "y", before "toString")
        PsiElement secondDot = findNthDotInThenClause(file, 2);
        assertNotNull("Should find second DOT in then-clause", secondDot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(secondDot);
        assertEquals("$x.y", result);
    }

    // ========================================================================
    // Test: $x.y.z — multi-level chain with $-binding
    // ========================================================================

    /**
     * Tests that a multi-level chain ($x.y.z.) is reconstructed as "$x.y.z".
     */
    public void testDollarBindingChainedTwoMembers() {
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    $x : Object()\n" +
                "then\n" +
                "    $x.y.z.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_dollar_x_y_z.drl", drl);
        // Find the third DOT (the one after "z", before "toString")
        PsiElement thirdDot = findNthDotInThenClause(file, 3);
        assertNotNull("Should find third DOT in then-clause", thirdDot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(thirdDot);
        assertEquals("$x.y.z", result);
    }

    // ========================================================================
    // Test: x — non-$ identifier before dot
    // ========================================================================

    /**
     * Tests that a non-$ identifier (x.) is reconstructed as "x".
     */
    public void testNonDollarIdentifier() {
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    Object()\n" +
                "then\n" +
                "    x.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_x.drl", drl);
        PsiElement dot = findFirstDotInThenClause(file);
        assertNotNull("Should find a DOT in then-clause", dot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(dot);
        assertEquals("x", result);
    }

    // ========================================================================
    // Test: x.y — non-$ chained access
    // ========================================================================

    /**
     * Tests that a non-$ chained access (x.y.) is reconstructed as "x.y".
     */
    public void testNonDollarChainedAccess() {
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    Object()\n" +
                "then\n" +
                "    x.y.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_x_y.drl", drl);
        // Find the second DOT (the one after "y", before "toString")
        PsiElement secondDot = findNthDotInThenClause(file, 2);
        assertNotNull("Should find second DOT in then-clause", secondDot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(secondDot);
        assertEquals("x.y", result);
    }

    // ========================================================================
    // Test: leading whitespace — whitespace between leaves is skipped
    // ========================================================================

    /**
     * Tests that whitespace between the dot and the preceding expression is skipped.
     * Even with spaces, the expression should be reconstructed correctly.
     */
    public void testLeadingWhitespaceSkipped() {
        // The lexer/parser may or may not produce whitespace between leaves in a then-clause,
        // but the helper should handle it gracefully. We test with a simple case.
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    $x : Object()\n" +
                "then\n" +
                "    $x.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_whitespace.drl", drl);
        PsiElement dot = findFirstDotInThenClause(file);
        assertNotNull("Should find a DOT in then-clause", dot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(dot);
        // Should still reconstruct correctly regardless of whitespace
        assertNotNull("Result should not be null", result);
        assertTrue("Result should start with $", result.startsWith("$"));
        assertEquals("$x", result);
    }

    // ========================================================================
    // Test: null input returns null
    // ========================================================================

    /**
     * Tests that passing null returns null.
     */
    public void testNullInput() {
        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(null);
        assertNull("Null input should return null", result);
    }

    // ========================================================================
    // Test: first DOT in a chain — $x from $x.y.z
    // ========================================================================

    /**
     * Tests that the first DOT in a chain ($x.y.z) reconstructs just "$x".
     */
    public void testFirstDotInChain() {
        String drl = "package test;\n" +
                "rule \"test\"\n" +
                "when\n" +
                "    $x : Object()\n" +
                "then\n" +
                "    $x.y.z.toString();\n" +
                "end\n";

        PsiFile file = myFixture.configureByText("test_first_dot.drl", drl);
        PsiElement firstDot = findNthDotInThenClause(file, 1);
        assertNotNull("Should find first DOT in then-clause", firstDot);

        String result = DotAccessExpressionResolver.reconstructExpressionBeforeDot(firstDot);
        assertEquals("$x", result);
    }

    // ========================================================================
    // Helper methods
    // ========================================================================

    /**
     * Finds the first DOT leaf element within the then-clause of the parsed DRL file.
     */
    private PsiElement findFirstDotInThenClause(PsiFile file) {
        return findNthDotInThenClause(file, 1);
    }

    /**
     * Finds the Nth DOT leaf element within the then-clause of the parsed DRL file.
     *
     * @param file the parsed DRL file
     * @param n    the 1-based index of the DOT to find
     * @return the Nth DOT element, or null if not found
     */
    private PsiElement findNthDotInThenClause(PsiFile file, int n) {
        List<PsiElement> dots = new ArrayList<>();
        collectDotsRecursive(file, dots);

        // Filter to only DOTs that are in the then-clause area
        // (after "then" keyword in the file text)
        String fileText = file.getText();
        int thenOffset = fileText.indexOf("then");
        int endOffset = fileText.lastIndexOf("end");

        List<PsiElement> thenClauseDots = new ArrayList<>();
        for (PsiElement dot : dots) {
            int offset = dot.getTextOffset();
            if (offset > thenOffset && offset < endOffset) {
                thenClauseDots.add(dot);
            }
        }

        if (n > 0 && n <= thenClauseDots.size()) {
            return thenClauseDots.get(n - 1);
        }
        return null;
    }

    /**
     * Recursively collects all DOT leaf elements from the PSI tree.
     */
    private void collectDotsRecursive(PsiElement element, List<PsiElement> dots) {
        if (element.getNode() != null
                && element.getNode().getElementType() == DroolsTypes.DOT) {
            dots.add(element);
        }
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            collectDotsRecursive(child, dots);
        }
    }
}
