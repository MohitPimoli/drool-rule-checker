package com.plugin.drool.util;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/** Centralized constants for Drools language support */
public class DroolsConstants {
  private DroolsConstants() {}

  // Core Drools keywords
  public static final String[] KEYWORDS = {
    "rule",
    "when",
    "then",
    "end",
    "package",
    "import",
    "global",
    "function",
    "declare",
    "dialect",
    "salience",
    "no-loop",
    "ruleflow-group",
    "agenda-group",
    "auto-focus",
    "lock-on-active",
    "date-effective",
    "date-expires",
    "enabled",
    "duration"
  };

  // Drools built-in functions
  public static final String[] FUNCTIONS = {
    "insert", "insertLogical", "update", "modify", "retract", "delete", "drools", "kcontext"
  };

  // Drools operators
  public static final String[] OPERATORS = {
    "matches", "contains", "memberOf", "soundslike", "str",
    "in", "not in", "exists", "not exists", "forall",
    "from", "collect", "accumulate"
  };

  // Rule attributes
  public static final String[] ATTRIBUTES = {
    "salience",
    "no-loop",
    "ruleflow-group",
    "agenda-group",
    "auto-focus",
    "lock-on-active",
    "date-effective",
    "date-expires",
    "enabled",
    "duration",
    "timer"
  };

  // Rule templates for auto-completion
  public static final String[] RULE_TEMPLATES = {
    "rule \"Rule Name\"\nwhen\n    // conditions\nthen\n    // actions\nend",
    "rule \"Rule Name\"\n    salience 100\nwhen\n    // conditions\nthen\n    // actions\nend",
    "rule \"Rule Name\"\n    no-loop true\nwhen\n    // conditions\nthen\n    // actions\nend"
  };

  // Efficient keyword lookup
  public static final Set<String> KEYWORD_SET = new HashSet<>(Arrays.asList(KEYWORDS));
  public static final Set<String> FUNCTION_SET = new HashSet<>(Arrays.asList(FUNCTIONS));
  public static final Set<String> OPERATOR_SET = new HashSet<>(Arrays.asList(OPERATORS));

  // Combined keyword pattern for regex
  public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";

  /** Check if a word is a Drools keyword */
  public static boolean isKeyword(String word) {
    return KEYWORD_SET.contains(word);
  }

  /** Check if a word is a Drools function */
  public static boolean isFunction(String word) {
    return FUNCTION_SET.contains(word);
  }

  /** Check if a word is a Drools operator */
  public static boolean isOperator(String word) {
    return OPERATOR_SET.contains(word);
  }

  // Common Java classes that don't need imports (java.lang package)
  public static final String[] JAVA_LANG_CLASSES = {
    "String",
    "Integer",
    "Long",
    "Double",
    "Float",
    "Boolean",
    "Character",
    "Byte",
    "Short",
    "Object",
    "Class",
    "System",
    "Math",
    "Thread",
    "Runnable",
    "Exception",
    "RuntimeException",
    "IllegalArgumentException",
    "NullPointerException",
    "StringBuilder",
    "StringBuffer"
  };

  // Drools built-in classes and types that don't need imports
  public static final String[] DROOLS_BUILTIN_CLASSES = {
    "List",
    "Map",
    "Set",
    "Date",
    "BigDecimal",
    "BigInteger",
    "Calendar",
    "Collection",
    "ArrayList",
    "HashMap",
    "HashSet",
    "LinkedList",
    "TreeMap",
    "TreeSet",
    "Vector",
    "Properties",
    "Locale",
    "TimeZone",
    "SimpleDateFormat",
    "DecimalFormat",
    // Drools specific classes that are commonly available
    "KnowledgeHelper",
    "WorkingMemory",
    "StatefulKnowledgeSession",
    "StatelessKnowledgeSession",
    "KnowledgeBase",
    "KnowledgeBuilder",
    "KnowledgeBuilderFactory",
    "KnowledgeBaseFactory",
    "ResourceFactory",
    "ResourceType",
    "KnowledgeAgent",
    "KnowledgeAgentFactory"
  };

  // Common words that appear in Drools but aren't class names
  public static final String[] DROOLS_SYNTAX_WORDS = {
    "Rule",
    "When",
    "Then",
    "End",
    "Package",
    "Import",
    "Global",
    "Function",
    "Declare",
    "Dialect",
    "Salience",
    "Loop",
    "Group",
    "Focus",
    "Active",
    "Effective",
    "Expires",
    "Enabled",
    "Duration",
    "Timer",
    "Name",
    "Value",
    "Type",
    "Field",
    "Method",
    "Action",
    "Condition",
    "Expression",
    "Status",
    "Active",
    "Inactive",
    "True",
    "False",
    "Null",
    "Age",
    "Price",
    "Total",
    "Count",
    "Size",
    "Length",
    "Width",
    "Height",
    "Weight",
    "Customer",
    "Order",
    "Product",
    "Item",
    "User",
    "Account",
    "Transaction",
    "Payment"
  };

  public static final Set<String> JAVA_LANG_SET = new HashSet<>(Arrays.asList(JAVA_LANG_CLASSES));
  public static final Set<String> DROOLS_BUILTIN_SET =
      new HashSet<>(Arrays.asList(DROOLS_BUILTIN_CLASSES));
  public static final Set<String> DROOLS_SYNTAX_SET =
      new HashSet<>(Arrays.asList(DROOLS_SYNTAX_WORDS));

  /** Check if a class is from java.lang package (doesn't need import) */
  public static boolean isJavaLangClass(String className) {
    return JAVA_LANG_SET.contains(className);
  }

  /** Check if a class is a Drools built-in class (doesn't need import) */
  public static boolean isDroolsBuiltinClass(String className) {
    return DROOLS_BUILTIN_SET.contains(className);
  }

  /** Check if a word is Drools syntax (not a class name) */
  public static boolean isDroolsSyntaxWord(String word) {
    return DROOLS_SYNTAX_SET.contains(word);
  }

  /** Check if a class should be excluded from import validation */
  public static boolean shouldSkipImportValidation(String className) {
    return isJavaLangClass(className)
        || isDroolsBuiltinClass(className)
        || isDroolsSyntaxWord(className)
        || isKeyword(className.toLowerCase())
        || isFunction(className.toLowerCase())
        || isOperator(className.toLowerCase());
  }

  public static final String RULE_SPACE = "rule ";
  public static final String IMPORT = "import ";
}
