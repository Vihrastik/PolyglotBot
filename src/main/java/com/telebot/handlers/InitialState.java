package com.telebot.handlers;

import com.telebot.enums.InputType;
import org.jetbrains.annotations.NotNull;

public class InitialState  implements StateHandler {
    @NotNull
    @Override
    public HandleResult handle(InputType type, String data) {
        return HandleResult.switchToState(new StartState());
    }

}
