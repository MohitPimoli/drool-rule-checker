package com.plugin.drool;

import com.intellij.lang.Language;

public class DroolsLanguage extends Language {
    public static final DroolsLanguage INSTANCE = new DroolsLanguage();

    private DroolsLanguage() {
        super("Drools");
    }
}