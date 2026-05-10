package com.plugin.drool;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides bracket matching for Drools files. Matches parentheses (), braces {}, and brackets [].
 */
public class DroolsBracketMatcher implements PairedBraceMatcher {

  private static final BracePair[] PAIRS =
      new BracePair[] {
        new BracePair(DroolsTokenTypes.LEFT_PAREN, DroolsTokenTypes.RIGHT_PAREN, false),
        new BracePair(DroolsTokenTypes.LEFT_BRACE, DroolsTokenTypes.RIGHT_BRACE, true),
        new BracePair(DroolsTokenTypes.LEFT_BRACKET, DroolsTokenTypes.RIGHT_BRACKET, false),
      };

  @Override
  public BracePair @NotNull [] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(
      @NotNull IElementType lbraceType, @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
