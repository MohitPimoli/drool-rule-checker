package com.plugin.drool;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.PlatformIcons;
import com.plugin.drool.psi.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a single element in the Drools structure view tree. Each element corresponds to a PSI
 * element (rule, query, function, or declare block) and supports navigation to that element in the
 * editor.
 */
public class DroolsStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

  private final PsiElement element;

  public DroolsStructureViewElement(@NotNull PsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return element;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (element instanceof NavigationItem navigationItem) {
      navigationItem.navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return element instanceof NavigationItem navigationItem && navigationItem.canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return element instanceof NavigationItem navigationItem && navigationItem.canNavigateToSource();
  }

  @NotNull
  @Override
  public String getAlphaSortKey() {
    String name = getElementName();
    return name != null ? name : "";
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        return getElementName();
      }

      @Nullable
      @Override
      public String getLocationString() {
        return null;
      }

      @Nullable
      @Override
      public Icon getIcon(boolean unused) {
        return getElementIcon();
      }
    };
  }

  @NotNull
  @Override
  public TreeElement @NotNull [] getChildren() {
    if (element instanceof DroolsPsiFile file) {
      List<TreeElement> children = new ArrayList<>();

      for (DroolsRuleBlock rule : file.getRules()) {
        children.add(new DroolsStructureViewElement(rule));
      }
      for (DroolsQueryDef query : file.getQueries()) {
        children.add(new DroolsStructureViewElement(query));
      }
      for (DroolsFunctionDef function : file.getFunctions()) {
        children.add(new DroolsStructureViewElement(function));
      }
      for (DroolsDeclareBlock declare : file.getDeclares()) {
        children.add(new DroolsStructureViewElement(declare));
      }

      return children.toArray(TreeElement.EMPTY_ARRAY);
    }
    return TreeElement.EMPTY_ARRAY;
  }

  /** Returns the display name for this element based on its PSI type. */
  @Nullable
  private String getElementName() {
    if (element instanceof DroolsPsiFile droolsPsiFile) {
      return droolsPsiFile.getName();
    } else if (element instanceof DroolsRuleBlock ruleBlock) {
      DroolsRuleName ruleName = ruleBlock.getRuleName();
      if (ruleName != null) {
        return stripQuotes(ruleName.getText());
      }
      return "<unnamed rule>";
    } else if (element instanceof DroolsQueryDef queryDef) {
      DroolsQueryName queryName = queryDef.getQueryName();
      if (queryName != null) {
        return stripQuotes(queryName.getText());
      }
      return "<unnamed query>";
    } else if (element instanceof DroolsFunctionDef functionDef) {
      PsiElement identifier = functionDef.getIdentifier();
      if (identifier != null) {
        return identifier.getText();
      }
      return "<unnamed function>";
    } else if (element instanceof DroolsDeclareBlock declareBlock) {
      PsiElement identifier = declareBlock.getIdentifier();
      if (identifier != null) {
        return identifier.getText();
      }
      return "<unnamed type>";
    } else if (element instanceof PsiNamedElement psiNamedElement) {
      return psiNamedElement.getName();
    }
    return element.getText();
  }

  /** Returns the appropriate icon for this element based on its PSI type. */
  @Nullable
  private Icon getElementIcon() {
    if (element instanceof DroolsPsiFile) {
      return DroolsFileType.getInstance().getIcon();
    } else if (element instanceof DroolsRuleBlock) {
      return PlatformIcons.METHOD_ICON;
    } else if (element instanceof DroolsQueryDef) {
      return PlatformIcons.ABSTRACT_METHOD_ICON;
    } else if (element instanceof DroolsFunctionDef) {
      return PlatformIcons.FUNCTION_ICON;
    } else if (element instanceof DroolsDeclareBlock) {
      return PlatformIcons.CLASS_ICON;
    }
    return null;
  }

  /** Strips surrounding quotes from a string if present. */
  @NotNull
  private static String stripQuotes(@Nullable String text) {
    if (text == null) {
      return "";
    }
    if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
      return text.substring(1, text.length() - 1);
    }
    return text;
  }
}
