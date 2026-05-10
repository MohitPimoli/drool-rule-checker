package com.plugin.drool.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.plugin.drool.DroolsTokenTypes;

%%

%class _DroolsLexer
%public
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state STRING
%state BLOCK_COMMENT

%{
  private StringBuilder stringBuilder = new StringBuilder();
%}

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
  {AnyWhiteSpace}                    { return DroolsTokenTypes.WHITE_SPACE; }

  // Single-line comments
  {SingleLineComment}                { return DroolsTokenTypes.COMMENT; }

  // Multi-line comment start
  "/*"                               { yybegin(BLOCK_COMMENT); return DroolsTokenTypes.COMMENT; }

  // String literal start
  \"                                 { yybegin(STRING); return DroolsTokenTypes.STRING; }

  // Drools keywords - core structure
  "rule"                             { return DroolsTokenTypes.KEYWORD; }
  "when"                             { return DroolsTokenTypes.KEYWORD; }
  "then"                             { return DroolsTokenTypes.KEYWORD; }
  "end"                              { return DroolsTokenTypes.KEYWORD; }
  "package"                          { return DroolsTokenTypes.KEYWORD; }
  "import"                           { return DroolsTokenTypes.KEYWORD; }
  "global"                           { return DroolsTokenTypes.KEYWORD; }
  "function"                         { return DroolsTokenTypes.KEYWORD; }
  "declare"                          { return DroolsTokenTypes.KEYWORD; }
  "query"                            { return DroolsTokenTypes.KEYWORD; }

  // Drools keywords - rule attributes
  "salience"                         { return DroolsTokenTypes.KEYWORD; }
  "no-loop"                          { return DroolsTokenTypes.KEYWORD; }
  "ruleflow-group"                   { return DroolsTokenTypes.KEYWORD; }
  "agenda-group"                     { return DroolsTokenTypes.KEYWORD; }
  "auto-focus"                       { return DroolsTokenTypes.KEYWORD; }
  "lock-on-active"                   { return DroolsTokenTypes.KEYWORD; }
  "date-effective"                   { return DroolsTokenTypes.KEYWORD; }
  "date-expires"                     { return DroolsTokenTypes.KEYWORD; }
  "enabled"                          { return DroolsTokenTypes.KEYWORD; }
  "duration"                         { return DroolsTokenTypes.KEYWORD; }
  "timer"                            { return DroolsTokenTypes.KEYWORD; }
  "dialect"                          { return DroolsTokenTypes.KEYWORD; }
  "extends"                          { return DroolsTokenTypes.KEYWORD; }

  // Drools keywords - conditional
  "and"                              { return DroolsTokenTypes.KEYWORD; }
  "or"                               { return DroolsTokenTypes.KEYWORD; }
  "not"                              { return DroolsTokenTypes.KEYWORD; }
  "exists"                           { return DroolsTokenTypes.KEYWORD; }
  "forall"                           { return DroolsTokenTypes.KEYWORD; }
  "from"                             { return DroolsTokenTypes.KEYWORD; }
  "collect"                          { return DroolsTokenTypes.KEYWORD; }
  "accumulate"                       { return DroolsTokenTypes.KEYWORD; }
  "eval"                             { return DroolsTokenTypes.KEYWORD; }

  // Drools keywords - action functions
  "insert"                           { return DroolsTokenTypes.KEYWORD; }
  "insertLogical"                    { return DroolsTokenTypes.KEYWORD; }
  "update"                           { return DroolsTokenTypes.KEYWORD; }
  "modify"                           { return DroolsTokenTypes.KEYWORD; }
  "retract"                          { return DroolsTokenTypes.KEYWORD; }
  "delete"                           { return DroolsTokenTypes.KEYWORD; }

  // Drools keywords - operators
  "matches"                          { return DroolsTokenTypes.KEYWORD; }
  "contains"                         { return DroolsTokenTypes.KEYWORD; }
  "memberOf"                         { return DroolsTokenTypes.KEYWORD; }
  "soundslike"                       { return DroolsTokenTypes.KEYWORD; }
  "str"                              { return DroolsTokenTypes.KEYWORD; }
  "in"                               { return DroolsTokenTypes.KEYWORD; }

  // Java/MVEL keywords commonly used in Drools
  "true"                             { return DroolsTokenTypes.KEYWORD; }
  "false"                            { return DroolsTokenTypes.KEYWORD; }
  "null"                             { return DroolsTokenTypes.KEYWORD; }
  "new"                              { return DroolsTokenTypes.KEYWORD; }
  "if"                               { return DroolsTokenTypes.KEYWORD; }
  "else"                             { return DroolsTokenTypes.KEYWORD; }
  "return"                           { return DroolsTokenTypes.KEYWORD; }
  "this"                             { return DroolsTokenTypes.KEYWORD; }

  // Binding variable prefix ($identifier)
  "$"{Identifier}                    { return DroolsTokenTypes.IDENTIFIER; }

  // Dollar sign alone
  "$"                                { return DroolsTokenTypes.DOLLAR; }

  // Identifiers (must come after keywords)
  {Identifier}                       { return DroolsTokenTypes.IDENTIFIER; }

  // Number literals
  {NumberLiteral}                    { return DroolsTokenTypes.NUMBER; }

  // Multi-character operators
  "=="                               { return DroolsTokenTypes.OPERATOR; }
  "!="                               { return DroolsTokenTypes.OPERATOR; }
  ">="                               { return DroolsTokenTypes.OPERATOR; }
  "<="                               { return DroolsTokenTypes.OPERATOR; }
  "&&"                               { return DroolsTokenTypes.OPERATOR; }
  "||"                               { return DroolsTokenTypes.OPERATOR; }
  "->"                               { return DroolsTokenTypes.OPERATOR; }
  "+="                               { return DroolsTokenTypes.OPERATOR; }
  "-="                               { return DroolsTokenTypes.OPERATOR; }
  "*="                               { return DroolsTokenTypes.OPERATOR; }
  "/="                               { return DroolsTokenTypes.OPERATOR; }

  // Single-character operators
  "="                                { return DroolsTokenTypes.OPERATOR; }
  ">"                                { return DroolsTokenTypes.OPERATOR; }
  "<"                                { return DroolsTokenTypes.OPERATOR; }
  "!"                                { return DroolsTokenTypes.OPERATOR; }
  "+"                                { return DroolsTokenTypes.OPERATOR; }
  "-"                                { return DroolsTokenTypes.OPERATOR; }
  "*"                                { return DroolsTokenTypes.OPERATOR; }
  "/"                                { return DroolsTokenTypes.OPERATOR; }
  "&"                                { return DroolsTokenTypes.OPERATOR; }
  "|"                                { return DroolsTokenTypes.OPERATOR; }
  "~"                                { return DroolsTokenTypes.OPERATOR; }
  "^"                                { return DroolsTokenTypes.OPERATOR; }
  "%"                                { return DroolsTokenTypes.OPERATOR; }
  "?"                                { return DroolsTokenTypes.OPERATOR; }

  // Punctuation and delimiters
  "("                                { return DroolsTokenTypes.LEFT_PAREN; }
  ")"                                { return DroolsTokenTypes.RIGHT_PAREN; }
  "{"                                { return DroolsTokenTypes.LEFT_BRACE; }
  "}"                                { return DroolsTokenTypes.RIGHT_BRACE; }
  "["                                { return DroolsTokenTypes.LEFT_BRACKET; }
  "]"                                { return DroolsTokenTypes.RIGHT_BRACKET; }
  ";"                                { return DroolsTokenTypes.SEMICOLON; }
  ","                                { return DroolsTokenTypes.COMMA; }
  "."                                { return DroolsTokenTypes.DOT; }
  ":"                                { return DroolsTokenTypes.COLON; }
  "@"                                { return DroolsTokenTypes.OPERATOR; }
  "#"                                { return DroolsTokenTypes.OPERATOR; }

  // Any other character is a bad character
  [^]                                { return DroolsTokenTypes.BAD_CHARACTER; }
}

<STRING> {
  // End of string
  \"                                 { yybegin(YYINITIAL); return DroolsTokenTypes.STRING; }

  // Escape sequences
  \\\"                               { return DroolsTokenTypes.STRING; }
  \\\\                               { return DroolsTokenTypes.STRING; }
  \\n                                { return DroolsTokenTypes.STRING; }
  \\r                                { return DroolsTokenTypes.STRING; }
  \\t                                { return DroolsTokenTypes.STRING; }
  \\b                                { return DroolsTokenTypes.STRING; }
  \\f                                { return DroolsTokenTypes.STRING; }
  \\[0-3]?[0-7]{1,2}                { return DroolsTokenTypes.STRING; }
  \\u[0-9a-fA-F]{4}                 { return DroolsTokenTypes.STRING; }

  // Any other escape (invalid but still part of string)
  \\.                                { return DroolsTokenTypes.STRING; }

  // String content (anything except quote, backslash, or newline)
  [^\"\\\r\n]+                       { return DroolsTokenTypes.STRING; }

  // Unterminated string at end of line
  {LineTerminator}                   { yybegin(YYINITIAL); return DroolsTokenTypes.BAD_CHARACTER; }
}

<BLOCK_COMMENT> {
  // End of block comment
  "*/"                               { yybegin(YYINITIAL); return DroolsTokenTypes.COMMENT; }

  // Comment content
  [^*]+                              { return DroolsTokenTypes.COMMENT; }
  "*"                                { return DroolsTokenTypes.COMMENT; }
}
