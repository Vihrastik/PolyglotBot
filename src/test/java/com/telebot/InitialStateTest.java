package com.telebot;
import com.telebot.enums.InputType;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.InitialState;
import com.telebot.handlers.StartState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class InitialStateTest {

    private InitialState initialState;

    @BeforeEach
    public void setUp() {
        initialState = new InitialState();
    }

    @Test
    public void testHandleSwitchesToStartState() {
        HandleResult result = initialState.handle(InputType.TEXT, "test data");
        assertNotNull(result);
        assertNotNull(result.newState);
        assertInstanceOf(StartState.class, result.newState);
    }

    @Test
    public void testHandleWithDifferentInputTypes() {
        HandleResult resultCommand = initialState.handle(InputType.COMMAND, "/start");
        assertNotNull(resultCommand);
        assertInstanceOf(StartState.class, resultCommand.newState);
        HandleResult resultEmpty = initialState.handle(InputType.TEXT, "");
        assertNotNull(resultEmpty);
        assertInstanceOf(StartState.class, resultEmpty.newState);
    }
}
