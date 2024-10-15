package com.telebot.enums;

public enum CommandId {
    SYSTEM_START("/start"),
    RESTART("start"),
    BEGIN;

    private final String text;

    CommandId(String text) {
        this.text = text;
    }

    CommandId() {
        text = this.name();
    }
}