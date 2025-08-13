package com.plugin.drool;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DroolsFileType extends LanguageFileType {

  private DroolsFileType(){
      super(DroolsLanguage.INSTANCE);
  }

  private static class SingletonHelper {
    public static final DroolsFileType INSTANCE = new DroolsFileType();
  }

  public static DroolsFileType getInstance(){
    return SingletonHelper.INSTANCE;
  }

  @NotNull
  @Override
  public String getName() {
    return "Drools";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Drools rule language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "drl";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return IconLoader.getIcon("/META-INF/pluginIcon.svg", DroolsFileType.class);
  }
}
