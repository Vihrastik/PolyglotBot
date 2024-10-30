package com.telebot;
import com.telebot.common.LanguageChecker;
import com.telebot.common.PhraseGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PhraseGeneratorTest {
    @Test
    public void testCheckRandomButtonPhrase() {
        assertNotNull(PhraseGenerator.getRandomButtonPhrase());
    }

    @Test
    public void testCheckRandomFWordPhrase() {
        assertNotNull(PhraseGenerator.getRandomFWordPhrase());
    }
    @Test
    public void testCheckRandomHalloPhrase() {
        assertNotNull(PhraseGenerator.getRandomHalloPhrase());
    }

}
