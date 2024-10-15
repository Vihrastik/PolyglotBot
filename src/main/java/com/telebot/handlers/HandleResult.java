package com.telebot.handlers;

import com.telebot.user.UserResponse;

public final class HandleResult {
    public final StateHandler newState;
    public final UserResponse response;

    private HandleResult(StateHandler newState, UserResponse sendResponse) {
        this.newState = newState;
        this.response = sendResponse;
    }

    public static HandleResult switchToState(StateHandler newState) {
        return new HandleResult(newState, null);
    }

    public static HandleResult response(UserResponse response) {
        return new HandleResult(null, response);
    }

}
