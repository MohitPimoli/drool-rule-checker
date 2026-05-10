package com.plugin.drool.psi;

import com.intellij.psi.tree.IElementType;
import com.plugin.drool.DroolsLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DroolsTokenType extends IElementType {
    public DroolsTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DroolsLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DroolsTokenType." + super.toString();
    }
}
