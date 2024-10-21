package com.telebot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class VocabularyTest {

    private Vocabulary vocabulary;
    private DbApplication databaseApp;

    @BeforeEach
    public void setUp() {
        vocabulary = new Vocabulary();
        databaseApp = Mockito.mock(DbApplication.class);
    }

    @Test
    public void testGetThemesFromJSONFile() throws Exception {
        // Assume the JSON file is parsed correctly and contains themes

        List<String> themes = vocabulary.getThemesfromJSONFile();
        assertNotNull(themes);
        assertEquals(13, themes.size());
        assertTrue(themes.contains("Charakterzug"));
    }

    @Test
    public void testGetWordsByThemeAndLanguageJSONFile() throws Exception {

        Map<String, List> result = vocabulary.getWordsByThemeAndLanguageJSONFile("Charakterzug", "english", "german");

        assertNotNull(result);
        assertTrue(result.containsKey("talkative"));
        assertEquals(Arrays.asList("redselig"), result.get("talkative"));
    }

    @Test
    public void testGetWordsByTopic_Russian() throws Exception {
        // Mocking database response for Russian words
        Map<Integer, String> mockWords = new HashMap<>();
        mockWords.put(1, "собака");
        mockWords.put(2, "кошка");
        when(databaseApp.getAllRussianWords(1)).thenReturn((HashMap<Integer, String>) mockWords);

        Map<Integer, String> words = vocabulary.getWordsByTopic(databaseApp, String.valueOf(Vocabulary.Language.RUSSIAN), 1);
        assertNotNull(words);
        assertEquals(2, words.size());
        assertEquals("собака", words.get(1));
        assertEquals("кошка", words.get(2));
    }

    @Test
    public void testGetTranslation_RussianToGerman() throws Exception {
        // Mocking the database response for translation from Russian to German
        HashMap<Integer, List<Integer>> mockTranslations = new HashMap<>();
        mockTranslations.put(1, Arrays.asList(1, 2));
        when(databaseApp.getAllTranslationsRusDe()).thenReturn(mockTranslations);

        HashMap<Integer, List<Integer>> translations = vocabulary.getTranslation(databaseApp, "russian", "german");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertTrue(translations.containsKey(1));
        assertEquals(Arrays.asList(1, 2), translations.get(1));
    }

}
