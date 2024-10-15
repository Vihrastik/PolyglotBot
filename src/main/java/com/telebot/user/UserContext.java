package com.telebot.user;

import com.telebot.enums.SupportedLanguages;

public class UserContext {
    private final String userName;

    private SupportedLanguages fromLang;
    private SupportedLanguages toLang;
    private String topic;

    public UserContext(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public SupportedLanguages getFromLang() {
        return fromLang;
    }

    public SupportedLanguages getToLang() {
        return toLang;
    }

    public void setFromLang(SupportedLanguages fromLang) {
        this.fromLang = fromLang;
    }

    public void setToLang(SupportedLanguages toLang) {
        this.toLang = toLang;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


}
