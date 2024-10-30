package com.telebot;
import com.telebot.handlers.HandleResult;
import com.telebot.handlers.StateHandler;
import com.telebot.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class HandleResultTest {

    @Test
    public void testConstructorAndFields() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        StateHandler mockState = mock(StateHandler.class);
        UserResponse mockResponse = mock(UserResponse.class);

        Constructor<HandleResult> constructor = HandleResult.class.getDeclaredConstructor(StateHandler.class, UserResponse.class);
        constructor.setAccessible(true);
        HandleResult result = constructor.newInstance(mockState, mockResponse);

        assertEquals(mockState, result.newState);
        assertEquals(mockResponse, result.response);
    }

    @Test
    public void testSwitchToState() {
        StateHandler mockState = mock(StateHandler.class);
        HandleResult result = HandleResult.switchToState(mockState);
        assertEquals(mockState, result.newState);
        assertNull(result.response);
    }

    @Test
    public void testResponse() {
        UserResponse mockResponse = mock(UserResponse.class);
        HandleResult result = HandleResult.response(mockResponse);
        assertNull(result.newState);
        assertEquals(mockResponse, result.response);
    }
}
