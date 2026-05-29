package com.plugin.drool;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceRegistrarImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.plugin.drool.psi.*;

public class DiagnosticTest2 extends LightJavaCodeInsightFixtureTestCase {

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

  public void testDiagnosticClassNavigation2() {
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

    myFixture.configureByText("diag_class2.drl", drlContent);

    PsiFile file = myFixture.getFile();
    int offset = myFixture.getCaretOffset();
    PsiElement elementAtCaret = file.findElementAt(offset);
    
    System.out.println("=== DIAGNOSTIC 2: Class Navigation ===");
    System.out.println("File type: " + file.getFileType().getName());
    System.out.println("File language: " + file.getLanguage());
    System.out.println("Caret offset: " + offset);
    System.out.println("Element at caret: " + (elementAtCaret != null ? elementAtCaret.getClass().getName() + " text='" + elementAtCaret.getText() + "'" : "null"));
    
    if (elementAtCaret != null) {
      System.out.println("Element type: " + elementAtCaret.getNode().getElementType());
      System.out.println("Element language: " + elementAtCaret.getLanguage());
      System.out.println("Element language ID: " + elementAtCaret.getLanguage().getID());
      
      // Try getting references via PsiReferenceService
      PsiReference[] serviceRefs = com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry.getReferencesFromProviders(elementAtCaret);
      System.out.println("References from providers: " + serviceRefs.length);
      for (PsiReference r : serviceRefs) {
        System.out.println("  provider ref: " + r.getClass().getSimpleName() + " canonical='" + r.getCanonicalText() + "'");
      }
      
      // Try file.findReferenceAt
      PsiReference refAt = file.findReferenceAt(offset);
      System.out.println("file.findReferenceAt: " + (refAt != null ? refAt.getClass().getSimpleName() : "null"));
      
      // Try getReferenceAtCaretPosition
      PsiReference caretRef = myFixture.getReferenceAtCaretPosition();
      System.out.println("getReferenceAtCaretPosition: " + (caretRef != null ? caretRef.getClass().getSimpleName() : "null"));
    }
  }
}
