package com.plugin.drool;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class DroolsParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(DroolsLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new DroolsLexer();
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new DroolsParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return DroolsTokenTypes.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return DroolsTokenTypes.STRINGS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return new DroolsPsiElement(node);
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new DroolsPsiFile(viewProvider);
    }
}