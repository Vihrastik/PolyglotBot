package com.telebot.handlers;

import com.telebot.user.UserCommand;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.enums.InputType;
import com.telebot.common.PhraseGenerator;
import com.telebot.enums.SupportedLanguages;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ChooseLangState implements StateHandler {
    private UserContext userContext;

    @Nullable
    @Override
    public UserResponse enter(UserContext userContext) {
        this.userContext = userContext;
        userContext.setFromLang(null);
        userContext.setToLang(null);

        return new UserResponse(
                "Choose language to translate from:",
                createLangCommands(null));
    }

    @NotNull
    @Override
    public HandleResult handle(InputType type, String data) {
        var lang = findLanguageByCode(data);
        if (type != InputType.COMMAND || lang == null)  {
            return HandleResult.response(new UserResponse(PhraseGenerator.getRandomButtonPhrase()));
        }
        if (userContext.getFromLang() == null) {
            userContext.setFromLang(lang);
            return HandleResult.response(new UserResponse("Good, choose language you want to master: ", createLangCommands(lang)));
        }
        userContext.setToLang(lang);
        return HandleResult.switchToState(new ChooseTopicState());
    }


    private UserCommand[] createLangCommands(SupportedLanguages except) {
        return Arrays.stream(SupportedLanguages.values())
                .filter(it -> it != except)
                .map(lang -> new UserCommand(lang.langCode(), lang.description()))
                .toArray(UserCommand[]::new);
    }

    private SupportedLanguages findLanguageByCode(String code) {
        for (SupportedLanguages lang : SupportedLanguages.values()) {
            if (lang.langCode().equals(code)) {
                return lang;
            }
        }
        return null;
    }
}