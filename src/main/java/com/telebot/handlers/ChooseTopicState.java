package com.telebot.handlers;

import com.telebot.user.UserCommand;
import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.Vocabulary;
import com.telebot.enums.InputType;
import com.telebot.common.PhraseGenerator;
import jakarta.annotation.Nullable;
import java.util.Arrays;

public class ChooseTopicState implements StateHandler {
    private UserContext userContext;
    @Nullable
    @Override
    public UserResponse enter(UserContext userContext) throws Exception {
        this.userContext = userContext;
        userContext.setTopic(null);
        return new UserResponse(
                "Choose topic:",
                createTopicsCommands());
    }

    @Override
    public HandleResult handle(InputType type, String data) {
        if (type != InputType.COMMAND)  {
            return HandleResult.response(new UserResponse(PhraseGenerator.getRandomButtonPhrase()));
        }
        userContext.setTopic(data);
        return HandleResult.switchToState(new LearningWordsState());
    }

    private UserCommand[] createTopicsCommands() throws Exception {
        Vocabulary voc = new Vocabulary();
        var topics = voc.getThemesfromJSONFile();
        return Arrays.stream(topics.toArray())
                .map(topic -> new UserCommand(topic.toString(), topic.toString()))
                .toArray(UserCommand[]::new);
    }

}

