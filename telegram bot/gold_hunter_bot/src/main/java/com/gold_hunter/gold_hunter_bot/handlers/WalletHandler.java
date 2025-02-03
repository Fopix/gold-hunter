package com.gold_hunter.gold_hunter_bot.handlers;

import com.gold_hunter.gold_hunter_bot.models.TelegramWallet;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramWalletRepository;
import com.gold_hunter.gold_hunter_bot.utils.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WalletHandler {

    public SendMessage walletButtons(String chatId) {

        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        ArrayList<String> wallets = new ArrayList<>();

        wallets.add("Qiwi");
        wallets.add("ЮMoney");
        wallets.add("Банковская карта");

        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите платёжную систему");

        for (int i = 0; i < wallets.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

            keyboardButton.setText(wallets.get(i));
            keyboardButton.setCallbackData("add_wallet-" + i);
            rowsInline.add(Collections.singletonList(keyboardButton));
        }

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }

    public SendMessage qiwiCheck(String chatId, String text, TelegramWalletRepository walletRepository) {

        String[] partText = text.split("", 2);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (partText[0].equals("+")) {
            try {
                Long.parseLong(partText[1]);

                TelegramWallet wallet = walletRepository.findByUserId(chatId);

                wallet.setQiwi(text);
                walletRepository.save(wallet);

                sendMessage.setText("Вы добавили кошелёк Qiwi");
                BotState.userState.put(chatId, BotState.DEFAULT);
            } catch (NumberFormatException e) {
                sendMessage.setText("Неправильная форма кошелька, попробуйте ещё раз");
            }
        } else {
            sendMessage.setText("Неправильная форма кошелька, попробуйте ещё раз");
        }

        return sendMessage;
    }

    public SendMessage walletCheck(String chatId, String text, int walletOption, TelegramWalletRepository walletRepository) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.length() == 16) {
            try {
                Long.parseLong(text);

                TelegramWallet wallet = walletRepository.findByUserId(chatId);

                if (walletOption == 1) {
                    wallet.setYoomoney(text);
                    sendMessage.setText("Вы добавили кошелёк ЮMoney");
                } else if (walletOption == 2) {
                    wallet.setCard(text);
                    sendMessage.setText("Вы добавили банковскую карту");
                }

                walletRepository.save(wallet);

                BotState.userState.put(chatId, BotState.DEFAULT);
            } catch (NumberFormatException e) {
                sendMessage.setText("Неправильная форма кошелька, попробуйте ещё раз");
            }
        } else {
            sendMessage.setText("Неправильная форма кошелька, попробуйте ещё раз");
        }


        return sendMessage;
    }
}
