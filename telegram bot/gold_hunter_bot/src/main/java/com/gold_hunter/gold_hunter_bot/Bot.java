package com.gold_hunter.gold_hunter_bot;

import com.gold_hunter.gold_hunter_bot.callbacks.CallbackController;
import com.gold_hunter.gold_hunter_bot.handlers.Controller;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private final Controller controller;
    private final CallbackController callbackController;

    public Bot(Controller controller, CallbackController callbackController) {
        this.controller = controller;
        this.callbackController = callbackController;
    }

    @Override
    public String getBotUsername() {
        return "Gold_HunterBot";
    }

    @Override
    public String getBotToken() {
        return "1716967754:AAGKAEmB7LqhYcSKbDbi9cjTXmOT6KEBIkc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                controller.executorHandlers(update.getMessage());
            } catch (TelegramApiException ignored) { }
        } else if (update.hasCallbackQuery()) {
            try {
                callbackController.executorCallbacks(update.getCallbackQuery());
            } catch (TelegramApiException ignored) { }
        }
    }
}