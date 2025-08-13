package com.plugin.drool;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.util.ProcessingContext;
import com.plugin.drool.util.DroolsConstants;
import org.jetbrains.annotations.NotNull;

public class DroolsCompletionContributor extends CompletionContributor {

  public DroolsCompletionContributor() {
    // Complete keywords anywhere in Drools files
    extend(
        CompletionType.BASIC,
        PlatformPatterns.psiElement()
            .inFile(
                PlatformPatterns.psiFile().withName(StandardPatterns.string().endsWith(".drl"))),
        new CompletionProvider<>() {
          @Override
          protected void addCompletions(
              @NotNull CompletionParameters parameters,
              @NotNull ProcessingContext context,
              @NotNull CompletionResultSet result) {

            // Add keywords (from centralized constants)
            for (String keyword : DroolsConstants.KEYWORDS) {
              result.addElement(
                  LookupElementBuilder.create(keyword).withTypeText("keyword").withBoldness(true));
            }

            // Add functions (from centralized constants)
            for (String function : DroolsConstants.FUNCTIONS) {
              result.addElement(
                  LookupElementBuilder.create(function + "()")
                      .withTypeText("function")
                      .withInsertHandler(
                          (context1, item) ->
                              // Move cursor between parentheses
                              context1
                                  .getEditor()
                                  .getCaretModel()
                                  .moveToOffset(context1.getTailOffset() - 1)));
            }

            // Add operators (from centralized constants)
            for (String operator : DroolsConstants.OPERATORS) {
              result.addElement(LookupElementBuilder.create(operator).withTypeText("operator"));
            }

            // Add rule templates (from centralized constants)
            for (int i = 0; i < DroolsConstants.RULE_TEMPLATES.length; i++) {
              int finalI = i;
              result.addElement(
                  LookupElementBuilder.create("rule_template_" + (i + 1))
                      .withPresentableText("Rule Template " + (i + 1))
                      .withTypeText("template")
                      .withInsertHandler(
                          (context1, item) ->
                              // Replace the trigger text with the template
                              context1
                                  .getDocument()
                                  .replaceString(
                                      context1.getStartOffset(),
                                      context1.getTailOffset(),
                                      DroolsConstants.RULE_TEMPLATES[finalI])));
            }
          }
        });
  }
}
