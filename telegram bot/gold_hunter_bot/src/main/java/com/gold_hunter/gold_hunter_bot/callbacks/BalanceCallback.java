package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.models.TelegramWallet;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramWalletRepository;
import com.gold_hunter.gold_hunter_bot.utils.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceCallback {

    public SendMessage selectWallet(String chatId, String data, TelegramWalletRepository walletRepository) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Впишите сумму для вывода");

        switch (data) {
            case "button_balance-0": {
                TelegramWallet wallet = walletRepository.findByUserId(chatId);

                sendMessage.setText("Выберите кошелёк для вывода");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                if (wallet.getQiwi() != null) {
                    InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                    keyboardButton.setText("Qiwi:  " + wallet.getQiwi());
                    keyboardButton.setCallbackData("button_balance-1");
                    rowsInline.add(Collections.singletonList(keyboardButton));
                }
                if (wallet.getYoomoney() != null) {
                    InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                    keyboardButton.setText("ЮMoney:  " + wallet.getYoomoney());
                    keyboardButton.setCallbackData("button_balance-2");
                    rowsInline.add(Collections.singletonList(keyboardButton));
                }
                if (wallet.getCard() != null) {
                    InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                    keyboardButton.setText("Банковская карта:  " + wallet.getCard());
                    keyboardButton.setCallbackData("button_balance-3");
                    rowsInline.add(Collections.singletonList(keyboardButton));
                }

                markupInline.setKeyboard(rowsInline);
                sendMessage.setReplyMarkup(markupInline);
                break;
            }
            case "button_balance-1": {
                BotState.userState.put(chatId, BotState.ASK_SUM_WITHDRAWAL_0);
                break;
            }
            case "button_balance-2": {
                BotState.userState.put(chatId, BotState.ASK_SUM_WITHDRAWAL_1);
                break;
            }
            case "button_balance-3": {
                BotState.userState.put(chatId, BotState.ASK_SUM_WITHDRAWAL_2);
                break;
            }
        }

        return sendMessage;
    }


}
