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
  public static final String RULE_NAME_REGEX = "rule\\s+(?:\"([^\"]*)\"|([\\w\\-_]+))";
  public static final String WHEN_THEN_BLOCK_REGEX = "when\\s+[\\s\\S]*?then";
  public static final String END_BLOCK_REGEX = "\\bend\\b";
  public static final String TYPO_REGEX = "\\b(wen|thn|edn|ruel)\\b";
  public static final String WHEN_CLAUSE_REGEX = "when\\s+[\\s\\S]*?(?=then)";
  public static final String THEN_CLAUSE_REGEX = "then\\s+[\\s\\S]*?(?=end)";
  public static final String RULE_ATTRIBUTES_REGEX =
      "(?:salience|no-loop|agenda-group|extends)\\s+[^\\n]*";
  public static final String PUNCTUATION_REGEX = "[(){}\\[\\];,.:$]";
  public static final String JAVA_STATEMENT_REGEX = "[^;{}]*;";
  public static final String FIELD_ACCESS_REGEX =
      "\\$[a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*";
  public static final String FUNCTION_CALL_REGEX = "[a-zA-Z_][a-zA-Z0-9_]*\\s*\\([^)]*\\)";
  // More specific constraint expression pattern for Drools (not Java assignments)
  public static final String CONSTRAINT_EXPRESSION_REGEX =
      "\\$?[a-zA-Z_][a-zA-Z0-9_]*\\s*[><=!]\\s*[^,);]+";
  // More conservative patterns to reduce false positives
  public static final String UNCLOSED_STRING_REGEX =
      "\"[^\"\\n]{10,}\\s*$"; // Only long unclosed strings
  public static final String INVALID_ESCAPE_REGEX = "\\\\[^\"\\\\nrtbf]";
  public static final String UNMATCHED_OPEN_PAREN = "\\([^)]{15,}$"; // Only longer unmatched
  public static final String UNMATCHED_CLOSE_PAREN = "^[^(]{15,}\\)";
  public static final String UNMATCHED_OPEN_BRACE = "\\{[^}]{15,}$";
  public static final String UNMATCHED_CLOSE_BRACE = "^[^{]{15,}\\}";
  public static final String UNMATCHED_OPEN_BRACKET = "\\[[^\\]]{15,}$";
  public static final String UNMATCHED_CLOSE_BRACKET = "^[^\\[]{15,}\\]";
  public static final String JAVA_CODE_BLOCK = "then\\s+([\\s\\S]*?)(?=\\s*end)";
  public static final String MISSING_SEMICOLON =
      "[a-zA-Z_$][a-zA-Z0-9_]*\\s*\\([^)]*\\)\\s*\\n\\s*[a-zA-Z_$]"; // More specific
  public static final String INCOMPLETE_STATEMENT =
      "(if|for|while)\\s*\\([^)]*\\)\\s*(?!\\{)[^;\\n]{5,}$"; // More specific
}
