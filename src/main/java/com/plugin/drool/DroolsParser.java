package com.plugin.drool;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class DroolsParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        PsiBuilder.Marker rootMarker = builder.mark();

        // Simple parsing - just consume all tokens
        while (!builder.eof()) {
            builder.advanceLexer();
        }

        rootMarker.done(root);
        return builder.getTreeBuilt();
    }
}