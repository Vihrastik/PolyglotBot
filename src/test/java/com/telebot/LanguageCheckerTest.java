package com.telebot;

import com.telebot.common.LanguageChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageCheckerTest {

    @Test
    public void testCheckWordEnglish() {
        assertTrue(LanguageChecker.checkWord("hello", "english"));
        assertFalse(LanguageChecker.checkWord("hello1", "english"));
        assertFalse(LanguageChecker.checkWord("hello#", "english"));
    }

    @Test
    public void testCheckWordGerman() {
        assertTrue(LanguageChecker.checkWord("hallo", "german"));
        assertTrue(LanguageChecker.checkWord("straße", "german"));
        assertFalse(LanguageChecker.checkWord("straße1", "german"));
    }

    @Test
    public void testCheckWordRussian() {
        assertTrue(LanguageChecker.checkWord("привет", "russian"));
        assertFalse(LanguageChecker.checkWord("привет1", "russian"));
        assertFalse(LanguageChecker.checkWord("привет#", "russian"));
    }

    @Test
    public void testCheckWordUnsupportedLanguage() {
        assertFalse(LanguageChecker.checkWord("hello", "french"));
    }

    @Test
    public void testIsSafeString() {
        assertTrue(LanguageChecker.isSafeString("hello world"));
        assertFalse(LanguageChecker.isSafeString("hello'world"));
        assertFalse(LanguageChecker.isSafeString("hello-world"));
        assertFalse(LanguageChecker.isSafeString("hello\\world"));
    }

    @Test
    public void testIsFWordsContainsFWord() {
        assertTrue(LanguageChecker.isFWords("This is a fuck word"));
        assertTrue(LanguageChecker.isFWords("Ебать!"));
        assertTrue(LanguageChecker.isFWords("Scheiße"));
    }

    @Test
    public void testIsFWordsDoesNotContainFWord() {
        assertFalse(LanguageChecker.isFWords("This is a normal sentence"));
        assertFalse(LanguageChecker.isFWords("Привет как дела"));
        assertFalse(LanguageChecker.isFWords("Hallo, wie geht's"));
    }

    @Test
    public void testIsFWordsEmptyString() {
        assertFalse(LanguageChecker.isFWords(""));
    }
}

