package com.plugin.drool.util;

public class Pattern {
  private Pattern() {}

  public static final String RULE_BLOCK = "rule\\s+\"([^\"]+)\"[\\s\\S]*?\\bend\\b";
  public static final String COMMENT_BLOCK = "/\\*[\\s\\S]*?\\*/";
  public static final String SINGLE_LINE_COMMENT = "(//.*(?:\\r?\\n\\s*//.*)*)";
  public static final String IDENTIFIER_REGEX = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
  public static final String STRING_REGEX = "\"([^\"\\\\]|\\\\.)*\"";
  public static final String NUMBER_REGEX = "\\b\\d+(\\.\\d+)?\\b";
  public static final String COMMENT_REGEX = "//.*|/\\*[\\s\\S]*?\\*/";
  public static final String WHITESPACE_REGEX = "\\s+";
  public static final String OPERATOR_REGEX = "[+\\-*/=<>!&|]+";
  public static final String RULE_BLOCK_REGEX = "rule\\s+\"([^\"]+)\"";
  public static final String RULE_KEYWORD_REGEX = ".*\\brule\\s+.*";
  public static final String WHEN_KEYWORD_REGEX = ".*\\bwhen\\b.*";
  public static final String THEN_KEYWORD_REGEX = ".*\\bthen\\b.*";
  public static final String END_KEYWORD_REGEX = ".*\\bend\\b.*";
  public static final String COMPLETE_RULE_REGEX =
      "rule\\s+\"[^\"]*\"\\s+when\\s+.*?then\\s+.*?end";
  public static final String COMPLETE_RULE_BLOCK =
      "rule\\s+(?:\"([^\"]+)\"|([\\w\\-_]+))\\s*"
          + "(?:extends\\s+\"[^\"]+\"\\s*)?"
          + "(?:salience\\s+\\d+\\s*)?"
          + "(?:no-loop\\s*true\\s*)?"
          + "(?:agenda-group\\s+\"[^\"]+\"\\s*)?"
          + "when\\s+"
          + "[\\s\\S]*?"
          + "then\\s+"
          + "[\\s\\S]*?"
          + "\\bend\\b";
  // Keep these for more granular validation
  public static final String RULE_NAME_REGEX = "rule\\s+(?:\"([^\"]*)\"|([\\w\\-_]+))";
  public static final String WHEN_THEN_BLOCK_REGEX = "when\\s+[\\s\\S]*?then";
  public static final String END_BLOCK_REGEX = "\\bend\\b";
  public static final String TYPO_REGEX = "\\b(wen|thn|edn|ruel)\\b";

  // Additional patterns for individual components
  public static final String WHEN_CLAUSE_REGEX = "when\\s+[\\s\\S]*?(?=then)";
  public static final String THEN_CLAUSE_REGEX = "then\\s+[\\s\\S]*?(?=end)";
  public static final String RULE_ATTRIBUTES_REGEX =
      "(?:salience|no-loop|agenda-group|extends)\\s+[^\\n]*";

}
