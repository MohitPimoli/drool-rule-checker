package com.plugin.drool;

import com.intellij.lexer.FlexAdapter;
import com.plugin.drool.lexer._DroolsLexer;

/**
 * Adapter that wraps the JFlex-generated {@link _DroolsLexer} for use with IntelliJ's lexer
 * infrastructure.
 */
public class DroolsLexerAdapter extends FlexAdapter {
  public DroolsLexerAdapter() {
    super(new _DroolsLexer(null));
  }
}
