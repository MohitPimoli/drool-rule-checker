package com.plugin.drool;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Provides comment/uncomment support for Drools files. Supports single-line comments (//) and block
 * comments (/* ... *&#47;).
 */
public class DroolsCommenter implements Commenter {

  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "//";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return "/*";
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return "*/";
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return null;
  }
}
