package com.plugin.drool.util;


public class Pattern {
  private Pattern() {}

  public static final String RULE_BLOCK = "rule\\s+\"([^\"]+)\"[\\s\\S]*?end";
  public static final String COMMENT_BLOCK = "/\\*[\\s\\S]*?\\*/";
  public static final String SINGLE_LINE_COMMENT = "(//.*(?:\\r?\\n\\s*//.*)*)";
  public static final String IDENTIFIER_REGEX = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
  public static final String STRING_REGEX = "\"([^\"\\\\]|\\\\.)*\"";
  public static final String NUMBER_REGEX = "\\b\\d+(\\.\\d+)?\\b";
  public static final String COMMENT_REGEX = "//.*|/\\*[\\s\\S]*?\\*/";
  public static final String WHITESPACE_REGEX = "\\s+";
  public static final String OPERATOR_REGEX = "[+\\-*/=<>!&|]+";
  public static final String WHEN_THEN_BLOCK_REGEX = "when\\s+.*?then";
  public static final String RULE_BLOCK_REGEX = "rule\\s+\"([^\"]+)\"";
  public static final String END_BLOCK_REGEX = "end\\s*$";
  public static final String RULE_NAME_REGEX = "rule\\s+\"([^\"]*)(?:$|[^\"]*$)";
  public static final String TYPO_REGEX = "\\b(wen|thn|edn|ruel)\\b";
}
