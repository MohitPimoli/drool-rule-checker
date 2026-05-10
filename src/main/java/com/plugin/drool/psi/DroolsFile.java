package com.plugin.drool.psi;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for Drools PSI file providing typed accessors for top-level constructs.
 */
public interface DroolsFile extends PsiFile {

    /**
     * Returns the package declaration, or null if none exists.
     */
    @Nullable
    DroolsPackageDecl getPackageDecl();

    /**
     * Returns all import statements in the file.
     */
    @NotNull
    List<DroolsImportStatement> getImports();

    /**
     * Returns all global declarations in the file.
     */
    @NotNull
    List<DroolsGlobalDecl> getGlobals();

    /**
     * Returns all rule blocks in the file.
     */
    @NotNull
    List<DroolsRuleBlock> getRules();

    /**
     * Returns all function definitions in the file.
     */
    @NotNull
    List<DroolsFunctionDef> getFunctions();

    /**
     * Returns all declare blocks in the file.
     */
    @NotNull
    List<DroolsDeclareBlock> getDeclares();

    /**
     * Returns all query definitions in the file.
     */
    @NotNull
    List<DroolsQueryDef> getQueries();
}
