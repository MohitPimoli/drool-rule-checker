package com.plugin.drool.util;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Centralized constants for Drools language support
 */
public class DroolsConstants {
    private DroolsConstants(){}

    // Core Drools keywords
    public static final String[] KEYWORDS = {
            "rule", "when", "then", "end", "package", "import", "global",
            "function", "declare", "dialect", "salience", "no-loop",
            "ruleflow-group", "agenda-group", "auto-focus", "lock-on-active",
            "date-effective", "date-expires", "enabled", "duration"
    };

    // Drools built-in functions
    public static final String[] FUNCTIONS = {
            "insert", "insertLogical", "update", "modify", "retract", "delete",
            "drools", "kcontext"
    };

    // Drools operators
    public static final String[] OPERATORS = {
            "matches", "contains", "memberOf", "soundslike", "str",
            "in", "not in", "exists", "not exists", "forall",
            "from", "collect", "accumulate"
    };

    // Rule attributes
    public static final String[] ATTRIBUTES = {
            "salience", "no-loop", "ruleflow-group", "agenda-group",
            "auto-focus", "lock-on-active", "date-effective",
            "date-expires", "enabled", "duration", "timer"
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

    /**
     * Check if a word is a Drools keyword
     */
    public static boolean isKeyword(String word) {
        return KEYWORD_SET.contains(word);
    }

    /**
     * Check if a word is a Drools function
     */
    public static boolean isFunction(String word) {
        return FUNCTION_SET.contains(word);
    }

    /**
     * Check if a word is a Drools operator
     */
    public static boolean isOperator(String word) {
        return OPERATOR_SET.contains(word);
    }
}