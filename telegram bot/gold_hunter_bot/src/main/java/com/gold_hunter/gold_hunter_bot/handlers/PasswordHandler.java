package com.gold_hunter.gold_hunter_bot.handlers;

import com.gold_hunter.gold_hunter_bot.models.TelegramPassword;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramPasswordRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class PasswordHandler {

    public SendMessage startText(String chatId) {

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите пароль для активации бота");

        return sendMessage;
    }

    public SendMessage passwordVerification(String chatId, String text, TelegramPasswordRepository passwordRepository) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (passwordRepository.existsByPassword(text)) {
            TelegramPassword telegramPassword = passwordRepository.findByPassword(text);

            if (telegramPassword.getUserId() == null) {
                telegramPassword.setUserId(chatId);
                passwordRepository.save(telegramPassword);

                sendMessage.setText("Пароль принят");
            } else {
                sendMessage.setText("Этот пароль уже используется");
            }
        } else {
            sendMessage.setText("Неправильный пароль");
        }

        return sendMessage;
    }
}
