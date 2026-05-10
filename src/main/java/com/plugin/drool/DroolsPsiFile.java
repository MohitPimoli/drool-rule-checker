package com.plugin.drool;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.plugin.drool.psi.*;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PSI File class for Drools .drl files. Provides typed accessor methods for navigating the PSI
 * tree.
 */
public class DroolsPsiFile extends PsiFileBase implements DroolsFile {

  public DroolsPsiFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, DroolsLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return DroolsFileType.getInstance();
  }

  /** Returns the package declaration, or null if none exists. */
  @Nullable
  public DroolsPackageDecl getPackageDecl() {
    return PsiTreeUtil.getChildOfType(this, DroolsPackageDecl.class);
  }

  /** Returns all import statements in the file. */
  @NotNull
  public List<DroolsImportStatement> getImports() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsImportStatement.class);
  }

  /** Returns all global declarations in the file. */
  @NotNull
  public List<DroolsGlobalDecl> getGlobals() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsGlobalDecl.class);
  }

  /** Returns all rule blocks in the file. */
  @NotNull
  public List<DroolsRuleBlock> getRules() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsRuleBlock.class);
  }

  /** Returns all function definitions in the file. */
  @NotNull
  public List<DroolsFunctionDef> getFunctions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsFunctionDef.class);
  }

  /** Returns all declare blocks in the file. */
  @NotNull
  public List<DroolsDeclareBlock> getDeclares() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsDeclareBlock.class);
  }

  /** Returns all query definitions in the file. */
  @NotNull
  public List<DroolsQueryDef> getQueries() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DroolsQueryDef.class);
  }
}
