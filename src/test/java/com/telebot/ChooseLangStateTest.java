package com.telebot;

import com.telebot.enums.InputType;
import com.telebot.enums.SupportedLanguages;
import com.telebot.handlers.ChooseLangState;
import com.telebot.handlers.HandleResult;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class ChooseLangStateTest {

    private ChooseLangState chooseLangState;
    private UserContext testUserContext;

    @BeforeEach
    void setUp() {
        testUserContext = new UserContext("TestUser");
        chooseLangState = new ChooseLangState();
    }

    @Test
    void testEnter() {
        UserResponse response = chooseLangState.enter(testUserContext);
        assertNotNull(response);
        assertEquals("Choose language to translate from:", response.getText());
    }

    @Test
    void testHandleValidCommand() {
        ReflectionTestUtils.setField(chooseLangState, "userContext", testUserContext);
        testUserContext.setFromLang(SupportedLanguages.RUSSIAN);
        HandleResult result = chooseLangState.handle(InputType.COMMAND, SupportedLanguages.ENGLISH.langCode());
        assertNotNull(result);
        assertNotNull(result.newState);
    }

    @Test
    void testHandleInvalidCommand() {
        String commandData = "INVALID_CODE";
        ReflectionTestUtils.setField(chooseLangState, "userContext", testUserContext);
        testUserContext.setFromLang(SupportedLanguages.RUSSIAN);
        HandleResult result = chooseLangState.handle(InputType.COMMAND, commandData);
        assertNotNull(result);
        assertNotNull(result.response);
    }

    @Test
    void testHandleSettingLanguages(){
        ReflectionTestUtils.setField(chooseLangState, "userContext", testUserContext);
        HandleResult result = chooseLangState.handle(InputType.COMMAND, SupportedLanguages.RUSSIAN.langCode());
        assertNotNull(result);
        assertEquals("Good, choose language you want to master: ", result.response.getText());
    }
}
