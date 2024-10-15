package com.telebot.user;

import com.telebot.enums.InputType;
import com.telebot.handlers.InitialState;
import com.telebot.handlers.StateHandler;
import jakarta.annotation.Nullable;

public class UserState {
    private StateHandler state;

    private final UserContext userContext;

    public UserState(String userName) {
        this.state = new InitialState();
        this.userContext = new UserContext(userName);
    }

    //todo create ctor to load user state from database

    /**
     * @param type - type of the data passed in second parameter
     * @param data - message text if type is {@code InputType.TEXT} and command name if {@code InputType.COMMAND}
     * @return response to send
     */
    @Nullable
    public UserResponse handle(InputType type, String data) throws Exception {
        var result = state.handle(type, data);
        if (result.response != null) {
            return result.response;
        }
        this.state = result.newState;
        return this.state.enter(userContext);
    }

}
