package com.telebot;

import com.telebot.enums.InputType;
import com.telebot.user.UserResponse;
import com.telebot.user.UserState;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TelebotApplicationTest {
    private TelebotApplication telebotApplication;
    private TelegramClient telegramClient;

    @BeforeEach
    public void setUp() {
        telegramClient = mock(TelegramClient.class);
        Dotenv dotenv = Dotenv.load();
        String botToken = dotenv.get("BOT_TOKEN");
        telebotApplication = Mockito.spy(new TelebotApplication(botToken));
        ReflectionTestUtils.setField(telebotApplication, "telegramClient", telegramClient);
    }

    @Test
    public void testGetChat() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);

        Chat resultChat = telebotApplication.getChat(update);
        assertNotNull(resultChat);
        assertEquals(chat, resultChat);
    }

    @Test
    public void testConsume() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        UserState userState = mock(UserState.class);

        doReturn(chat).when(telebotApplication).getChat(update);
        doReturn(userState).when(telebotApplication).getUserState(chat);
        TelebotApplication.TeleInput input = new TelebotApplication.TeleInput(InputType.TEXT, "test message");
        doReturn(input).when(telebotApplication).parseUpdate(update);

        telebotApplication.consume(update);

        verify(telebotApplication, times(1)).processMessage(userState, chat, input);
    }

    @Test
    public void testSendResponseWithRetry() throws Exception {
        SendMessage message = mock(SendMessage.class);
        doReturn(null).when(telegramClient).execute(message);

        telebotApplication.sendResponseWithRetry(message);

        verify(telegramClient, times(1)).execute(message);
    }

    @Test
    public void testSendResponseWithRetryWithFailures() throws Exception {
        SendMessage message = mock(SendMessage.class);
        TelegramApiException apiException = new TelegramApiException("Test Exception");
        doThrow(apiException).when(telegramClient).execute(message);
        telebotApplication.sendResponseWithRetry(message);

        verify(telegramClient, times(3)).execute(message);
    }

    @Test
    public void testProcessMessage() throws Exception {
        Chat chat = mock(Chat.class);
        UserState userState = mock(UserState.class);
        TelebotApplication.TeleInput input = new TelebotApplication.TeleInput(InputType.TEXT, "Hello");

        List<SendMessage> messages = List.of(mock(SendMessage.class));
        doReturn(messages).when(telebotApplication).handleMessage(userState, chat, input);

        telebotApplication.processMessage(userState, chat, input);
        verify(telebotApplication, times(messages.size())).sendResponseWithRetry(any(SendMessage.class));
    }

    @Test
    public void testHandleMessage() throws Exception {
        UserState userState = mock(UserState.class);
        Chat chat = mock(Chat.class);
        TelebotApplication.TeleInput input = new TelebotApplication.TeleInput(InputType.TEXT, "Hello");

        UserResponse mockResponse = mock(UserResponse.class);
        when(mockResponse.getText()).thenReturn("Mocked response text");
        when(userState.handle(input.type(), input.data())).thenReturn(mockResponse);

        List<SendMessage> result = telebotApplication.handleMessage(userState, chat, input);

        assertNotNull(result);
    }
}
