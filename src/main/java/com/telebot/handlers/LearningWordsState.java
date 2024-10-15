package com.telebot.handlers;

import com.telebot.user.UserContext;
import com.telebot.user.UserResponse;
import com.telebot.Vocabulary;
import com.telebot.enums.InputType;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LearningWordsState implements StateHandler {

    private Map<String, List> wordsByTopic;
    private String currentWord;
    private int userAttempts;
    private List<String> currentTranslation;

    @Nullable
    @Override
    public UserResponse enter(UserContext userContext) throws Exception {
        String topic = userContext.getTopic();
        Vocabulary voc = new Vocabulary();
        wordsByTopic = voc.getWordsByThemeAndLanguageJSONFile(topic, userContext.getFromLang().description().toLowerCase(), userContext.getToLang().description().toLowerCase());
        userAttempts = 3;
        getNewWord();
        return new UserResponse(
                String.format("Let's start with topic *%s*\n\nTranslate *%s*", topic, this.currentWord));
    }

    @NotNull
    @Override
    public HandleResult handle(InputType type, String data) {
        if (type == InputType.COMMAND) {
            return HandleResult.response(new UserResponse("Please write the word"));
        }
        if (currentTranslation.stream().anyMatch(word -> word.contains(data))) {
            List<String> guessedTranslation = this.currentTranslation;
            wordsByTopic.remove(this.currentWord);
            getNewWord();
            userAttempts = 3;
            return HandleResult.response(new UserResponse(String.format("Correct! It is *%s*\n\nTranslate *%s*", String.join(", ", guessedTranslation), this.currentWord)));
        } else {
            if (userAttempts > 0) {
                userAttempts--;
                return HandleResult.response(new UserResponse(String.format(String.format("Nope. Try again.\nYou've got *%s* attempts more", userAttempts))));

            } else {
                List<String> guessedTranslation = this.currentTranslation;
                this.getNewWord();
                userAttempts = 3;
                return HandleResult.response(new UserResponse(String.format("Correct is *%s*\n\nTranslate *%s*", String.join(", ", guessedTranslation), this.currentWord)));
            }
        }
    }

    private void getNewWord() {
        int random = (int) (Math.random() * this.wordsByTopic.keySet().size());
        this.currentWord = new ArrayList<>(this.wordsByTopic.keySet()).get(random);
        this.currentTranslation = this.wordsByTopic.get(this.currentWord);
    }

}

