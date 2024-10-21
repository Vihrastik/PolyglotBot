package com.telebot.handlers;

import com.telebot.enums.InputType;

public class InitialState  implements StateHandler {
    @Override
    public HandleResult handle(InputType type, String data) {
        return HandleResult.switchToState(new StartState());
    }

}
