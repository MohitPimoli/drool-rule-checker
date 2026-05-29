package com.plugin.drool;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.plugin.drool.psi.*;

public class DiagnosticTest extends LightJavaCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return "src/test/resources";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.addClass(
        "package com.test.fixture;\n"
            + "public class TestFactType {\n"
            + "    public String name;\n"
            + "    private String resourceId;\n"
            + "    public String getResourceId() { return resourceId; }\n"
            + "}\n");
  }

  public void testDiagnosticImportClick() {
    String drlContent =
        "package com.test.rules;\n"
            + "import com.test.fixture.Test<caret>FactType;\n"
            + "\n"
            + "rule \"sanity check\"\n"
            + "when\n"
            + "    $x : TestFactType()\n"
            + "then\n"
            + "    System.out.println(\"ok\");\n"
            + "end\n";

    myFixture.configureByText("diag_import.drl", drlContent);

    PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
    System.out.println("=== DIAGNOSTIC: Import Click ===");
    System.out.println(
        "Element at caret: "
            + (elementAtCaret != null
                ? elementAtCaret.getClass().getSimpleName()
                    + " text='"
                    + elementAtCaret.getText()
                    + "'"
                : "null"));
    if (elementAtCaret != null) {
      System.out.println("Element type: " + elementAtCaret.getNode().getElementType());
      System.out.println("Language: " + elementAtCaret.getLanguage());
      System.out.println(
          "Parent: "
              + (elementAtCaret.getParent() != null
                  ? elementAtCaret.getParent().getClass().getSimpleName()
                  : "null"));
      System.out.println(
          "Parent parent: "
              + (elementAtCaret.getParent() != null
                      && elementAtCaret.getParent().getParent() != null
                  ? elementAtCaret.getParent().getParent().getClass().getSimpleName()
                  : "null"));

      DroolsImportPath importPath =
          PsiTreeUtil.getParentOfType(elementAtCaret, DroolsImportPath.class);
      System.out.println(
          "DroolsImportPath ancestor: "
              + (importPath != null ? "found, text='" + importPath.getText() + "'" : "null"));

      DroolsClassName className =
          PsiTreeUtil.getParentOfType(elementAtCaret, DroolsClassName.class);
      System.out.println(
          "DroolsClassName ancestor: "
              + (className != null ? "found, text='" + className.getText() + "'" : "null"));

      PsiReference ref = myFixture.getReferenceAtCaretPosition();
      System.out.println(
          "Reference at caret: "
              + (ref != null
                  ? ref.getClass().getSimpleName() + " canonical='" + ref.getCanonicalText() + "'"
                  : "null"));

      PsiReference[] refs = elementAtCaret.getReferences();
      System.out.println("Element references count: " + refs.length);
      for (PsiReference r : refs) {
        System.out.println(
            "  ref: " + r.getClass().getSimpleName() + " canonical='" + r.getCanonicalText() + "'");
      }
    }
  }

  public void testDiagnosticClassNavigation() {
    String drlContent =
        "package com.test.rules;\n"
            + "import com.test.fixture.TestFactType;\n"
            + "\n"
            + "rule \"test class navigation\"\n"
            + "when\n"
            + "    $alloyDbBackup : <caret>TestFactType()\n"
            + "then\n"
            + "    System.out.println(\"ok\");\n"
            + "end\n";

    myFixture.configureByText("diag_class.drl", drlContent);

    PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
    System.out.println("=== DIAGNOSTIC: Class Navigation ===");
    System.out.println(
        "Element at caret: "
            + (elementAtCaret != null
                ? elementAtCaret.getClass().getSimpleName()
                    + " text='"
                    + elementAtCaret.getText()
                    + "'"
                : "null"));
    if (elementAtCaret != null) {
      System.out.println("Element type: " + elementAtCaret.getNode().getElementType());
      System.out.println("Language: " + elementAtCaret.getLanguage());
      System.out.println(
          "Parent: "
              + (elementAtCaret.getParent() != null
                  ? elementAtCaret.getParent().getClass().getSimpleName()
                  : "null"));
      System.out.println(
          "Parent parent: "
              + (elementAtCaret.getParent() != null
                      && elementAtCaret.getParent().getParent() != null
                  ? elementAtCaret.getParent().getParent().getClass().getSimpleName()
                  : "null"));
      System.out.println(
          "Parent parent parent: "
              + (elementAtCaret.getParent() != null
                      && elementAtCaret.getParent().getParent() != null
                      && elementAtCaret.getParent().getParent().getParent() != null
                  ? elementAtCaret.getParent().getParent().getParent().getClass().getSimpleName()
                  : "null"));

      DroolsImportPath importPath =
          PsiTreeUtil.getParentOfType(elementAtCaret, DroolsImportPath.class);
      System.out.println("DroolsImportPath ancestor: " + (importPath != null ? "found" : "null"));

      DroolsClassName classNameEl =
          PsiTreeUtil.getParentOfType(elementAtCaret, DroolsClassName.class);
      System.out.println(
          "DroolsClassName ancestor: "
              + (classNameEl != null ? "found, text='" + classNameEl.getText() + "'" : "null"));

      PsiReference ref = myFixture.getReferenceAtCaretPosition();
      System.out.println(
          "Reference at caret: " + (ref != null ? ref.getClass().getSimpleName() : "null"));

      PsiReference[] refs = elementAtCaret.getReferences();
      System.out.println("Element references count: " + refs.length);
      for (PsiReference r : refs) {
        System.out.println(
            "  ref: " + r.getClass().getSimpleName() + " canonical='" + r.getCanonicalText() + "'");
      }
    }
  }
}
