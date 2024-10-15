package com.telebot;

import com.telebot.common.LanguageChecker;
import com.telebot.common.PhraseGenerator;
import com.telebot.enums.InputType;
import com.telebot.user.UserResponse;
import com.telebot.user.UserState;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class TelebotApplication implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final ConcurrentHashMap<Long, UserState> userState = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TelebotApplication.class);
    private record TeleInput(InputType type, String data) {
    }

    public TelebotApplication(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    public void consume(Update update) {

        Chat chat = getChat(update);
        if (chat == null) {
            logger.warn("Received update without chat. Update: {}", update);
            return;
        }
        logger.info("Processing update for chat id: {}", chat.getId());
        UserState currentUserState = getUserState(chat);

        var message = parseUpdate(update);

        processMessage(currentUserState, chat, message);

    }

    @Nullable
    private Chat getChat(Update message) {
        if (message.hasMessage()) {
            return message.getMessage().getChat();
        }
        if (message.hasCallbackQuery()) {
            return message.getCallbackQuery().getMessage().getChat();
        }
        return null;
    }

    private UserState getUserState(Chat chat) {
        return userState.computeIfAbsent(chat.getId(), id -> new UserState(chat.getFirstName()));
    }

    @Nullable
    private TeleInput parseUpdate(Update message) {
        if (message.hasMessage()) {
            return new TeleInput(InputType.TEXT, message.getMessage().getText());
        }
        if (message.hasCallbackQuery()) {
            return new TeleInput(InputType.COMMAND, message.getCallbackQuery().getData());
        }
        return null;
    }


    private void processMessage(UserState userState, Chat chat, TeleInput message) {
        try {
            // Determine the message type and select the appropriate processing
            List<SendMessage> responses = handleMessage(userState, chat, message);

            // Send every response with retry logic on error
            for (SendMessage response : responses) {
                sendResponseWithRetry(response);
            }
        } catch (Exception e) {
            logger.error("Error processing update", e);
        }
    }

    // Method to process a message depending on its type and content
    private List<SendMessage> handleMessage(UserState userState, Chat chat, TeleInput message) throws Exception {
        if (LanguageChecker.isFWords(message.data())) {
            //If the message contains f words, send a random response
            return convertToTeleResponses(chat, new UserResponse(PhraseGenerator.getRandomFWordPhrase()));
        } else {
            // Processing based on current user state and message type
            UserResponse result = userState.handle(message.type(), message.data());
            return convertToTeleResponses(chat, result);
        }
    }

    private void sendResponseWithRetry(SendMessage response) {
        int retries = 3;
        while (retries-- > 0) {
            try {
                telegramClient.execute(response);
                return;
            } catch (TelegramApiException e) {
                if (retries == 0) {
                    logger.error("Failed to send message after retries", e);
                } else {
                    logger.warn("Retrying to send message... Retries left: {}", retries);
                }
            }
        }
    }

    private List<SendMessage> convertToTeleResponses(Chat chat, UserResponse response) {
        var replyMarkup = InlineKeyboardMarkup.builder();
        for (var cmd : response.getCommands()) {
            var btn = InlineKeyboardButton.builder()
                    .text(cmd.getCaption())
                    .callbackData(cmd.getId())
                    .build();
            replyMarkup.keyboardRow(new InlineKeyboardRow(btn));
        }
        return List.of(
                SendMessage.builder()
                        .chatId(chat.getId())
                        .text(response.getText())
                        .replyMarkup(replyMarkup.build())
                        .parseMode("Markdown")
                        .build()
        );
    }


}