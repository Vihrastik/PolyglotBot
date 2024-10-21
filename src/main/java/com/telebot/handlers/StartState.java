package com.telebot.handlers;

import com.telebot.enums.CommandId;
import com.telebot.user.UserCommand;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.enums.InputType;
import com.telebot.common.PhraseGenerator;
import jakarta.annotation.Nullable;

public class StartState implements StateHandler {
    @Nullable
    @Override
    public UserResponse enter(UserContext userContext) {
        userContext.setFromLang(null);
        userContext.setToLang(null);
        return new UserResponse(
                PhraseGenerator.getRandomHalloPhrase() + String.format("\n\nGlad to see you, %s! \uD83D\uDC7B", userContext.getUserName()),
                new UserCommand(CommandId.BEGIN, "Press to Impress")
        );
    }

    @Override
    public HandleResult handle(InputType type, String data) {
        if (StateHandler.isCommand(type, data, CommandId.BEGIN)) {
            return HandleResult.switchToState(new ChooseLangState());
        }
        return HandleResult.response(new UserResponse(PhraseGenerator.getRandomButtonPhrase()));
    }
}

