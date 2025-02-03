package com.gold_hunter.gold_hunter_bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StartMenuHandler {

    public SendMessage keyboardButtons(String chatId, String text, boolean stateSearch) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        //Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        //Первая строчка клавиатуры
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Бот выключен");

        //Вторая строчка клавиатуры
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("Баланс");

        //Третья строчка клавиатуры
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add("Игры");
        keyboardRow3.add("Кошельки");
        keyboardRow3.add("Инфо");

        //Добавляем все строчки клавиатуры в список
        List<KeyboardRow> keyboard = new ArrayList<>();

        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);

        sendMessage.setChatId(chatId);

        if (stateSearch) {
            keyboardRow1.get(0).setText("Бот включен");

            sendMessage.setText("Запущен поиск заказов");
        } else {
            keyboardRow1.get(0).setText("Бот выключен");

            sendMessage.setText("Поиск заказов приостановлен");
        }

        if (text.equals("/start") || text.equals("/menu")) {
            sendMessage.setText("Меню");
        }

        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }
}
