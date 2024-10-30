package com.telebot;

import com.telebot.enums.InputType;
import com.telebot.handlers.ChooseTopicState;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.LearningWordsState;
import com.telebot.user.UserCommand;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
public class ChooseTopicStateTest {

    private ChooseTopicState chooseTopicState;
    private UserContext testUserContext;


    @BeforeEach
    public void setUp() {
        chooseTopicState = new ChooseTopicState();
        testUserContext = new UserContext("TestUser");
    }

    @Test
    public void testEnter() throws Exception {
        testUserContext.setTopic(null);
        UserResponse response = chooseTopicState.enter(testUserContext);
        assertNotNull(response);
        assertEquals("Choose topic:", response.getText());
        assertNotNull(response.getCommands());
    }

    @Test
    public void testHandleWithNonCommandInput() {
        ReflectionTestUtils.setField(chooseTopicState, "userContext", testUserContext);
        HandleResult result = chooseTopicState.handle(InputType.TEXT, "Some Command");
        assertNotNull(result);
        assertNull(result.newState);
        assertNotNull(result.response);
    }

    @Test
    public void testHandleWithCommandInput() {
        ReflectionTestUtils.setField(chooseTopicState, "userContext", testUserContext);
        HandleResult result = chooseTopicState.handle(InputType.COMMAND, "Sample Topic");
        assertNotNull(result);
        assertNotNull(result.newState);
        assertInstanceOf(LearningWordsState.class, result.newState);
    }

    @Test
    public void testCreateTopicsCommands() throws Exception {
        UserCommand[] commands = chooseTopicState.createTopicsCommands();
        assertNotNull(commands);
    }
}
