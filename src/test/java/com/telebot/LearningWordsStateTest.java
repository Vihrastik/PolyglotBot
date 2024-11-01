package com.telebot;

import com.telebot.enums.InputType;
import com.telebot.enums.SupportedLanguages;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.LearningWordsState;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

public class LearningWordsStateTest {

    private LearningWordsState learningWordsState;
    private UserContext testUserContext;

    @BeforeEach
    public void setUp() {
        testUserContext = new UserContext("TestUser");
        testUserContext.setTopic("Charakterzug");
        testUserContext.setFromLang(SupportedLanguages.ENGLISH);
        testUserContext.setToLang(SupportedLanguages.GERMAN);
        learningWordsState = new LearningWordsState();
    }

    @Test
    public void testEnter() throws Exception {
        UserResponse response = learningWordsState.enter(testUserContext);
        assertNotNull(response);
        assertEquals(String.format("Let's start with topic *Charakterzug*\n\nTranslate *%s*", learningWordsState.getCurrentWord()), response.getText());
    }

    @Test
    public void testHandleCorrectWord() {
        Map<String, List<String>> wordsByTopic = new HashMap<>();
        wordsByTopic.put("word1", new ArrayList<>(Arrays.asList("word2", "word3")));
        ReflectionTestUtils.setField(learningWordsState, "wordsByTopic", wordsByTopic);
        ReflectionTestUtils.setField(learningWordsState, "currentTranslation", List.of("word0"));

        HandleResult result = learningWordsState.handle(InputType.TEXT, "word0");
        assertNotNull(result);
        assertEquals("Correct! It is *word0*\n\nTranslate *word1*", result.response.getText());
    }

    @Test
    public void testHandleIncorrectWord() {
        ReflectionTestUtils.setField(learningWordsState, "currentTranslation", List.of("word1"));
        ReflectionTestUtils.setField(learningWordsState, "userAttempts", 3);

        HandleResult result = learningWordsState.handle(InputType.TEXT, "wrongWord");
        assertNotNull(result);
        assertEquals("Nope. Try again.\nYou've got *2* attempts more", result.response.getText());
    }

    @Test
    public void testHandleExceededAttempts() {
        Map<String, List<String>> wordsByTopic = new HashMap<>();
        wordsByTopic.put("word1", new ArrayList<>(Arrays.asList("word2", "word3")));
        ReflectionTestUtils.setField(learningWordsState, "userAttempts", 0);
        ReflectionTestUtils.setField(learningWordsState, "currentTranslation", List.of("word0"));
        ReflectionTestUtils.setField(learningWordsState, "wordsByTopic", wordsByTopic);

        HandleResult result = learningWordsState.handle(InputType.TEXT, "word0");
        assertNotNull(result);
        assertEquals("Correct! It is *word0*\n\nTranslate *word1*", result.response.getText());
    }
}

