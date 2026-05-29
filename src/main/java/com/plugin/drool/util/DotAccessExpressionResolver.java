package com.plugin.drool.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.plugin.drool.psi.DroolsTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Shared helper that reconstructs the textual expression preceding a DOT leaf by walking backward
 * over DOLLAR / IDENTIFIER / DOT leaves.
 *
 * <p>Supports the following grammar:
 *
 * <pre>
 *   expression := DOLLAR IDENTIFIER ( DOT IDENTIFIER )*
 *              | IDENTIFIER ( DOT IDENTIFIER )*
 * </pre>
 *
 * <p>The walk starts from the DOT leaf (the parameter), goes backward (getPrevSibling), skipping
 * PsiWhiteSpace, collecting leaves that match the alternation. Returns the concatenation of leaf
 * texts in source order. Returns {@code null} if no expression is recoverable.
 *
 * <p>Used by both {@code DroolsCompletionContributor.MethodFieldProvider} and {@code
 * DroolsReferenceContributor.DroolsMethodReferenceProvider} to avoid duplicated logic for
 * reconstructing $-prefixed expressions.
 */
public final class DotAccessExpressionResolver {

  private DotAccessExpressionResolver() {
    // Utility class — no instantiation
  }

  /**
   * Reconstructs the expression text preceding the given DOT leaf by walking backward over sibling
   * leaves.
   *
   * <p>Examples of returned values:
   *
   * <ul>
   *   <li>{@code "$x"} — for a simple $-binding
   *   <li>{@code "$x.y"} — for a chained member access
   *   <li>{@code "$x.y.z"} — for a multi-level chain
   *   <li>{@code "x"} — for a non-$ identifier
   *   <li>{@code "x.y"} — for a non-$ chained access
   * </ul>
   *
   * @param dotLeaf the DOT leaf element (the {@code .} before the caret or method name)
   * @return the reconstructed expression text in source order, or {@code null} if no valid
   *     expression is recoverable
   */
  @Nullable
  public static String reconstructExpressionBeforeDot(@Nullable PsiElement dotLeaf) {
    if (dotLeaf == null) {
      return null;
    }

    // Collect leaves in reverse order (walking backward from the dot)
    List<PsiElement> collectedLeaves = new ArrayList<>();
    PsiElement current = dotLeaf.getPrevSibling();

    // Skip whitespace to find the first meaningful leaf
    current = skipWhitespace(current);

    if (current == null) {
      return null;
    }

    // The grammar we're matching (reading right-to-left from the dot):
    //   ... IDENTIFIER (DOT IDENTIFIER)* [DOLLAR] IDENTIFIER
    // In reverse: IDENTIFIER, then optionally (DOT IDENTIFIER)* pairs, then optionally DOLLAR
    //
    // State machine:
    //   EXPECT_IDENTIFIER -> got IDENTIFIER -> EXPECT_DOT_OR_DOLLAR_OR_END
    //   EXPECT_DOT_OR_DOLLAR_OR_END -> got DOT -> EXPECT_IDENTIFIER_AFTER_DOT
    //   EXPECT_DOT_OR_DOLLAR_OR_END -> got DOLLAR -> done (collect DOLLAR)
    //   EXPECT_IDENTIFIER_AFTER_DOT -> got IDENTIFIER -> EXPECT_DOT_OR_DOLLAR_OR_END
    //   EXPECT_IDENTIFIER_AFTER_DOT -> got DOLLAR -> invalid (DOLLAR before DOT without IDENTIFIER)

    // First, expect an IDENTIFIER
    IElementType currentType = getElementType(current);
    if (currentType != DroolsTypes.IDENTIFIER) {
      return null;
    }

    collectedLeaves.add(current);
    current = skipWhitespace(current.getPrevSibling());

    // Now loop: expect DOT or DOLLAR or end
    while (current != null) {
      currentType = getElementType(current);

      if (currentType == DroolsTypes.DOT) {
        // Got a DOT — expect an IDENTIFIER before it
        PsiElement dotElement = current;
        PsiElement beforeDot = skipWhitespace(current.getPrevSibling());

        if (beforeDot == null) {
          // DOT at the start with nothing before — don't include this DOT
          break;
        }

        IElementType beforeDotType = getElementType(beforeDot);

        if (beforeDotType == DroolsTypes.IDENTIFIER) {
          // Valid: IDENTIFIER DOT — collect both and continue
          collectedLeaves.add(dotElement);
          collectedLeaves.add(beforeDot);
          current = skipWhitespace(beforeDot.getPrevSibling());
        } else if (beforeDotType == DroolsTypes.DOLLAR) {
          // DOLLAR DOT — invalid pattern ($. is not valid), stop before this DOT
          break;
        } else {
          // Something else before the DOT — stop before this DOT
          break;
        }
      } else if (currentType == DroolsTypes.DOLLAR) {
        // Got a DOLLAR — this is the start of a $-binding, collect and done
        collectedLeaves.add(current);
        break;
      } else {
        // Not a DOT or DOLLAR — stop
        break;
      }
    }

    if (collectedLeaves.isEmpty()) {
      return null;
    }

    // Reverse to get source order (we collected right-to-left)
    Collections.reverse(collectedLeaves);

    // Concatenate leaf texts
    StringBuilder sb = new StringBuilder();
    for (PsiElement leaf : collectedLeaves) {
      sb.append(leaf.getText());
    }

    String result = sb.toString();
    return result.isEmpty() ? null : result;
  }

  /**
   * Skips PsiWhiteSpace elements going backward (via getPrevSibling chain).
   *
   * @param element the starting element (may be null or whitespace)
   * @return the first non-whitespace element, or null if none found
   */
  @Nullable
  private static PsiElement skipWhitespace(@Nullable PsiElement element) {
    while (element instanceof PsiWhiteSpace) {
      element = element.getPrevSibling();
    }
    return element;
  }

  /**
   * Gets the IElementType of a PSI element's AST node.
   *
   * @param element the PSI element
   * @return the element type, or null if the node is null
   */
  @Nullable
  private static IElementType getElementType(@Nullable PsiElement element) {
    if (element == null || element.getNode() == null) {
      return null;
    }
    return element.getNode().getElementType();
  }
}
