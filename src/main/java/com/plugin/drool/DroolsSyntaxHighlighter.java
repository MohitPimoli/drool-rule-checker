package com.plugin.drool;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class DroolsSyntaxHighlighter extends SyntaxHighlighterBase {

  // Define text attribute keys for different token types
  public static final TextAttributesKey KEYWORD =
      createTextAttributesKey("DROOLS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

  public static final TextAttributesKey IDENTIFIER =
      createTextAttributesKey("DROOLS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

  public static final TextAttributesKey STRING =
      createTextAttributesKey("DROOLS_STRING", DefaultLanguageHighlighterColors.STRING);

  public static final TextAttributesKey NUMBER =
      createTextAttributesKey("DROOLS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

  public static final TextAttributesKey COMMENT =
      createTextAttributesKey("DROOLS_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

  public static final TextAttributesKey OPERATOR =
      createTextAttributesKey("DROOLS_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

  public static final TextAttributesKey PUNCTUATION =
      createTextAttributesKey("DROOLS_PUNCTUATION", DefaultLanguageHighlighterColors.PARENTHESES);

  public static final TextAttributesKey BRACKETS =
      createTextAttributesKey("DROOLS_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

  private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[] {KEYWORD};
  private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[] {IDENTIFIER};
  private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[] {STRING};
  private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[] {NUMBER};
  private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] {COMMENT};
  private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[] {OPERATOR};
  private static final TextAttributesKey[] PUNCTUATION_KEYS = new TextAttributesKey[] {PUNCTUATION};
  private static final TextAttributesKey[] BRACKETS_KEYS = new TextAttributesKey[] {BRACKETS};
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new DroolsLexer();
  }

  @NotNull
  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (tokenType.equals(DroolsTokenTypes.KEYWORD)) {
      return KEYWORD_KEYS;
    } else if (tokenType.equals(DroolsTokenTypes.IDENTIFIER)) {
      return IDENTIFIER_KEYS;
    } else if (tokenType.equals(DroolsTokenTypes.STRING)) {
      return STRING_KEYS;
    } else if (tokenType.equals(DroolsTokenTypes.NUMBER)) {
      return NUMBER_KEYS;
    } else if (tokenType.equals(DroolsTokenTypes.COMMENT)) {
      return COMMENT_KEYS;
    } else if (tokenType.equals(DroolsTokenTypes.OPERATOR)) {
      return OPERATOR_KEYS;
    } else if (DroolsTokenTypes.BRACKETS.contains(tokenType)) {
      return BRACKETS_KEYS;
    } else if (DroolsTokenTypes.PUNCTUATION.contains(tokenType)) {
      return PUNCTUATION_KEYS;
    } else {
      return EMPTY_KEYS;
    }
  }
}
