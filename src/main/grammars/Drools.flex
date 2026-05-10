package com.plugin.drool.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.plugin.drool.psi.DroolsTypes;

%%

%class _DroolsLexer
%public
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state BLOCK_COMMENT

// Helpers
LineTerminator = \r|\n|\r\n
WhiteSpace = [ \t\f]+
NewLine = {LineTerminator}
AnyWhiteSpace = ({WhiteSpace}|{NewLine})+

// Identifiers
Letter = [a-zA-Z_]
LetterOrDigit = [a-zA-Z0-9_]
Identifier = {Letter}{LetterOrDigit}*

// Numbers
Digit = [0-9]
IntegerLiteral = {Digit}+
DecimalLiteral = {Digit}+\.{Digit}+
NumberLiteral = {DecimalLiteral}|{IntegerLiteral}

// Single-line comment
SingleLineComment = "//" [^\r\n]*

%%

<YYINITIAL> {
  // Whitespace
  {AnyWhiteSpace}                    { return TokenType.WHITE_SPACE; }

  // Single-line comments
  {SingleLineComment}                { return DroolsTypes.COMMENT; }

  // Multi-line comment start
  "/*"                               { yybegin(BLOCK_COMMENT); return DroolsTypes.COMMENT; }

  // Complete string literal (single line)
  \"([^\"\\\r\n]|\\.)*\"            { return DroolsTypes.STRING; }

  // Unterminated string (no closing quote before end of line) - still return as STRING for error recovery
  \"([^\"\\\r\n]|\\.)*              { return DroolsTypes.STRING; }

  // Drools keywords - core structure
  "rule"                             { return DroolsTypes.RULE_KEYWORD; }
  "when"                             { return DroolsTypes.WHEN_KEYWORD; }
  "then"                             { return DroolsTypes.THEN_KEYWORD; }
  "end"                              { return DroolsTypes.END_KEYWORD; }
  "package"                          { return DroolsTypes.PACKAGE_KEYWORD; }
  "import"                           { return DroolsTypes.IMPORT_KEYWORD; }
  "global"                           { return DroolsTypes.GLOBAL_KEYWORD; }
  "function"                         { return DroolsTypes.FUNCTION_KEYWORD; }
  "declare"                          { return DroolsTypes.DECLARE_KEYWORD; }
  "query"                            { return DroolsTypes.QUERY_KEYWORD; }

  // Drools keywords - rule attributes
  "salience"                         { return DroolsTypes.SALIENCE_KEYWORD; }
  "no-loop"                          { return DroolsTypes.NO_LOOP_KEYWORD; }
  "ruleflow-group"                   { return DroolsTypes.RULEFLOW_GROUP_KEYWORD; }
  "agenda-group"                     { return DroolsTypes.AGENDA_GROUP_KEYWORD; }
  "auto-focus"                       { return DroolsTypes.AUTO_FOCUS_KEYWORD; }
  "lock-on-active"                   { return DroolsTypes.LOCK_ON_ACTIVE_KEYWORD; }
  "date-effective"                   { return DroolsTypes.DATE_EFFECTIVE_KEYWORD; }
  "date-expires"                     { return DroolsTypes.DATE_EXPIRES_KEYWORD; }
  "enabled"                          { return DroolsTypes.ENABLED_KEYWORD; }
  "duration"                         { return DroolsTypes.DURATION_KEYWORD; }
  "timer"                            { return DroolsTypes.TIMER_KEYWORD; }
  "dialect"                          { return DroolsTypes.DIALECT_KEYWORD; }
  "extends"                          { return DroolsTypes.EXTENDS_KEYWORD; }

  // Drools keywords - conditional
  "and"                              { return DroolsTypes.AND_KEYWORD; }
  "or"                               { return DroolsTypes.OR_KEYWORD; }
  "not"                              { return DroolsTypes.NOT_KEYWORD; }
  "exists"                           { return DroolsTypes.EXISTS_KEYWORD; }
  "forall"                           { return DroolsTypes.FORALL_KEYWORD; }
  "from"                             { return DroolsTypes.FROM_KEYWORD; }
  "collect"                          { return DroolsTypes.COLLECT_KEYWORD; }
  "accumulate"                       { return DroolsTypes.ACCUMULATE_KEYWORD; }
  "eval"                             { return DroolsTypes.EVAL_KEYWORD; }

  // Drools keywords - action functions
  "insert"                           { return DroolsTypes.INSERT_KEYWORD; }
  "insertLogical"                    { return DroolsTypes.INSERT_LOGICAL_KEYWORD; }
  "update"                           { return DroolsTypes.UPDATE_KEYWORD; }
  "modify"                           { return DroolsTypes.MODIFY_KEYWORD; }
  "retract"                          { return DroolsTypes.RETRACT_KEYWORD; }
  "delete"                           { return DroolsTypes.DELETE_KEYWORD; }

  // Drools keywords - operators
  "matches"                          { return DroolsTypes.MATCHES_KEYWORD; }
  "contains"                         { return DroolsTypes.CONTAINS_KEYWORD; }
  "memberOf"                         { return DroolsTypes.MEMBER_OF_KEYWORD; }
  "soundslike"                       { return DroolsTypes.SOUNDSLIKE_KEYWORD; }
  "str"                              { return DroolsTypes.STR_KEYWORD; }
  "in"                               { return DroolsTypes.IN_KEYWORD; }

  // Java/MVEL keywords commonly used in Drools
  "true"                             { return DroolsTypes.TRUE_KEYWORD; }
  "false"                            { return DroolsTypes.FALSE_KEYWORD; }
  "null"                             { return DroolsTypes.NULL_KEYWORD; }
  "new"                              { return DroolsTypes.NEW_KEYWORD; }
  "if"                               { return DroolsTypes.IF_KEYWORD; }
  "else"                             { return DroolsTypes.ELSE_KEYWORD; }
  "return"                           { return DroolsTypes.RETURN_KEYWORD; }
  "this"                             { return DroolsTypes.THIS_KEYWORD; }

  // Dollar sign
  "$"                                { return DroolsTypes.DOLLAR; }

  // Identifiers (must come after keywords)
  {Identifier}                       { return DroolsTypes.IDENTIFIER; }

  // Number literals
  {NumberLiteral}                    { return DroolsTypes.NUMBER; }

  // Multi-character operators
  "=="                               { return DroolsTypes.OPERATOR; }
  "!="                               { return DroolsTypes.OPERATOR; }
  ">="                               { return DroolsTypes.OPERATOR; }
  "<="                               { return DroolsTypes.OPERATOR; }
  "&&"                               { return DroolsTypes.OPERATOR; }
  "||"                               { return DroolsTypes.OPERATOR; }
  "->"                               { return DroolsTypes.OPERATOR; }
  "+="                               { return DroolsTypes.OPERATOR; }
  "-="                               { return DroolsTypes.OPERATOR; }
  "*="                               { return DroolsTypes.OPERATOR; }
  "/="                               { return DroolsTypes.OPERATOR; }

  // Single-character operators
  "="                                { return DroolsTypes.OPERATOR; }
  ">"                                { return DroolsTypes.OPERATOR; }
  "<"                                { return DroolsTypes.OPERATOR; }
  "!"                                { return DroolsTypes.OPERATOR; }
  "+"                                { return DroolsTypes.OPERATOR; }
  "-"                                { return DroolsTypes.OPERATOR; }
  "*"                                { return DroolsTypes.OPERATOR; }
  "/"                                { return DroolsTypes.OPERATOR; }
  "&"                                { return DroolsTypes.OPERATOR; }
  "|"                                { return DroolsTypes.OPERATOR; }
  "~"                                { return DroolsTypes.OPERATOR; }
  "^"                                { return DroolsTypes.OPERATOR; }
  "%"                                { return DroolsTypes.OPERATOR; }
  "?"                                { return DroolsTypes.OPERATOR; }

  // Punctuation and delimiters
  "("                                { return DroolsTypes.LEFT_PAREN; }
  ")"                                { return DroolsTypes.RIGHT_PAREN; }
  "{"                                { return DroolsTypes.LEFT_BRACE; }
  "}"                                { return DroolsTypes.RIGHT_BRACE; }
  "["                                { return DroolsTypes.LEFT_BRACKET; }
  "]"                                { return DroolsTypes.RIGHT_BRACKET; }
  ";"                                { return DroolsTypes.SEMICOLON; }
  ","                                { return DroolsTypes.COMMA; }
  "."                                { return DroolsTypes.DOT; }
  ":"                                { return DroolsTypes.COLON; }
  "@"                                { return DroolsTypes.OPERATOR; }
  "#"                                { return DroolsTypes.OPERATOR; }

  // Any other character is a bad character
  [^]                                { return TokenType.BAD_CHARACTER; }
}

<BLOCK_COMMENT> {
  // End of block comment
  "*/"                               { yybegin(YYINITIAL); return DroolsTypes.COMMENT; }

  // Comment content
  [^*]+                              { return DroolsTypes.COMMENT; }
  "*"                                { return DroolsTypes.COMMENT; }
}
