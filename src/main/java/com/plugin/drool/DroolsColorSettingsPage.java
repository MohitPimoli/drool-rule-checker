package com.plugin.drool;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DroolsColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS =
      new AttributesDescriptor[] {
        new AttributesDescriptor("Keyword", DroolsSyntaxHighlighter.KEYWORD),
        new AttributesDescriptor("Identifier", DroolsSyntaxHighlighter.IDENTIFIER),
        new AttributesDescriptor("String", DroolsSyntaxHighlighter.STRING),
        new AttributesDescriptor("Number", DroolsSyntaxHighlighter.NUMBER),
        new AttributesDescriptor("Comment", DroolsSyntaxHighlighter.COMMENT),
        new AttributesDescriptor("Operator", DroolsSyntaxHighlighter.OPERATOR),
      };

  @Nullable
  @Override
  public Icon getIcon() {
    return DroolsFileType.getInstance().getIcon();
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new DroolsSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return """
                package com.example.rules

                import java.util.List

                // Sample Drools rule
                rule "Sample Rule"
                    salience 10
                    when
                        $person : Person(age > 18)
                        $accounts : List(size > 0) from $person.accounts
                    then
                        System.out.println("Adult with accounts found: " + $person.name);
                        modify($person) {
                            setStatus("ACTIVE")
                        }
                end

                rule "Another Rule"
                    when
                        $order : Order(total > 1000.00)
                    then
                        $order.setDiscount(0.1);
                        update($order);
                end
                """;
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return null;
  }

  @NotNull
  @Override
  public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @NotNull
  @Override
  public ColorDescriptor @NotNull [] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Drools";
  }
}
