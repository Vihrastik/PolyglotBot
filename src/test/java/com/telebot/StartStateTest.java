package com.telebot;

import com.telebot.enums.CommandId;
import com.telebot.enums.InputType;
import com.telebot.handlers.ChooseLangState;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.StartState;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class StartStateTest {

    private StartState startState;
    private UserContext testUserContext;

    @BeforeEach
    public void setUp() {
        startState = new StartState();
        testUserContext = new UserContext("TestUser");
    }

    @Test
    public void testEnter() {
        UserResponse response = startState.enter(testUserContext);
        assertNotNull(response);
        assertNotNull(response.getText());
        assertTrue(response.getText().contains("Glad to see you, TestUser!"));
        assertFalse(response.getCommands().isEmpty());
        assertEquals(CommandId.BEGIN.toString(), response.getCommands().get(0).getId());
    }

    @Test
    public void testHandleBeginCommand() {
        HandleResult result = startState.handle(InputType.COMMAND, String.valueOf(CommandId.BEGIN));
        assertNotNull(result);
        assertNotNull(result.newState);
        assertInstanceOf(ChooseLangState.class, result.newState);
    }

    @Test
    public void testHandleOtherInput() {
        HandleResult result = startState.handle(InputType.TEXT, "random text");

        assertNotNull(result);
        assertNull(result.newState);
        assertNotNull(result.response);
        assertNotNull(result.response.getText());
        assertFalse(result.response.getText().isEmpty());
    }
}
