package com.telebot.user;

import jakarta.annotation.Nonnull;

import java.util.List;

public class UserResponse {
    private final String text;
    private final List<UserCommand> commands;

    public UserResponse(@Nonnull String text, UserCommand... commands) {
        this.text = text;
        this.commands = List.of(commands);
    }

    @Nonnull
    public String getText() {
        return text;
    }

    public List<UserCommand> getCommands() {
        return commands;
    }
}
