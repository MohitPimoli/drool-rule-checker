// This is a generated file. Not intended for manual editing.
package com.plugin.drool.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.plugin.drool.psi.impl.*;

public interface DroolsTypes {

  IElementType ACCUMULATE_FUNCTION = new DroolsElementType("ACCUMULATE_FUNCTION");
  IElementType ACCUM_ELEMENT = new DroolsElementType("ACCUM_ELEMENT");
  IElementType AGENDA_GROUP_ATTRIBUTE = new DroolsElementType("AGENDA_GROUP_ATTRIBUTE");
  IElementType ARRAY_BRACKETS = new DroolsElementType("ARRAY_BRACKETS");
  IElementType ATTRIBUTE_VALUE = new DroolsElementType("ATTRIBUTE_VALUE");
  IElementType AUTO_FOCUS_ATTRIBUTE = new DroolsElementType("AUTO_FOCUS_ATTRIBUTE");
  IElementType BINDING_PATTERN = new DroolsElementType("BINDING_PATTERN");
  IElementType BINDING_VARIABLE = new DroolsElementType("BINDING_VARIABLE");
  IElementType BLOCK_CONTENT = new DroolsElementType("BLOCK_CONTENT");
  IElementType CLASS_NAME = new DroolsElementType("CLASS_NAME");
  IElementType CONDITIONAL_ELEMENT = new DroolsElementType("CONDITIONAL_ELEMENT");
  IElementType CONSTRAINT_EXPR = new DroolsElementType("CONSTRAINT_EXPR");
  IElementType CONSTRAINT_LIST = new DroolsElementType("CONSTRAINT_LIST");
  IElementType CONSTRAINT_OPERATOR = new DroolsElementType("CONSTRAINT_OPERATOR");
  IElementType DATE_EFFECTIVE_ATTRIBUTE = new DroolsElementType("DATE_EFFECTIVE_ATTRIBUTE");
  IElementType DATE_EXPIRES_ATTRIBUTE = new DroolsElementType("DATE_EXPIRES_ATTRIBUTE");
  IElementType DECLARE_BLOCK = new DroolsElementType("DECLARE_BLOCK");
  IElementType DECLARE_EXTENDS = new DroolsElementType("DECLARE_EXTENDS");
  IElementType DECLARE_FIELD = new DroolsElementType("DECLARE_FIELD");
  IElementType DIALECT_ATTRIBUTE = new DroolsElementType("DIALECT_ATTRIBUTE");
  IElementType DURATION_ATTRIBUTE = new DroolsElementType("DURATION_ATTRIBUTE");
  IElementType ENABLED_ATTRIBUTE = new DroolsElementType("ENABLED_ATTRIBUTE");
  IElementType EVAL_ELEMENT = new DroolsElementType("EVAL_ELEMENT");
  IElementType EXISTS_ELEMENT = new DroolsElementType("EXISTS_ELEMENT");
  IElementType EXPRESSION = new DroolsElementType("EXPRESSION");
  IElementType EXPRESSION_CONTENT = new DroolsElementType("EXPRESSION_CONTENT");
  IElementType EXTENDS_ATTRIBUTE = new DroolsElementType("EXTENDS_ATTRIBUTE");
  IElementType FACT_PATTERN = new DroolsElementType("FACT_PATTERN");
  IElementType FORALL_ELEMENT = new DroolsElementType("FORALL_ELEMENT");
  IElementType FROM_ELEMENT = new DroolsElementType("FROM_ELEMENT");
  IElementType FUNCTION_BODY = new DroolsElementType("FUNCTION_BODY");
  IElementType FUNCTION_DEF = new DroolsElementType("FUNCTION_DEF");
  IElementType GLOBAL_DECL = new DroolsElementType("GLOBAL_DECL");
  IElementType IMPORT_PATH = new DroolsElementType("IMPORT_PATH");
  IElementType IMPORT_STATEMENT = new DroolsElementType("IMPORT_STATEMENT");
  IElementType JAVA_STATEMENT = new DroolsElementType("JAVA_STATEMENT");
  IElementType LITERAL = new DroolsElementType("LITERAL");
  IElementType LOCK_ON_ACTIVE_ATTRIBUTE = new DroolsElementType("LOCK_ON_ACTIVE_ATTRIBUTE");
  IElementType LOGICAL_CONNECTIVE = new DroolsElementType("LOGICAL_CONNECTIVE");
  IElementType NOT_ELEMENT = new DroolsElementType("NOT_ELEMENT");
  IElementType NO_LOOP_ATTRIBUTE = new DroolsElementType("NO_LOOP_ATTRIBUTE");
  IElementType PACKAGE_DECL = new DroolsElementType("PACKAGE_DECL");
  IElementType PARAMETER = new DroolsElementType("PARAMETER");
  IElementType PARAMETER_LIST = new DroolsElementType("PARAMETER_LIST");
  IElementType PATTERN = new DroolsElementType("PATTERN");
  IElementType QUALIFIED_NAME = new DroolsElementType("QUALIFIED_NAME");
  IElementType QUERY_DEF = new DroolsElementType("QUERY_DEF");
  IElementType QUERY_NAME = new DroolsElementType("QUERY_NAME");
  IElementType QUERY_PARAMS = new DroolsElementType("QUERY_PARAMS");
  IElementType RULEFLOW_GROUP_ATTRIBUTE = new DroolsElementType("RULEFLOW_GROUP_ATTRIBUTE");
  IElementType RULE_ATTRIBUTE = new DroolsElementType("RULE_ATTRIBUTE");
  IElementType RULE_ATTRIBUTES = new DroolsElementType("RULE_ATTRIBUTES");
  IElementType RULE_BLOCK = new DroolsElementType("RULE_BLOCK");
  IElementType RULE_NAME = new DroolsElementType("RULE_NAME");
  IElementType SALIENCE_ATTRIBUTE = new DroolsElementType("SALIENCE_ATTRIBUTE");
  IElementType THEN_CLAUSE = new DroolsElementType("THEN_CLAUSE");
  IElementType TIMER_ATTRIBUTE = new DroolsElementType("TIMER_ATTRIBUTE");
  IElementType TYPE_ARGUMENTS = new DroolsElementType("TYPE_ARGUMENTS");
  IElementType TYPE_NAME = new DroolsElementType("TYPE_NAME");
  IElementType WHEN_CLAUSE = new DroolsElementType("WHEN_CLAUSE");

  IElementType ACCUMULATE_KEYWORD = new DroolsTokenType("accumulate");
  IElementType AGENDA_GROUP_KEYWORD = new DroolsTokenType("agenda-group");
  IElementType AND_KEYWORD = new DroolsTokenType("and");
  IElementType AUTO_FOCUS_KEYWORD = new DroolsTokenType("auto-focus");
  IElementType COLLECT_KEYWORD = new DroolsTokenType("collect");
  IElementType COLON = new DroolsTokenType(":");
  IElementType COMMA = new DroolsTokenType(",");
  IElementType COMMENT = new DroolsTokenType("COMMENT");
  IElementType CONTAINS_KEYWORD = new DroolsTokenType("contains");
  IElementType DATE_EFFECTIVE_KEYWORD = new DroolsTokenType("date-effective");
  IElementType DATE_EXPIRES_KEYWORD = new DroolsTokenType("date-expires");
  IElementType DECLARE_KEYWORD = new DroolsTokenType("declare");
  IElementType DELETE_KEYWORD = new DroolsTokenType("delete");
  IElementType DIALECT_KEYWORD = new DroolsTokenType("dialect");
  IElementType DOLLAR = new DroolsTokenType("$");
  IElementType DOT = new DroolsTokenType(".");
  IElementType DURATION_KEYWORD = new DroolsTokenType("duration");
  IElementType ELSE_KEYWORD = new DroolsTokenType("else");
  IElementType ENABLED_KEYWORD = new DroolsTokenType("enabled");
  IElementType END_KEYWORD = new DroolsTokenType("end");
  IElementType EVAL_KEYWORD = new DroolsTokenType("eval");
  IElementType EXISTS_KEYWORD = new DroolsTokenType("exists");
  IElementType EXTENDS_KEYWORD = new DroolsTokenType("extends");
  IElementType FALSE_KEYWORD = new DroolsTokenType("false");
  IElementType FORALL_KEYWORD = new DroolsTokenType("forall");
  IElementType FROM_KEYWORD = new DroolsTokenType("from");
  IElementType FUNCTION_KEYWORD = new DroolsTokenType("function");
  IElementType GLOBAL_KEYWORD = new DroolsTokenType("global");
  IElementType IDENTIFIER = new DroolsTokenType("IDENTIFIER");
  IElementType IF_KEYWORD = new DroolsTokenType("if");
  IElementType IMPORT_KEYWORD = new DroolsTokenType("import");
  IElementType INSERT_KEYWORD = new DroolsTokenType("insert");
  IElementType INSERT_LOGICAL_KEYWORD = new DroolsTokenType("insertLogical");
  IElementType IN_KEYWORD = new DroolsTokenType("in");
  IElementType LEFT_BRACE = new DroolsTokenType("{");
  IElementType LEFT_BRACKET = new DroolsTokenType("[");
  IElementType LEFT_PAREN = new DroolsTokenType("(");
  IElementType LOCK_ON_ACTIVE_KEYWORD = new DroolsTokenType("lock-on-active");
  IElementType MATCHES_KEYWORD = new DroolsTokenType("matches");
  IElementType MEMBER_OF_KEYWORD = new DroolsTokenType("memberOf");
  IElementType MODIFY_KEYWORD = new DroolsTokenType("modify");
  IElementType NEW_KEYWORD = new DroolsTokenType("new");
  IElementType NOT_KEYWORD = new DroolsTokenType("not");
  IElementType NO_LOOP_KEYWORD = new DroolsTokenType("no-loop");
  IElementType NULL_KEYWORD = new DroolsTokenType("null");
  IElementType NUMBER = new DroolsTokenType("NUMBER");
  IElementType OPERATOR = new DroolsTokenType("OPERATOR");
  IElementType OR_KEYWORD = new DroolsTokenType("or");
  IElementType PACKAGE_KEYWORD = new DroolsTokenType("package");
  IElementType QUERY_KEYWORD = new DroolsTokenType("query");
  IElementType RETRACT_KEYWORD = new DroolsTokenType("retract");
  IElementType RETURN_KEYWORD = new DroolsTokenType("return");
  IElementType RIGHT_BRACE = new DroolsTokenType("}");
  IElementType RIGHT_BRACKET = new DroolsTokenType("]");
  IElementType RIGHT_PAREN = new DroolsTokenType(")");
  IElementType RULEFLOW_GROUP_KEYWORD = new DroolsTokenType("ruleflow-group");
  IElementType RULE_KEYWORD = new DroolsTokenType("rule");
  IElementType SALIENCE_KEYWORD = new DroolsTokenType("salience");
  IElementType SEMICOLON = new DroolsTokenType(";");
  IElementType SOUNDSLIKE_KEYWORD = new DroolsTokenType("soundslike");
  IElementType STRING = new DroolsTokenType("STRING");
  IElementType STR_KEYWORD = new DroolsTokenType("str");
  IElementType THEN_KEYWORD = new DroolsTokenType("then");
  IElementType THIS_KEYWORD = new DroolsTokenType("this");
  IElementType TIMER_KEYWORD = new DroolsTokenType("timer");
  IElementType TRUE_KEYWORD = new DroolsTokenType("true");
  IElementType UPDATE_KEYWORD = new DroolsTokenType("update");
  IElementType WHEN_KEYWORD = new DroolsTokenType("when");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ACCUMULATE_FUNCTION) {
        return new DroolsAccumulateFunctionImpl(node);
      }
      else if (type == ACCUM_ELEMENT) {
        return new DroolsAccumElementImpl(node);
      }
      else if (type == AGENDA_GROUP_ATTRIBUTE) {
        return new DroolsAgendaGroupAttributeImpl(node);
      }
      else if (type == ARRAY_BRACKETS) {
        return new DroolsArrayBracketsImpl(node);
      }
      else if (type == ATTRIBUTE_VALUE) {
        return new DroolsAttributeValueImpl(node);
      }
      else if (type == AUTO_FOCUS_ATTRIBUTE) {
        return new DroolsAutoFocusAttributeImpl(node);
      }
      else if (type == BINDING_PATTERN) {
        return new DroolsBindingPatternImpl(node);
      }
      else if (type == BINDING_VARIABLE) {
        return new DroolsBindingVariableImpl(node);
      }
      else if (type == BLOCK_CONTENT) {
        return new DroolsBlockContentImpl(node);
      }
      else if (type == CLASS_NAME) {
        return new DroolsClassNameImpl(node);
      }
      else if (type == CONDITIONAL_ELEMENT) {
        return new DroolsConditionalElementImpl(node);
      }
      else if (type == CONSTRAINT_EXPR) {
        return new DroolsConstraintExprImpl(node);
      }
      else if (type == CONSTRAINT_LIST) {
        return new DroolsConstraintListImpl(node);
      }
      else if (type == CONSTRAINT_OPERATOR) {
        return new DroolsConstraintOperatorImpl(node);
      }
      else if (type == DATE_EFFECTIVE_ATTRIBUTE) {
        return new DroolsDateEffectiveAttributeImpl(node);
      }
      else if (type == DATE_EXPIRES_ATTRIBUTE) {
        return new DroolsDateExpiresAttributeImpl(node);
      }
      else if (type == DECLARE_BLOCK) {
        return new DroolsDeclareBlockImpl(node);
      }
      else if (type == DECLARE_EXTENDS) {
        return new DroolsDeclareExtendsImpl(node);
      }
      else if (type == DECLARE_FIELD) {
        return new DroolsDeclareFieldImpl(node);
      }
      else if (type == DIALECT_ATTRIBUTE) {
        return new DroolsDialectAttributeImpl(node);
      }
      else if (type == DURATION_ATTRIBUTE) {
        return new DroolsDurationAttributeImpl(node);
      }
      else if (type == ENABLED_ATTRIBUTE) {
        return new DroolsEnabledAttributeImpl(node);
      }
      else if (type == EVAL_ELEMENT) {
        return new DroolsEvalElementImpl(node);
      }
      else if (type == EXISTS_ELEMENT) {
        return new DroolsExistsElementImpl(node);
      }
      else if (type == EXPRESSION) {
        return new DroolsExpressionImpl(node);
      }
      else if (type == EXPRESSION_CONTENT) {
        return new DroolsExpressionContentImpl(node);
      }
      else if (type == EXTENDS_ATTRIBUTE) {
        return new DroolsExtendsAttributeImpl(node);
      }
      else if (type == FACT_PATTERN) {
        return new DroolsFactPatternImpl(node);
      }
      else if (type == FORALL_ELEMENT) {
        return new DroolsForallElementImpl(node);
      }
      else if (type == FROM_ELEMENT) {
        return new DroolsFromElementImpl(node);
      }
      else if (type == FUNCTION_BODY) {
        return new DroolsFunctionBodyImpl(node);
      }
      else if (type == FUNCTION_DEF) {
        return new DroolsFunctionDefImpl(node);
      }
      else if (type == GLOBAL_DECL) {
        return new DroolsGlobalDeclImpl(node);
      }
      else if (type == IMPORT_PATH) {
        return new DroolsImportPathImpl(node);
      }
      else if (type == IMPORT_STATEMENT) {
        return new DroolsImportStatementImpl(node);
      }
      else if (type == JAVA_STATEMENT) {
        return new DroolsJavaStatementImpl(node);
      }
      else if (type == LITERAL) {
        return new DroolsLiteralImpl(node);
      }
      else if (type == LOCK_ON_ACTIVE_ATTRIBUTE) {
        return new DroolsLockOnActiveAttributeImpl(node);
      }
      else if (type == LOGICAL_CONNECTIVE) {
        return new DroolsLogicalConnectiveImpl(node);
      }
      else if (type == NOT_ELEMENT) {
        return new DroolsNotElementImpl(node);
      }
      else if (type == NO_LOOP_ATTRIBUTE) {
        return new DroolsNoLoopAttributeImpl(node);
      }
      else if (type == PACKAGE_DECL) {
        return new DroolsPackageDeclImpl(node);
      }
      else if (type == PARAMETER) {
        return new DroolsParameterImpl(node);
      }
      else if (type == PARAMETER_LIST) {
        return new DroolsParameterListImpl(node);
      }
      else if (type == PATTERN) {
        return new DroolsPatternImpl(node);
      }
      else if (type == QUALIFIED_NAME) {
        return new DroolsQualifiedNameImpl(node);
      }
      else if (type == QUERY_DEF) {
        return new DroolsQueryDefImpl(node);
      }
      else if (type == QUERY_NAME) {
        return new DroolsQueryNameImpl(node);
      }
      else if (type == QUERY_PARAMS) {
        return new DroolsQueryParamsImpl(node);
      }
      else if (type == RULEFLOW_GROUP_ATTRIBUTE) {
        return new DroolsRuleflowGroupAttributeImpl(node);
      }
      else if (type == RULE_ATTRIBUTE) {
        return new DroolsRuleAttributeImpl(node);
      }
      else if (type == RULE_ATTRIBUTES) {
        return new DroolsRuleAttributesImpl(node);
      }
      else if (type == RULE_BLOCK) {
        return new DroolsRuleBlockImpl(node);
      }
      else if (type == RULE_NAME) {
        return new DroolsRuleNameImpl(node);
      }
      else if (type == SALIENCE_ATTRIBUTE) {
        return new DroolsSalienceAttributeImpl(node);
      }
      else if (type == THEN_CLAUSE) {
        return new DroolsThenClauseImpl(node);
      }
      else if (type == TIMER_ATTRIBUTE) {
        return new DroolsTimerAttributeImpl(node);
      }
      else if (type == TYPE_ARGUMENTS) {
        return new DroolsTypeArgumentsImpl(node);
      }
      else if (type == TYPE_NAME) {
        return new DroolsTypeNameImpl(node);
      }
      else if (type == WHEN_CLAUSE) {
        return new DroolsWhenClauseImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
