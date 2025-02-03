package com.gold_hunter.gold_hunter_bot.handlers;

import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceHandler {

    public SendMessage showBalance(String chatId, TelegramUserRepository userRepository) {
        TelegramUser user = userRepository.findByUserId(chatId);
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Ваш баланс:  " + round(user.getSum()) + " руб.");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

        keyboardButton.setText("Вывести");
        keyboardButton.setCallbackData("button_balance-0");
        rowsInline.add(Collections.singletonList(keyboardButton));

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }

    private static float round(float number) {
        int pow = 10;

        for (int i = 1; i < 2; i++) {
            pow *= 10;
        }

        float tmp = number * pow;

        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}
