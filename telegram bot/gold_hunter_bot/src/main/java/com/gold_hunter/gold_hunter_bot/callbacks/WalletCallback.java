package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.utils.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WalletCallback {

    public SendMessage walletSelect(String chatId, String data) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (data) {
            case "add_wallet-0": {
                //Qiwi
                BotState.userState.put(chatId, BotState.ASK_WALLET_0);
                sendMessage.setText("Напишите свой кошелёк Qiwi\nВ формате:  +79998886655");
                break;
            }
            case "add_wallet-1": {
                //YooMoney
                BotState.userState.put(chatId, BotState.ASK_WALLET_1);
                sendMessage.setText("Напишите свой кошелёк ЮMoney\nВ формате:  4100117932989513");
                break;
            }
            case "add_wallet-2": {
                //Visa
                BotState.userState.put(chatId, BotState.ASK_WALLET_2);
                sendMessage.setText("Напишите номер банковской карты\nВ формате:  4276501332864018");
                break;
            }
        }

        return sendMessage;
    }
}
