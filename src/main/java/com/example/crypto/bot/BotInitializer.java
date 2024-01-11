package com.example.crypto.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {
    private final CryptocurrencyTrackerBot cryptoCurrencyTrackerBot;

    @Autowired
    public BotInitializer(CryptocurrencyTrackerBot cryptoCurrencyTrackerBot) {
        this.cryptoCurrencyTrackerBot = cryptoCurrencyTrackerBot;
    }

    @SneakyThrows
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(cryptoCurrencyTrackerBot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
