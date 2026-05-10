package com.plugin.drool;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.plugin.drool.psi.DroolsTypes;
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

  private static final TokenSet KEYWORD_SET = TokenSet.create(
      DroolsTypes.RULE_KEYWORD,
      DroolsTypes.WHEN_KEYWORD,
      DroolsTypes.THEN_KEYWORD,
      DroolsTypes.END_KEYWORD,
      DroolsTypes.PACKAGE_KEYWORD,
      DroolsTypes.IMPORT_KEYWORD,
      DroolsTypes.GLOBAL_KEYWORD,
      DroolsTypes.FUNCTION_KEYWORD,
      DroolsTypes.DECLARE_KEYWORD,
      DroolsTypes.QUERY_KEYWORD,
      DroolsTypes.SALIENCE_KEYWORD,
      DroolsTypes.NO_LOOP_KEYWORD,
      DroolsTypes.RULEFLOW_GROUP_KEYWORD,
      DroolsTypes.AGENDA_GROUP_KEYWORD,
      DroolsTypes.AUTO_FOCUS_KEYWORD,
      DroolsTypes.LOCK_ON_ACTIVE_KEYWORD,
      DroolsTypes.DATE_EFFECTIVE_KEYWORD,
      DroolsTypes.DATE_EXPIRES_KEYWORD,
      DroolsTypes.ENABLED_KEYWORD,
      DroolsTypes.DURATION_KEYWORD,
      DroolsTypes.TIMER_KEYWORD,
      DroolsTypes.DIALECT_KEYWORD,
      DroolsTypes.EXTENDS_KEYWORD,
      DroolsTypes.AND_KEYWORD,
      DroolsTypes.OR_KEYWORD,
      DroolsTypes.NOT_KEYWORD,
      DroolsTypes.EXISTS_KEYWORD,
      DroolsTypes.FORALL_KEYWORD,
      DroolsTypes.FROM_KEYWORD,
      DroolsTypes.COLLECT_KEYWORD,
      DroolsTypes.ACCUMULATE_KEYWORD,
      DroolsTypes.EVAL_KEYWORD,
      DroolsTypes.INSERT_KEYWORD,
      DroolsTypes.INSERT_LOGICAL_KEYWORD,
      DroolsTypes.UPDATE_KEYWORD,
      DroolsTypes.MODIFY_KEYWORD,
      DroolsTypes.RETRACT_KEYWORD,
      DroolsTypes.DELETE_KEYWORD,
      DroolsTypes.MATCHES_KEYWORD,
      DroolsTypes.CONTAINS_KEYWORD,
      DroolsTypes.MEMBER_OF_KEYWORD,
      DroolsTypes.SOUNDSLIKE_KEYWORD,
      DroolsTypes.STR_KEYWORD,
      DroolsTypes.IN_KEYWORD,
      DroolsTypes.TRUE_KEYWORD,
      DroolsTypes.FALSE_KEYWORD,
      DroolsTypes.NULL_KEYWORD,
      DroolsTypes.NEW_KEYWORD,
      DroolsTypes.IF_KEYWORD,
      DroolsTypes.ELSE_KEYWORD,
      DroolsTypes.RETURN_KEYWORD,
      DroolsTypes.THIS_KEYWORD
  );

  private static final TokenSet BRACKETS_SET = TokenSet.create(
      DroolsTypes.LEFT_PAREN,
      DroolsTypes.RIGHT_PAREN,
      DroolsTypes.LEFT_BRACE,
      DroolsTypes.RIGHT_BRACE,
      DroolsTypes.LEFT_BRACKET,
      DroolsTypes.RIGHT_BRACKET
  );

  private static final TokenSet PUNCTUATION_SET = TokenSet.create(
      DroolsTypes.SEMICOLON,
      DroolsTypes.COMMA,
      DroolsTypes.DOT,
      DroolsTypes.COLON,
      DroolsTypes.DOLLAR
  );

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new DroolsLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (KEYWORD_SET.contains(tokenType)) {
      return KEYWORD_KEYS;
    } else if (tokenType.equals(DroolsTypes.IDENTIFIER)) {
      return IDENTIFIER_KEYS;
    } else if (tokenType.equals(DroolsTypes.STRING)) {
      return STRING_KEYS;
    } else if (tokenType.equals(DroolsTypes.NUMBER)) {
      return NUMBER_KEYS;
    } else if (tokenType.equals(DroolsTypes.COMMENT)) {
      return COMMENT_KEYS;
    } else if (tokenType.equals(DroolsTypes.OPERATOR)) {
      return OPERATOR_KEYS;
    } else if (BRACKETS_SET.contains(tokenType)) {
      return BRACKETS_KEYS;
    } else if (PUNCTUATION_SET.contains(tokenType)) {
      return PUNCTUATION_KEYS;
    } else {
      return EMPTY_KEYS;
    }
  }
}
