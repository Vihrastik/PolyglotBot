package com.telebot.common;

import java.util.Arrays;
import java.util.List;

public class LanguageChecker {
    private static final List<String> fWords = Arrays.asList(
            // Русские нецензурные слова
            "хуй", "пизда", "ебать", "ебал", "ебаный", "сука", "блядь",
            "манда", "гондон", "сучка", "мудила", "пидор", "дрочить", "говно",
            "жопа", "пидорас", "бля", "чмо", "хуесос",
            "хyй", "xуй", "х*й", "е6ать", "eбaть", "ёбать", "ё6ать",
            // Английские нецензурные слова
            "fuck", "shit", "bitch", "asshole", "cunt", "dick", "bastard",
            "motherfucker", "whore", "pussy", "slut", "cock", "faggot", "nigger",
            "f*ck", "sh1t", "a$$", "bi*ch", "d1ck",
            // Немецкие нецензурные слова
            "scheiße", "arschloch", "fick", "verfickt", "fotze", "hure", "schlampe",
            "wichser", "schwanz", "pisser", "miststück", "dummkopf", "hurensohn",
            "scheißkerl", "kacke", "kanake", "zigeuner",
            // Вариации и замены
            "sch3iße", "f1ck", "f1cken", "a$$", "h*rensohn", "schw@nz"
    );

    public static boolean checkWord(String str, String language) {
        String regex;
        switch (language.toLowerCase()) {
            case "english":
                regex = "^[a-zA-Z\\s]+$";
                break;
            case "german":
                regex = "^[a-zA-ZäöüÄÖÜß\\s]+$";
                break;
            case "russian":
                regex = "^[а-яА-ЯёЁ\\s]+$";
                break;
            default:
                return false;
        }
        return str.matches(regex);
    }

    public static boolean isSafeString(String str) {
        String unsafeRegex = ".*['\";\\\\-].*";
        return !str.matches(unsafeRegex);
    }

    public static boolean isFWords(String str) {
        for (String word : fWords) {
            if (str.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}
