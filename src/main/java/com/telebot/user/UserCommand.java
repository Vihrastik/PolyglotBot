package com.telebot.user;

import com.telebot.enums.CommandId;

public class UserCommand {
    private final String id;
    private final String caption;

    public UserCommand(String id, String text) {
        this.id = id;
        this.caption = text;
    }

    public UserCommand(CommandId id, String text) {
        this(id.name(), text);
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }
}
