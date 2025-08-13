package com.plugin.drool;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

// PSI File class
class DroolsPsiFile extends PsiFileBase {
  public DroolsPsiFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, DroolsLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return DroolsFileType.getInstance();
  }
}
