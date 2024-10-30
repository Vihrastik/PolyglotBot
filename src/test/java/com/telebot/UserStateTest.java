package com.telebot;

import com.telebot.enums.InputType;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.InitialState;
import com.telebot.handlers.StartState;
import com.telebot.handlers.StateHandler;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.user.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserStateTest {

    private UserState userState;
    private StateHandler stateHandlerMock;

    @BeforeEach
    public void setUp() {
        stateHandlerMock = mock(StateHandler.class);
    }

    @Test
    public void testConstructor() {
        UserState state = new UserState("testUser");
        assertNotNull(state);
        UserContext userContextCurrent = state.getUserContext();
        assertNotNull(userContextCurrent);
        assertEquals("testUser", userContextCurrent.getUserName());
        assertInstanceOf(InitialState.class, state.getUserState());
    }

    @Test
    public void testHandleWithResponse() throws Exception {
        userState = new UserState("testUser", stateHandlerMock);
        UserResponse testResponse = new UserResponse("Test response");
        when(stateHandlerMock.handle(eq(InputType.TEXT), eq("Hello"))).thenReturn(HandleResult.response(testResponse));
        UserResponse result = userState.handle(InputType.TEXT, "Hello");
        assertNotNull(result);
        assertEquals("Test response", result.getText());
        assertEquals(stateHandlerMock, userState.getUserState());
    }

    @Test
    public void testHandleWithNewState() throws Exception {
        userState = new UserState("testUser", new InitialState());
        UserResponse result = userState.handle(InputType.TEXT, "Hello");
        assertNotNull(result);
        assert userState.getUserState() instanceof StartState;
    }

}

