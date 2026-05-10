package com.plugin.drool;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import com.plugin.drool.psi.DroolsDeclareBlock;
import com.plugin.drool.psi.DroolsFunctionDef;
import com.plugin.drool.psi.DroolsQueryDef;
import com.plugin.drool.psi.DroolsRuleBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Structure view model for Drools files. Defines which PSI element types are shown in the structure
 * view.
 */
public class DroolsStructureViewModel extends StructureViewModelBase
    implements StructureViewModel.ElementInfoProvider {

  public DroolsStructureViewModel(@NotNull PsiFile psiFile) {
    super(psiFile, new DroolsStructureViewElement(psiFile));
  }

  @NotNull
  @Override
  protected Class<?> @NotNull [] getSuitableClasses() {
    return new Class<?>[] {
      DroolsRuleBlock.class, DroolsQueryDef.class, DroolsFunctionDef.class, DroolsDeclareBlock.class
    };
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    return element instanceof DroolsStructureViewElement droolsStructureViewElement
        && !(droolsStructureViewElement.getValue() instanceof PsiFile);
  }
}
