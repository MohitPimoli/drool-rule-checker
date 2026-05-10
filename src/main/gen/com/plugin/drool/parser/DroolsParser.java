// This is a generated file. Not intended for manual editing.
package com.plugin.drool.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.plugin.drool.psi.DroolsTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DroolsParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return droolsFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // ACCUMULATE_KEYWORD LEFT_PAREN pattern COMMA accumulateFunction+ RIGHT_PAREN
  public static boolean accumElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "accumElement")) return false;
    if (!nextTokenIs(builder_, ACCUMULATE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACCUM_ELEMENT, null);
    result_ = consumeTokens(builder_, 1, ACCUMULATE_KEYWORD, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, pattern(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, COMMA)) && result_;
    result_ = pinned_ && report_error_(builder_, accumElement_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // accumulateFunction+
  private static boolean accumElement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "accumElement_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = accumulateFunction(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!accumulateFunction(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "accumElement_4", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER LEFT_PAREN expressionContent RIGHT_PAREN
  public static boolean accumulateFunction(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "accumulateFunction")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, LEFT_PAREN);
    result_ = result_ && expressionContent(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, ACCUMULATE_FUNCTION, result_);
    return result_;
  }

  /* ********************************************************** */
  // AGENDA_GROUP_KEYWORD attributeValue
  public static boolean agendaGroupAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "agendaGroupAttribute")) return false;
    if (!nextTokenIs(builder_, AGENDA_GROUP_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AGENDA_GROUP_ATTRIBUTE, null);
    result_ = consumeToken(builder_, AGENDA_GROUP_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // LEFT_BRACKET RIGHT_BRACKET
  public static boolean arrayBrackets(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrayBrackets")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, LEFT_BRACKET, RIGHT_BRACKET);
    exit_section_(builder_, marker_, ARRAY_BRACKETS, result_);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER | STRING | IDENTIFIER | TRUE_KEYWORD | FALSE_KEYWORD
  public static boolean attributeValue(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attributeValue")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTRIBUTE_VALUE, "<attribute value>");
    result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // AUTO_FOCUS_KEYWORD attributeValue?
  public static boolean autoFocusAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "autoFocusAttribute")) return false;
    if (!nextTokenIs(builder_, AUTO_FOCUS_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AUTO_FOCUS_ATTRIBUTE, null);
    result_ = consumeToken(builder_, AUTO_FOCUS_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && autoFocusAttribute_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // attributeValue?
  private static boolean autoFocusAttribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "autoFocusAttribute_1")) return false;
    attributeValue(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // bindingVariable COLON factPattern
  public static boolean bindingPattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bindingPattern")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = bindingVariable(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && factPattern(builder_, level_ + 1);
    exit_section_(builder_, marker_, BINDING_PATTERN, result_);
    return result_;
  }

  /* ********************************************************** */
  // DOLLAR IDENTIFIER
  public static boolean bindingVariable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bindingVariable")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOLLAR, IDENTIFIER);
    exit_section_(builder_, marker_, BINDING_VARIABLE, result_);
    return result_;
  }

  /* ********************************************************** */
  // blockContentToken+
  public static boolean blockContent(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContent")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_CONTENT, "<block content>");
    result_ = blockContentToken(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!blockContentToken(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "blockContent", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | NUMBER | STRING | OPERATOR | DOT | COMMA | COLON | DOLLAR | SEMICOLON
  //                              | LEFT_PAREN expressionContent? RIGHT_PAREN
  //                              | LEFT_BRACE blockContent? RIGHT_BRACE
  //                              | LEFT_BRACKET expressionContent? RIGHT_BRACKET
  //                              | NEW_KEYWORD | NULL_KEYWORD | TRUE_KEYWORD | FALSE_KEYWORD | THIS_KEYWORD
  //                              | NOT_KEYWORD | AND_KEYWORD | OR_KEYWORD | FROM_KEYWORD
  //                              | IF_KEYWORD | ELSE_KEYWORD | RETURN_KEYWORD
  //                              | INSERT_KEYWORD | INSERT_LOGICAL_KEYWORD | UPDATE_KEYWORD
  //                              | MODIFY_KEYWORD | RETRACT_KEYWORD | DELETE_KEYWORD
  //                              | MATCHES_KEYWORD | CONTAINS_KEYWORD | MEMBER_OF_KEYWORD
  //                              | IN_KEYWORD | EVAL_KEYWORD | COLLECT_KEYWORD | ACCUMULATE_KEYWORD
  static boolean blockContentToken(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = blockContentToken_9(builder_, level_ + 1);
    if (!result_) result_ = blockContentToken_10(builder_, level_ + 1);
    if (!result_) result_ = blockContentToken_11(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEW_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NULL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THIS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FROM_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ELSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETURN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_LOGICAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, UPDATE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MODIFY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETRACT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DELETE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MATCHES_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, CONTAINS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MEMBER_OF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, EVAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, COLLECT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ACCUMULATE_KEYWORD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean blockContentToken_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && blockContentToken_9_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean blockContentToken_9_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_9_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACE blockContent? RIGHT_BRACE
  private static boolean blockContentToken_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && blockContentToken_10_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // blockContent?
  private static boolean blockContentToken_10_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_10_1")) return false;
    blockContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACKET expressionContent? RIGHT_BRACKET
  private static boolean blockContentToken_11(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_11")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    result_ = result_ && blockContentToken_11_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean blockContentToken_11_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "blockContentToken_11_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOT IDENTIFIER)*
  public static boolean className(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "className")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && className_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, CLASS_NAME, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean className_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "className_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!className_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "className_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean className_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "className_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // notElement | existsElement | forallElement | evalElement | accumElement | fromElement
  public static boolean conditionalElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditionalElement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONDITIONAL_ELEMENT, "<conditional element>");
    result_ = notElement(builder_, level_ + 1);
    if (!result_) result_ = existsElement(builder_, level_ + 1);
    if (!result_) result_ = forallElement(builder_, level_ + 1);
    if (!result_) result_ = evalElement(builder_, level_ + 1);
    if (!result_) result_ = accumElement(builder_, level_ + 1);
    if (!result_) result_ = fromElement(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // constraintAtom (OPERATOR constraintAtom)*
  static boolean constraintAnd(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAnd")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = constraintAtom(builder_, level_ + 1);
    result_ = result_ && constraintAnd_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (OPERATOR constraintAtom)*
  private static boolean constraintAnd_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAnd_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!constraintAnd_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "constraintAnd_1", pos_)) break;
    }
    return true;
  }

  // OPERATOR constraintAtom
  private static boolean constraintAnd_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAnd_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR);
    result_ = result_ && constraintAtom(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // fieldAccessOrValue (constraintOperator fieldAccessOrValue)?
  //                          | LEFT_PAREN constraintOr RIGHT_PAREN
  static boolean constraintAtom(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAtom")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = constraintAtom_0(builder_, level_ + 1);
    if (!result_) result_ = constraintAtom_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // fieldAccessOrValue (constraintOperator fieldAccessOrValue)?
  private static boolean constraintAtom_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAtom_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = fieldAccessOrValue(builder_, level_ + 1);
    result_ = result_ && constraintAtom_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (constraintOperator fieldAccessOrValue)?
  private static boolean constraintAtom_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAtom_0_1")) return false;
    constraintAtom_0_1_0(builder_, level_ + 1);
    return true;
  }

  // constraintOperator fieldAccessOrValue
  private static boolean constraintAtom_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAtom_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = constraintOperator(builder_, level_ + 1);
    result_ = result_ && fieldAccessOrValue(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN constraintOr RIGHT_PAREN
  private static boolean constraintAtom_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintAtom_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && constraintOr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // constraintOr
  public static boolean constraintExpr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintExpr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONSTRAINT_EXPR, "<constraint expr>");
    result_ = constraintOr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::constraintExprRecover);
    return result_;
  }

  /* ********************************************************** */
  // !(COMMA | RIGHT_PAREN | <<eof>>)
  static boolean constraintExprRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintExprRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !constraintExprRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // COMMA | RIGHT_PAREN | <<eof>>
  private static boolean constraintExprRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintExprRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, RIGHT_PAREN);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // constraintExpr (COMMA constraintExpr)*
  public static boolean constraintList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintList")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONSTRAINT_LIST, "<constraint list>");
    result_ = constraintExpr(builder_, level_ + 1);
    result_ = result_ && constraintList_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA constraintExpr)*
  private static boolean constraintList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!constraintList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "constraintList_1", pos_)) break;
    }
    return true;
  }

  // COMMA constraintExpr
  private static boolean constraintList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && constraintExpr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // OPERATOR
  //                       | MATCHES_KEYWORD
  //                       | CONTAINS_KEYWORD
  //                       | MEMBER_OF_KEYWORD
  //                       | SOUNDSLIKE_KEYWORD
  //                       | STR_KEYWORD
  //                       | IN_KEYWORD
  //                       | NOT_KEYWORD IN_KEYWORD
  public static boolean constraintOperator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintOperator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONSTRAINT_OPERATOR, "<constraint operator>");
    result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, MATCHES_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, CONTAINS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MEMBER_OF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, SOUNDSLIKE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, STR_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IN_KEYWORD);
    if (!result_) result_ = parseTokens(builder_, 0, NOT_KEYWORD, IN_KEYWORD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // constraintAnd (OPERATOR constraintAnd)*
  static boolean constraintOr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintOr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = constraintAnd(builder_, level_ + 1);
    result_ = result_ && constraintOr_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (OPERATOR constraintAnd)*
  private static boolean constraintOr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintOr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!constraintOr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "constraintOr_1", pos_)) break;
    }
    return true;
  }

  // OPERATOR constraintAnd
  private static boolean constraintOr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constraintOr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR);
    result_ = result_ && constraintAnd(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // DATE_EFFECTIVE_KEYWORD attributeValue
  public static boolean dateEffectiveAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dateEffectiveAttribute")) return false;
    if (!nextTokenIs(builder_, DATE_EFFECTIVE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DATE_EFFECTIVE_ATTRIBUTE, null);
    result_ = consumeToken(builder_, DATE_EFFECTIVE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // DATE_EXPIRES_KEYWORD attributeValue
  public static boolean dateExpiresAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dateExpiresAttribute")) return false;
    if (!nextTokenIs(builder_, DATE_EXPIRES_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DATE_EXPIRES_ATTRIBUTE, null);
    result_ = consumeToken(builder_, DATE_EXPIRES_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // DECLARE_KEYWORD IDENTIFIER declareExtends? declareField* END_KEYWORD
  public static boolean declareBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareBlock")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DECLARE_BLOCK, "<declare block>");
    result_ = consumeTokens(builder_, 1, DECLARE_KEYWORD, IDENTIFIER);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, declareBlock_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, declareBlock_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, END_KEYWORD) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::declareBlockRecover);
    return result_ || pinned_;
  }

  // declareExtends?
  private static boolean declareBlock_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareBlock_2")) return false;
    declareExtends(builder_, level_ + 1);
    return true;
  }

  // declareField*
  private static boolean declareBlock_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareBlock_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!declareField(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "declareBlock_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>)
  static boolean declareBlockRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareBlockRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !declareBlockRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>
  private static boolean declareBlockRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareBlockRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // EXTENDS_KEYWORD className
  public static boolean declareExtends(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareExtends")) return false;
    if (!nextTokenIs(builder_, EXTENDS_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EXTENDS_KEYWORD);
    result_ = result_ && className(builder_, level_ + 1);
    exit_section_(builder_, marker_, DECLARE_EXTENDS, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON typeName (OPERATOR attributeValue)? SEMICOLON?
  public static boolean declareField(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareField")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DECLARE_FIELD, "<declare field>");
    result_ = consumeTokens(builder_, 0, IDENTIFIER, COLON);
    result_ = result_ && typeName(builder_, level_ + 1);
    result_ = result_ && declareField_3(builder_, level_ + 1);
    result_ = result_ && declareField_4(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::declareFieldRecover);
    return result_;
  }

  // (OPERATOR attributeValue)?
  private static boolean declareField_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareField_3")) return false;
    declareField_3_0(builder_, level_ + 1);
    return true;
  }

  // OPERATOR attributeValue
  private static boolean declareField_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareField_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR);
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean declareField_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareField_4")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // !(IDENTIFIER | END_KEYWORD | <<eof>>)
  static boolean declareFieldRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareFieldRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !declareFieldRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IDENTIFIER | END_KEYWORD | <<eof>>
  private static boolean declareFieldRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declareFieldRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // DIALECT_KEYWORD attributeValue
  public static boolean dialectAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dialectAttribute")) return false;
    if (!nextTokenIs(builder_, DIALECT_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DIALECT_ATTRIBUTE, null);
    result_ = consumeToken(builder_, DIALECT_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // packageDecl? importStatement* topLevelItem*
  static boolean droolsFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "droolsFile")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = droolsFile_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, droolsFile_1(builder_, level_ + 1));
    result_ = pinned_ && droolsFile_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // packageDecl?
  private static boolean droolsFile_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "droolsFile_0")) return false;
    packageDecl(builder_, level_ + 1);
    return true;
  }

  // importStatement*
  private static boolean droolsFile_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "droolsFile_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!importStatement(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "droolsFile_1", pos_)) break;
    }
    return true;
  }

  // topLevelItem*
  private static boolean droolsFile_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "droolsFile_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!topLevelItem(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "droolsFile_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // DURATION_KEYWORD attributeValue
  public static boolean durationAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "durationAttribute")) return false;
    if (!nextTokenIs(builder_, DURATION_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DURATION_ATTRIBUTE, null);
    result_ = consumeToken(builder_, DURATION_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // ENABLED_KEYWORD attributeValue
  public static boolean enabledAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enabledAttribute")) return false;
    if (!nextTokenIs(builder_, ENABLED_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENABLED_ATTRIBUTE, null);
    result_ = consumeToken(builder_, ENABLED_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // EVAL_KEYWORD LEFT_PAREN expressionContent RIGHT_PAREN
  public static boolean evalElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "evalElement")) return false;
    if (!nextTokenIs(builder_, EVAL_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EVAL_ELEMENT, null);
    result_ = consumeTokens(builder_, 1, EVAL_KEYWORD, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expressionContent(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // EXISTS_KEYWORD (LEFT_PAREN pattern+ RIGHT_PAREN | factPattern)
  public static boolean existsElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "existsElement")) return false;
    if (!nextTokenIs(builder_, EXISTS_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXISTS_ELEMENT, null);
    result_ = consumeToken(builder_, EXISTS_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && existsElement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // LEFT_PAREN pattern+ RIGHT_PAREN | factPattern
  private static boolean existsElement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "existsElement_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = existsElement_1_0(builder_, level_ + 1);
    if (!result_) result_ = factPattern(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN pattern+ RIGHT_PAREN
  private static boolean existsElement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "existsElement_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && existsElement_1_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pattern+
  private static boolean existsElement_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "existsElement_1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pattern(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pattern(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "existsElement_1_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // expressionToken+
  public static boolean expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPRESSION, "<expression>");
    result_ = expressionToken(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expressionToken(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expression", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // expressionContentToken+
  public static boolean expressionContent(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContent")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, EXPRESSION_CONTENT, "<expression content>");
    result_ = expressionContentToken(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expressionContentToken(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expressionContent", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | NUMBER | STRING | OPERATOR | DOT | COMMA | COLON | DOLLAR | SEMICOLON
  //                                   | LEFT_PAREN expressionContent? RIGHT_PAREN
  //                                   | LEFT_BRACE blockContent? RIGHT_BRACE
  //                                   | LEFT_BRACKET expressionContent? RIGHT_BRACKET
  //                                   | NEW_KEYWORD | NULL_KEYWORD | TRUE_KEYWORD | FALSE_KEYWORD | THIS_KEYWORD
  //                                   | NOT_KEYWORD | AND_KEYWORD | OR_KEYWORD | FROM_KEYWORD
  //                                   | IF_KEYWORD | ELSE_KEYWORD | RETURN_KEYWORD
  //                                   | INSERT_KEYWORD | INSERT_LOGICAL_KEYWORD | UPDATE_KEYWORD
  //                                   | MODIFY_KEYWORD | RETRACT_KEYWORD | DELETE_KEYWORD
  //                                   | MATCHES_KEYWORD | CONTAINS_KEYWORD | MEMBER_OF_KEYWORD
  //                                   | IN_KEYWORD | EVAL_KEYWORD | COLLECT_KEYWORD | ACCUMULATE_KEYWORD
  static boolean expressionContentToken(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = expressionContentToken_9(builder_, level_ + 1);
    if (!result_) result_ = expressionContentToken_10(builder_, level_ + 1);
    if (!result_) result_ = expressionContentToken_11(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEW_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NULL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THIS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FROM_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ELSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETURN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_LOGICAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, UPDATE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MODIFY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETRACT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DELETE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MATCHES_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, CONTAINS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MEMBER_OF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, EVAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, COLLECT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ACCUMULATE_KEYWORD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean expressionContentToken_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && expressionContentToken_9_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean expressionContentToken_9_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_9_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACE blockContent? RIGHT_BRACE
  private static boolean expressionContentToken_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && expressionContentToken_10_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // blockContent?
  private static boolean expressionContentToken_10_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_10_1")) return false;
    blockContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACKET expressionContent? RIGHT_BRACKET
  private static boolean expressionContentToken_11(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_11")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    result_ = result_ && expressionContentToken_11_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean expressionContentToken_11_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionContentToken_11_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER | NUMBER | STRING | OPERATOR | DOT | COMMA | COLON | DOLLAR | SEMICOLON
  //                            | LEFT_PAREN expressionContent? RIGHT_PAREN
  //                            | LEFT_BRACE blockContent? RIGHT_BRACE
  //                            | LEFT_BRACKET expressionContent? RIGHT_BRACKET
  //                            | NEW_KEYWORD | NULL_KEYWORD | TRUE_KEYWORD | FALSE_KEYWORD | THIS_KEYWORD
  //                            | NOT_KEYWORD | AND_KEYWORD | OR_KEYWORD
  static boolean expressionToken(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = expressionToken_9(builder_, level_ + 1);
    if (!result_) result_ = expressionToken_10(builder_, level_ + 1);
    if (!result_) result_ = expressionToken_11(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEW_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NULL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THIS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean expressionToken_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && expressionToken_9_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean expressionToken_9_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_9_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACE blockContent? RIGHT_BRACE
  private static boolean expressionToken_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && expressionToken_10_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // blockContent?
  private static boolean expressionToken_10_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_10_1")) return false;
    blockContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACKET expressionContent? RIGHT_BRACKET
  private static boolean expressionToken_11(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_11")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    result_ = result_ && expressionToken_11_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean expressionToken_11_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expressionToken_11_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // EXTENDS_KEYWORD attributeValue
  public static boolean extendsAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "extendsAttribute")) return false;
    if (!nextTokenIs(builder_, EXTENDS_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXTENDS_ATTRIBUTE, null);
    result_ = consumeToken(builder_, EXTENDS_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // className LEFT_PAREN constraintList? RIGHT_PAREN (logicalConnective pattern)?
  public static boolean factPattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPattern")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FACT_PATTERN, "<fact pattern>");
    result_ = className(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, factPattern_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RIGHT_PAREN)) && result_;
    result_ = pinned_ && factPattern_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::factPatternRecover);
    return result_ || pinned_;
  }

  // constraintList?
  private static boolean factPattern_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPattern_2")) return false;
    constraintList(builder_, level_ + 1);
    return true;
  }

  // (logicalConnective pattern)?
  private static boolean factPattern_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPattern_4")) return false;
    factPattern_4_0(builder_, level_ + 1);
    return true;
  }

  // logicalConnective pattern
  private static boolean factPattern_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPattern_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = logicalConnective(builder_, level_ + 1);
    result_ = result_ && pattern(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(THEN_KEYWORD | END_KEYWORD | AND_KEYWORD | OR_KEYWORD | NOT_KEYWORD | EXISTS_KEYWORD | FORALL_KEYWORD | IDENTIFIER | DOLLAR | LEFT_PAREN | SEMICOLON | <<eof>>)
  static boolean factPatternRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPatternRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !factPatternRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // THEN_KEYWORD | END_KEYWORD | AND_KEYWORD | OR_KEYWORD | NOT_KEYWORD | EXISTS_KEYWORD | FORALL_KEYWORD | IDENTIFIER | DOLLAR | LEFT_PAREN | SEMICOLON | <<eof>>
  private static boolean factPatternRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "factPatternRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, THEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, EXISTS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FORALL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, LEFT_PAREN);
    if (!result_) result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (DOLLAR? IDENTIFIER | THIS_KEYWORD) (DOT IDENTIFIER (LEFT_PAREN expressionContent? RIGHT_PAREN)?)*
  //                               | literal
  //                               | NEW_KEYWORD className LEFT_PAREN expressionContent? RIGHT_PAREN
  static boolean fieldAccessOrValue(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = fieldAccessOrValue_0(builder_, level_ + 1);
    if (!result_) result_ = literal(builder_, level_ + 1);
    if (!result_) result_ = fieldAccessOrValue_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (DOLLAR? IDENTIFIER | THIS_KEYWORD) (DOT IDENTIFIER (LEFT_PAREN expressionContent? RIGHT_PAREN)?)*
  private static boolean fieldAccessOrValue_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = fieldAccessOrValue_0_0(builder_, level_ + 1);
    result_ = result_ && fieldAccessOrValue_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DOLLAR? IDENTIFIER | THIS_KEYWORD
  private static boolean fieldAccessOrValue_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = fieldAccessOrValue_0_0_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, THIS_KEYWORD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DOLLAR? IDENTIFIER
  private static boolean fieldAccessOrValue_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = fieldAccessOrValue_0_0_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DOLLAR?
  private static boolean fieldAccessOrValue_0_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_0_0_0")) return false;
    consumeToken(builder_, DOLLAR);
    return true;
  }

  // (DOT IDENTIFIER (LEFT_PAREN expressionContent? RIGHT_PAREN)?)*
  private static boolean fieldAccessOrValue_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!fieldAccessOrValue_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "fieldAccessOrValue_0_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER (LEFT_PAREN expressionContent? RIGHT_PAREN)?
  private static boolean fieldAccessOrValue_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    result_ = result_ && fieldAccessOrValue_0_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (LEFT_PAREN expressionContent? RIGHT_PAREN)?
  private static boolean fieldAccessOrValue_0_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_1_0_2")) return false;
    fieldAccessOrValue_0_1_0_2_0(builder_, level_ + 1);
    return true;
  }

  // LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean fieldAccessOrValue_0_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && fieldAccessOrValue_0_1_0_2_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean fieldAccessOrValue_0_1_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_0_1_0_2_0_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  // NEW_KEYWORD className LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean fieldAccessOrValue_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NEW_KEYWORD);
    result_ = result_ && className(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && fieldAccessOrValue_2_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean fieldAccessOrValue_2_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fieldAccessOrValue_2_3")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // FORALL_KEYWORD LEFT_PAREN pattern+ RIGHT_PAREN
  public static boolean forallElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forallElement")) return false;
    if (!nextTokenIs(builder_, FORALL_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FORALL_ELEMENT, null);
    result_ = consumeTokens(builder_, 1, FORALL_KEYWORD, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, forallElement_2(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // pattern+
  private static boolean forallElement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forallElement_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pattern(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pattern(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "forallElement_2", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // factPattern FROM_KEYWORD expression
  public static boolean fromElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fromElement")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FROM_ELEMENT, null);
    result_ = factPattern(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, FROM_KEYWORD);
    pinned_ = result_; // pin = 2
    result_ = result_ && expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // LEFT_BRACE blockContent? RIGHT_BRACE
  public static boolean functionBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionBody")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && functionBody_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, FUNCTION_BODY, result_);
    return result_;
  }

  // blockContent?
  private static boolean functionBody_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionBody_1")) return false;
    blockContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // FUNCTION_KEYWORD typeName? IDENTIFIER LEFT_PAREN parameterList? RIGHT_PAREN functionBody
  public static boolean functionDef(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDef")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNCTION_DEF, "<function def>");
    result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, functionDef_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeTokens(builder_, -1, IDENTIFIER, LEFT_PAREN)) && result_;
    result_ = pinned_ && report_error_(builder_, functionDef_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RIGHT_PAREN)) && result_;
    result_ = pinned_ && functionBody(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::functionDefRecover);
    return result_ || pinned_;
  }

  // typeName?
  private static boolean functionDef_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDef_1")) return false;
    typeName(builder_, level_ + 1);
    return true;
  }

  // parameterList?
  private static boolean functionDef_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDef_4")) return false;
    parameterList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !(RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>)
  static boolean functionDefRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDefRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !functionDefRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>
  private static boolean functionDefRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "functionDefRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // GLOBAL_KEYWORD typeName IDENTIFIER SEMICOLON?
  public static boolean globalDecl(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "globalDecl")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GLOBAL_DECL, "<global decl>");
    result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, typeName(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, IDENTIFIER)) && result_;
    result_ = pinned_ && globalDecl_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::globalDeclRecover);
    return result_ || pinned_;
  }

  // SEMICOLON?
  private static boolean globalDecl_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "globalDecl_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // !(IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>)
  static boolean globalDeclRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "globalDeclRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !globalDeclRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>
  private static boolean globalDeclRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "globalDeclRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // qualifiedName (DOT OPERATOR)?
  public static boolean importPath(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importPath")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = qualifiedName(builder_, level_ + 1);
    result_ = result_ && importPath_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, IMPORT_PATH, result_);
    return result_;
  }

  // (DOT OPERATOR)?
  private static boolean importPath_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importPath_1")) return false;
    importPath_1_0(builder_, level_ + 1);
    return true;
  }

  // DOT OPERATOR
  private static boolean importPath_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importPath_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, OPERATOR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IMPORT_KEYWORD importPath SEMICOLON?
  public static boolean importStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importStatement")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPORT_STATEMENT, "<import statement>");
    result_ = consumeToken(builder_, IMPORT_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, importPath(builder_, level_ + 1));
    result_ = pinned_ && importStatement_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::importStatementRecover);
    return result_ || pinned_;
  }

  // SEMICOLON?
  private static boolean importStatement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importStatement_2")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // !(IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | PACKAGE_KEYWORD | <<eof>>)
  static boolean importStatementRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importStatementRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !importStatementRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | PACKAGE_KEYWORD | <<eof>>
  private static boolean importStatementRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "importStatementRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, PACKAGE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // javaStatementContent SEMICOLON
  public static boolean javaStatement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "javaStatement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, JAVA_STATEMENT, "<java statement>");
    result_ = javaStatementContent(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::javaStatementRecover);
    return result_;
  }

  /* ********************************************************** */
  // statementToken+
  static boolean javaStatementContent(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "javaStatementContent")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statementToken(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!statementToken(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "javaStatementContent", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(SEMICOLON | END_KEYWORD | RULE_KEYWORD | <<eof>>)
  static boolean javaStatementRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "javaStatementRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !javaStatementRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SEMICOLON | END_KEYWORD | RULE_KEYWORD | <<eof>>
  private static boolean javaStatementRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "javaStatementRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER | STRING | TRUE_KEYWORD | FALSE_KEYWORD | NULL_KEYWORD
  public static boolean literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LITERAL, "<literal>");
    result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NULL_KEYWORD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LOCK_ON_ACTIVE_KEYWORD attributeValue?
  public static boolean lockOnActiveAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lockOnActiveAttribute")) return false;
    if (!nextTokenIs(builder_, LOCK_ON_ACTIVE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LOCK_ON_ACTIVE_ATTRIBUTE, null);
    result_ = consumeToken(builder_, LOCK_ON_ACTIVE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && lockOnActiveAttribute_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // attributeValue?
  private static boolean lockOnActiveAttribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lockOnActiveAttribute_1")) return false;
    attributeValue(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // AND_KEYWORD | OR_KEYWORD
  public static boolean logicalConnective(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logicalConnective")) return false;
    if (!nextTokenIs(builder_, "<logical connective>", AND_KEYWORD, OR_KEYWORD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LOGICAL_CONNECTIVE, "<logical connective>");
    result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // NO_LOOP_KEYWORD attributeValue?
  public static boolean noLoopAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "noLoopAttribute")) return false;
    if (!nextTokenIs(builder_, NO_LOOP_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NO_LOOP_ATTRIBUTE, null);
    result_ = consumeToken(builder_, NO_LOOP_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && noLoopAttribute_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // attributeValue?
  private static boolean noLoopAttribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "noLoopAttribute_1")) return false;
    attributeValue(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // NOT_KEYWORD (LEFT_PAREN pattern+ RIGHT_PAREN | factPattern)
  public static boolean notElement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "notElement")) return false;
    if (!nextTokenIs(builder_, NOT_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NOT_ELEMENT, null);
    result_ = consumeToken(builder_, NOT_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && notElement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // LEFT_PAREN pattern+ RIGHT_PAREN | factPattern
  private static boolean notElement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "notElement_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = notElement_1_0(builder_, level_ + 1);
    if (!result_) result_ = factPattern(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN pattern+ RIGHT_PAREN
  private static boolean notElement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "notElement_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && notElement_1_0_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pattern+
  private static boolean notElement_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "notElement_1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pattern(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pattern(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "notElement_1_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // PACKAGE_KEYWORD qualifiedName SEMICOLON?
  public static boolean packageDecl(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "packageDecl")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PACKAGE_DECL, "<package decl>");
    result_ = consumeToken(builder_, PACKAGE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, qualifiedName(builder_, level_ + 1));
    result_ = pinned_ && packageDecl_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::packageDeclRecover);
    return result_ || pinned_;
  }

  // SEMICOLON?
  private static boolean packageDecl_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "packageDecl_2")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // !(IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>)
  static boolean packageDeclRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "packageDeclRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !packageDeclRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IMPORT_KEYWORD | GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>
  private static boolean packageDeclRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "packageDeclRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // typeName IDENTIFIER
  public static boolean parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = typeName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, marker_, PARAMETER, result_);
    return result_;
  }

  /* ********************************************************** */
  // parameter (COMMA parameter)*
  public static boolean parameterList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parameter(builder_, level_ + 1);
    result_ = result_ && parameterList_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARAMETER_LIST, result_);
    return result_;
  }

  // (COMMA parameter)*
  private static boolean parameterList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!parameterList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parameterList_1", pos_)) break;
    }
    return true;
  }

  // COMMA parameter
  private static boolean parameterList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameterList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // bindingPattern | conditionalElement | factPattern
  public static boolean pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pattern")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PATTERN, "<pattern>");
    result_ = bindingPattern(builder_, level_ + 1);
    if (!result_) result_ = conditionalElement(builder_, level_ + 1);
    if (!result_) result_ = factPattern(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::patternRecover);
    return result_;
  }

  /* ********************************************************** */
  // !(THEN_KEYWORD | END_KEYWORD | AND_KEYWORD | OR_KEYWORD | NOT_KEYWORD | EXISTS_KEYWORD | FORALL_KEYWORD | IDENTIFIER | DOLLAR | LEFT_PAREN | <<eof>>)
  static boolean patternRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "patternRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !patternRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // THEN_KEYWORD | END_KEYWORD | AND_KEYWORD | OR_KEYWORD | NOT_KEYWORD | EXISTS_KEYWORD | FORALL_KEYWORD | IDENTIFIER | DOLLAR | LEFT_PAREN | <<eof>>
  private static boolean patternRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "patternRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, THEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AND_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, OR_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NOT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, EXISTS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FORALL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, LEFT_PAREN);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOT IDENTIFIER)*
  public static boolean qualifiedName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedName")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && qualifiedName_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, QUALIFIED_NAME, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean qualifiedName_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedName_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!qualifiedName_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qualifiedName_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean qualifiedName_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualifiedName_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // QUERY_KEYWORD queryName queryParams? pattern* END_KEYWORD
  public static boolean queryDef(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryDef")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, QUERY_DEF, "<query def>");
    result_ = consumeToken(builder_, QUERY_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, queryName(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, queryDef_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, queryDef_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, END_KEYWORD) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::queryDefRecover);
    return result_ || pinned_;
  }

  // queryParams?
  private static boolean queryDef_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryDef_2")) return false;
    queryParams(builder_, level_ + 1);
    return true;
  }

  // pattern*
  private static boolean queryDef_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryDef_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pattern(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "queryDef_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>)
  static boolean queryDefRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryDefRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !queryDefRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>
  private static boolean queryDefRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryDefRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING | IDENTIFIER
  public static boolean queryName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryName")) return false;
    if (!nextTokenIs(builder_, "<query name>", IDENTIFIER, STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, QUERY_NAME, "<query name>");
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LEFT_PAREN parameter (COMMA parameter)* RIGHT_PAREN
  public static boolean queryParams(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryParams")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && parameter(builder_, level_ + 1);
    result_ = result_ && queryParams_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, QUERY_PARAMS, result_);
    return result_;
  }

  // (COMMA parameter)*
  private static boolean queryParams_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryParams_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!queryParams_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "queryParams_2", pos_)) break;
    }
    return true;
  }

  // COMMA parameter
  private static boolean queryParams_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "queryParams_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // salienceAttribute
  //                  | noLoopAttribute
  //                  | ruleflowGroupAttribute
  //                  | agendaGroupAttribute
  //                  | autoFocusAttribute
  //                  | lockOnActiveAttribute
  //                  | dateEffectiveAttribute
  //                  | dateExpiresAttribute
  //                  | enabledAttribute
  //                  | durationAttribute
  //                  | timerAttribute
  //                  | dialectAttribute
  //                  | extendsAttribute
  public static boolean ruleAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttribute")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RULE_ATTRIBUTE, "<rule attribute>");
    result_ = salienceAttribute(builder_, level_ + 1);
    if (!result_) result_ = noLoopAttribute(builder_, level_ + 1);
    if (!result_) result_ = ruleflowGroupAttribute(builder_, level_ + 1);
    if (!result_) result_ = agendaGroupAttribute(builder_, level_ + 1);
    if (!result_) result_ = autoFocusAttribute(builder_, level_ + 1);
    if (!result_) result_ = lockOnActiveAttribute(builder_, level_ + 1);
    if (!result_) result_ = dateEffectiveAttribute(builder_, level_ + 1);
    if (!result_) result_ = dateExpiresAttribute(builder_, level_ + 1);
    if (!result_) result_ = enabledAttribute(builder_, level_ + 1);
    if (!result_) result_ = durationAttribute(builder_, level_ + 1);
    if (!result_) result_ = timerAttribute(builder_, level_ + 1);
    if (!result_) result_ = dialectAttribute(builder_, level_ + 1);
    if (!result_) result_ = extendsAttribute(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::ruleAttributeRecover);
    return result_;
  }

  /* ********************************************************** */
  // !(SALIENCE_KEYWORD | NO_LOOP_KEYWORD | RULEFLOW_GROUP_KEYWORD | AGENDA_GROUP_KEYWORD | AUTO_FOCUS_KEYWORD | LOCK_ON_ACTIVE_KEYWORD | DATE_EFFECTIVE_KEYWORD | DATE_EXPIRES_KEYWORD | ENABLED_KEYWORD | DURATION_KEYWORD | TIMER_KEYWORD | DIALECT_KEYWORD | EXTENDS_KEYWORD | WHEN_KEYWORD | THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>)
  static boolean ruleAttributeRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttributeRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !ruleAttributeRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SALIENCE_KEYWORD | NO_LOOP_KEYWORD | RULEFLOW_GROUP_KEYWORD | AGENDA_GROUP_KEYWORD | AUTO_FOCUS_KEYWORD | LOCK_ON_ACTIVE_KEYWORD | DATE_EFFECTIVE_KEYWORD | DATE_EXPIRES_KEYWORD | ENABLED_KEYWORD | DURATION_KEYWORD | TIMER_KEYWORD | DIALECT_KEYWORD | EXTENDS_KEYWORD | WHEN_KEYWORD | THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>
  private static boolean ruleAttributeRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttributeRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SALIENCE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NO_LOOP_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULEFLOW_GROUP_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AGENDA_GROUP_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, AUTO_FOCUS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, LOCK_ON_ACTIVE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DATE_EFFECTIVE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DATE_EXPIRES_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ENABLED_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DURATION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TIMER_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DIALECT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, EXTENDS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, WHEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ruleAttribute+
  public static boolean ruleAttributes(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttributes")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RULE_ATTRIBUTES, "<rule attributes>");
    result_ = ruleAttribute(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!ruleAttribute(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "ruleAttributes", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::ruleAttributesRecover);
    return result_;
  }

  /* ********************************************************** */
  // !(WHEN_KEYWORD | THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>)
  static boolean ruleAttributesRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttributesRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !ruleAttributesRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // WHEN_KEYWORD | THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>
  private static boolean ruleAttributesRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleAttributesRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, WHEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // RULE_KEYWORD ruleName ruleAttributes? whenClause thenClause END_KEYWORD
  public static boolean ruleBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleBlock")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RULE_BLOCK, "<rule block>");
    result_ = consumeToken(builder_, RULE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, ruleName(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, ruleBlock_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, whenClause(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, thenClause(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, END_KEYWORD) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::ruleBlockRecover);
    return result_ || pinned_;
  }

  // ruleAttributes?
  private static boolean ruleBlock_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleBlock_2")) return false;
    ruleAttributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !(RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>)
  static boolean ruleBlockRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleBlockRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !ruleBlockRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | GLOBAL_KEYWORD | IMPORT_KEYWORD | <<eof>>
  private static boolean ruleBlockRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleBlockRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IMPORT_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING | IDENTIFIER (DOT IDENTIFIER)*
  public static boolean ruleName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleName")) return false;
    if (!nextTokenIs(builder_, "<rule name>", IDENTIFIER, STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RULE_NAME, "<rule name>");
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = ruleName_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IDENTIFIER (DOT IDENTIFIER)*
  private static boolean ruleName_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleName_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && ruleName_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean ruleName_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleName_1_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!ruleName_1_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "ruleName_1_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean ruleName_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleName_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // RULEFLOW_GROUP_KEYWORD attributeValue
  public static boolean ruleflowGroupAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ruleflowGroupAttribute")) return false;
    if (!nextTokenIs(builder_, RULEFLOW_GROUP_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RULEFLOW_GROUP_ATTRIBUTE, null);
    result_ = consumeToken(builder_, RULEFLOW_GROUP_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // SALIENCE_KEYWORD attributeValue
  public static boolean salienceAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "salienceAttribute")) return false;
    if (!nextTokenIs(builder_, SALIENCE_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SALIENCE_ATTRIBUTE, null);
    result_ = consumeToken(builder_, SALIENCE_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // IDENTIFIER | NUMBER | STRING | OPERATOR | DOT | COMMA | COLON | DOLLAR
  //                           | LEFT_PAREN expressionContent? RIGHT_PAREN
  //                           | LEFT_BRACE blockContent? RIGHT_BRACE
  //                           | LEFT_BRACKET expressionContent? RIGHT_BRACKET
  //                           | NEW_KEYWORD | NULL_KEYWORD | TRUE_KEYWORD | FALSE_KEYWORD | THIS_KEYWORD
  //                           | IF_KEYWORD | ELSE_KEYWORD | RETURN_KEYWORD
  //                           | INSERT_KEYWORD | INSERT_LOGICAL_KEYWORD | UPDATE_KEYWORD
  //                           | MODIFY_KEYWORD | RETRACT_KEYWORD | DELETE_KEYWORD
  static boolean statementToken(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, OPERATOR);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = statementToken_8(builder_, level_ + 1);
    if (!result_) result_ = statementToken_9(builder_, level_ + 1);
    if (!result_) result_ = statementToken_10(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEW_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, NULL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, TRUE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FALSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, THIS_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, IF_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, ELSE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETURN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, INSERT_LOGICAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, UPDATE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, MODIFY_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RETRACT_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DELETE_KEYWORD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LEFT_PAREN expressionContent? RIGHT_PAREN
  private static boolean statementToken_8(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_8")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && statementToken_8_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean statementToken_8_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_8_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACE blockContent? RIGHT_BRACE
  private static boolean statementToken_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && statementToken_9_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // blockContent?
  private static boolean statementToken_9_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_9_1")) return false;
    blockContent(builder_, level_ + 1);
    return true;
  }

  // LEFT_BRACKET expressionContent? RIGHT_BRACKET
  private static boolean statementToken_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    result_ = result_ && statementToken_10_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expressionContent?
  private static boolean statementToken_10_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statementToken_10_1")) return false;
    expressionContent(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // THEN_KEYWORD javaStatement*
  public static boolean thenClause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "thenClause")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, THEN_CLAUSE, "<then clause>");
    result_ = consumeToken(builder_, THEN_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && thenClause_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::thenClauseRecover);
    return result_ || pinned_;
  }

  // javaStatement*
  private static boolean thenClause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "thenClause_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!javaStatement(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "thenClause_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(END_KEYWORD | RULE_KEYWORD | <<eof>>)
  static boolean thenClauseRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "thenClauseRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !thenClauseRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // END_KEYWORD | RULE_KEYWORD | <<eof>>
  private static boolean thenClauseRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "thenClauseRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TIMER_KEYWORD attributeValue
  public static boolean timerAttribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "timerAttribute")) return false;
    if (!nextTokenIs(builder_, TIMER_KEYWORD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TIMER_ATTRIBUTE, null);
    result_ = consumeToken(builder_, TIMER_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && attributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // globalDecl | ruleBlock | functionDef | declareBlock | queryDef
  static boolean topLevelItem(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "topLevelItem")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = globalDecl(builder_, level_ + 1);
    if (!result_) result_ = ruleBlock(builder_, level_ + 1);
    if (!result_) result_ = functionDef(builder_, level_ + 1);
    if (!result_) result_ = declareBlock(builder_, level_ + 1);
    if (!result_) result_ = queryDef(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, DroolsParser::topLevelItemRecover);
    return result_;
  }

  /* ********************************************************** */
  // !(GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>)
  static boolean topLevelItemRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "topLevelItemRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !topLevelItemRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // GLOBAL_KEYWORD | RULE_KEYWORD | FUNCTION_KEYWORD | DECLARE_KEYWORD | QUERY_KEYWORD | <<eof>>
  private static boolean topLevelItemRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "topLevelItemRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, GLOBAL_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, FUNCTION_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, DECLARE_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, QUERY_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // OPERATOR className (COMMA className)* OPERATOR
  public static boolean typeArguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeArguments")) return false;
    if (!nextTokenIs(builder_, OPERATOR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR);
    result_ = result_ && className(builder_, level_ + 1);
    result_ = result_ && typeArguments_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, OPERATOR);
    exit_section_(builder_, marker_, TYPE_ARGUMENTS, result_);
    return result_;
  }

  // (COMMA className)*
  private static boolean typeArguments_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeArguments_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!typeArguments_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "typeArguments_2", pos_)) break;
    }
    return true;
  }

  // COMMA className
  private static boolean typeArguments_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeArguments_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && className(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // className typeArguments? arrayBrackets?
  public static boolean typeName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeName")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = className(builder_, level_ + 1);
    result_ = result_ && typeName_1(builder_, level_ + 1);
    result_ = result_ && typeName_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, TYPE_NAME, result_);
    return result_;
  }

  // typeArguments?
  private static boolean typeName_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeName_1")) return false;
    typeArguments(builder_, level_ + 1);
    return true;
  }

  // arrayBrackets?
  private static boolean typeName_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeName_2")) return false;
    arrayBrackets(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // WHEN_KEYWORD pattern*
  public static boolean whenClause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "whenClause")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHEN_CLAUSE, "<when clause>");
    result_ = consumeToken(builder_, WHEN_KEYWORD);
    pinned_ = result_; // pin = 1
    result_ = result_ && whenClause_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, DroolsParser::whenClauseRecover);
    return result_ || pinned_;
  }

  // pattern*
  private static boolean whenClause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "whenClause_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pattern(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "whenClause_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>)
  static boolean whenClauseRecover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "whenClauseRecover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !whenClauseRecover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // THEN_KEYWORD | END_KEYWORD | RULE_KEYWORD | <<eof>>
  private static boolean whenClauseRecover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "whenClauseRecover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, THEN_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, END_KEYWORD);
    if (!result_) result_ = consumeToken(builder_, RULE_KEYWORD);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

}
