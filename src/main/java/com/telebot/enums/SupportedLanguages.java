package com.telebot.enums;

public enum SupportedLanguages {
    ENGLISH("en", "english"),
    RUSSIAN("ru", "russian"),
    GERMAN("de", "german");

    private final String description;
    private final String code;

    SupportedLanguages(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String langCode() {
        return code;
    }

    public String description(){
        return description;
    }
}
