package com.plugin.drool;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.plugin.drool.util.DroolsConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.plugin.drool.util.Pattern.COMMENT_REGEX;
import static com.plugin.drool.util.Pattern.IDENTIFIER_REGEX;
import static com.plugin.drool.util.Pattern.NUMBER_REGEX;
import static com.plugin.drool.util.Pattern.OPERATOR_REGEX;
import static com.plugin.drool.util.Pattern.STRING_REGEX;
import static com.plugin.drool.util.Pattern.WHITESPACE_REGEX;

public class DroolsLexer extends LexerBase {
  private CharSequence buffer;
  private int endOffset;
  private int currentOffset;
  private IElementType currentTokenType;
  private int currentTokenStart;
  private int currentTokenEnd;

  // Use centralized patterns
  private static final Pattern KEYWORD_PATTERN = Pattern.compile(DroolsConstants.KEYWORD_PATTERN);
  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER_REGEX);
  private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
  private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);
  private static final Pattern COMMENT_PATTERN = Pattern.compile(COMMENT_REGEX);
  private static final Pattern WHITESPACE_PATTERN = Pattern.compile(WHITESPACE_REGEX);
  private static final Pattern OPERATOR_PATTERN = Pattern.compile(OPERATOR_REGEX);

  @Override
  public void start(
      @NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    this.buffer = buffer;
    this.endOffset = endOffset;
    this.currentOffset = startOffset;
    advance();
  }

  @Override
  public int getState() {
    return 0;
  }

  @Nullable
  @Override
  public IElementType getTokenType() {
    return currentTokenType;
  }

  @Override
  public int getTokenStart() {
    return currentTokenStart;
  }

  @Override
  public int getTokenEnd() {
    return currentTokenEnd;
  }

  @Override
  public void advance() {
    if (currentOffset >= endOffset) {
      currentTokenType = null;
      return;
    }

    currentTokenStart = currentOffset;
    CharSequence text = buffer.subSequence(currentOffset, endOffset);

    // Check for whitespace
    Matcher matcher = WHITESPACE_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.WHITE_SPACE;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for comments
    matcher = COMMENT_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.COMMENT;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for strings
    matcher = STRING_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.STRING;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for numbers
    matcher = NUMBER_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.NUMBER;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for keywords (using centralized list)
    matcher = KEYWORD_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.KEYWORD;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for identifiers
    matcher = IDENTIFIER_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.IDENTIFIER;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for operators
    matcher = OPERATOR_PATTERN.matcher(text);
    if (matcher.lookingAt()) {
      currentTokenEnd = currentOffset + matcher.end();
      currentTokenType = DroolsTokenTypes.OPERATOR;
      currentOffset = currentTokenEnd;
      return;
    }

    // Check for specific punctuation characters
    char currentChar = text.charAt(0);
    switch (currentChar) {
      case '(':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.LEFT_PAREN;
        currentOffset = currentTokenEnd;
        return;
      case ')':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.RIGHT_PAREN;
        currentOffset = currentTokenEnd;
        return;
      case '{':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.LEFT_BRACE;
        currentOffset = currentTokenEnd;
        return;
      case '}':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.RIGHT_BRACE;
        currentOffset = currentTokenEnd;
        return;
      case '[':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.LEFT_BRACKET;
        currentOffset = currentTokenEnd;
        return;
      case ']':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.RIGHT_BRACKET;
        currentOffset = currentTokenEnd;
        return;
      case ';':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.SEMICOLON;
        currentOffset = currentTokenEnd;
        return;
      case ',':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.COMMA;
        currentOffset = currentTokenEnd;
        return;
      case '.':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.DOT;
        currentOffset = currentTokenEnd;
        return;
      case ':':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.COLON;
        currentOffset = currentTokenEnd;
        return;
      case '$':
        currentTokenEnd = currentOffset + 1;
        currentTokenType = DroolsTokenTypes.DOLLAR;
        currentOffset = currentTokenEnd;
        return;
      default:
    }

    // Single character token (unrecognized)
    currentTokenEnd = currentOffset + 1;
    currentTokenType = DroolsTokenTypes.BAD_CHARACTER;
    currentOffset = currentTokenEnd;
  }

  @NotNull
  @Override
  public CharSequence getBufferSequence() {
    return buffer;
  }

  @Override
  public int getBufferEnd() {
    return endOffset;
  }
}
