package com.telebot.handlers;

import com.telebot.enums.CommandId;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.enums.InputType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface StateHandler {
    @Nullable
    default UserResponse enter(UserContext userContext) throws Exception {
        return null;
    }

    static boolean isCommand(InputType type, String commandId, CommandId expectedId) {
        return type == InputType.COMMAND && expectedId.name().equals(commandId);
    }

    @Nonnull
    HandleResult handle(InputType type, String data);
}
