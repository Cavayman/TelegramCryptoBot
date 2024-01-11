package com.example.crypto.bot;

import com.example.crypto.configuration.BotConfig;
import com.example.crypto.model.BotMessages;
import com.example.crypto.service.CryptocurrencyService;
import com.example.crypto.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CryptocurrencyTrackerBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CryptocurrencyService cryptocurrencyService;
    private final UserService userService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    public void sendMessage(
            Long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup, boolean htmlMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        if (htmlMode) sendMessage.setParseMode("HTML");

        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (userService.isUserLimitReached()) {
                sendMessage(chatId, BotMessages.BOT_UNAVAILABLE, getStartKeyboardMarkup(), false);
                return;
            }

            switch (messageText) {
                case "/start" -> {
                    if (userService.doesUserExist(chatId)) {
                        sendMessage(
                                chatId,
                                BotMessages.ANALYSIS_IN_PROGRESS,
                                getStopRestartKeyboardMarkup(),
                                false);
                    } else {
                        sendMessage(
                                chatId,
                                BotMessages.WELCOME_MESSAGE,
                                getStartKeyboardMarkup(),
                                true);
                    }
                }
                case "Start" -> {
                    if (userService.doesUserExist(chatId)) {
                        sendMessage(
                                chatId,
                                BotMessages.ANALYSIS_IN_PROGRESS,
                                getStopRestartKeyboardMarkup(),
                                false);
                    } else {
                        sendMessage(
                                chatId,
                                BotMessages.ANALYSIS_INITIATED,
                                getStopRestartKeyboardMarkup(),
                                false);
                        cryptocurrencyService.startCheck(chatId);
                    }
                }
                case "Restart" -> {
                    sendMessage(
                            chatId,
                            BotMessages.ANALYSIS_RESTARTED,
                            getStopRestartKeyboardMarkup(),
                            false);
                    cryptocurrencyService.restartCheck(chatId);
                }
                case "Stop" -> {
                    sendMessage(
                            chatId, BotMessages.ANALYSIS_STOPPED, getStartKeyboardMarkup(), false);
                    cryptocurrencyService.stopCheck(chatId);
                }
                default -> {
                    sendMessage(
                            chatId, BotMessages.INVALID_MESSAGE, getStartKeyboardMarkup(), false);
                }
            }
        }
    }

    private ReplyKeyboardMarkup getStartKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Start");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup getStopRestartKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Restart");
        row.add("Stop");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
