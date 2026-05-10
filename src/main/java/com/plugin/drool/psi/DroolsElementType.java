package com.plugin.drool.psi;

import com.intellij.psi.tree.IElementType;
import com.plugin.drool.DroolsLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DroolsElementType extends IElementType {
    public DroolsElementType(@NotNull @NonNls String debugName) {
        super(debugName, DroolsLanguage.INSTANCE);
    }
}
