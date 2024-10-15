package com.telebot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String botToken = dotenv.get("BOT_TOKEN");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new TelebotApplication(botToken));
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
