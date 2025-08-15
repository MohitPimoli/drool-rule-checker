package com.plugin.drool;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public interface DroolsTokenTypes {
  IElementType KEYWORD = new IElementType("KEYWORD", DroolsLanguage.INSTANCE);
  IElementType IDENTIFIER = new IElementType("IDENTIFIER", DroolsLanguage.INSTANCE);
  IElementType STRING = new IElementType("STRING", DroolsLanguage.INSTANCE);
  IElementType NUMBER = new IElementType("NUMBER", DroolsLanguage.INSTANCE);
  IElementType COMMENT = new IElementType("COMMENT", DroolsLanguage.INSTANCE);
  IElementType OPERATOR = new IElementType("OPERATOR", DroolsLanguage.INSTANCE);
  IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
  IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;
  // Punctuation and delimiters
  IElementType LEFT_PAREN = new IElementType("LEFT_PAREN", DroolsLanguage.INSTANCE);
  IElementType RIGHT_PAREN = new IElementType("RIGHT_PAREN", DroolsLanguage.INSTANCE);
  IElementType LEFT_BRACE = new IElementType("LEFT_BRACE", DroolsLanguage.INSTANCE);
  IElementType RIGHT_BRACE = new IElementType("RIGHT_BRACE", DroolsLanguage.INSTANCE);
  IElementType LEFT_BRACKET = new IElementType("LEFT_BRACKET", DroolsLanguage.INSTANCE);
  IElementType RIGHT_BRACKET = new IElementType("RIGHT_BRACKET", DroolsLanguage.INSTANCE);
  IElementType SEMICOLON = new IElementType("SEMICOLON", DroolsLanguage.INSTANCE);
  IElementType COMMA = new IElementType("COMMA", DroolsLanguage.INSTANCE);
  IElementType DOT = new IElementType("DOT", DroolsLanguage.INSTANCE);
  IElementType COLON = new IElementType("COLON", DroolsLanguage.INSTANCE);
  IElementType DOLLAR = new IElementType("DOLLAR", DroolsLanguage.INSTANCE);

  // Token sets for grouping
  TokenSet KEYWORDS = TokenSet.create(KEYWORD);
  TokenSet COMMENTS = TokenSet.create(COMMENT);
  TokenSet STRINGS = TokenSet.create(STRING);
  TokenSet NUMBERS = TokenSet.create(NUMBER);
  TokenSet PUNCTUATION =
      TokenSet.create(
          LEFT_PAREN,
          RIGHT_PAREN,
          LEFT_BRACE,
          RIGHT_BRACE,
          LEFT_BRACKET,
          RIGHT_BRACKET,
          SEMICOLON,
          COMMA,
          DOT,
          COLON,
          DOLLAR);
  TokenSet BRACKETS =
      TokenSet.create(
          LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET);
}
