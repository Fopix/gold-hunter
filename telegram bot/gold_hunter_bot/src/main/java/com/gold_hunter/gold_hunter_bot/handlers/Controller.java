package com.gold_hunter.gold_hunter_bot.handlers;

import com.gold_hunter.gold_hunter_bot.Bot;
import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.models.TelegramWallet;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramPasswordRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramWalletRepository;
import com.gold_hunter.gold_hunter_bot.utils.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Controller {

    private final TelegramUserRepository userRepository;
    private final TelegramPasswordRepository passwordRepository;
    private final TelegramWalletRepository walletRepository;

    public Controller(TelegramUserRepository userRepository, TelegramPasswordRepository passwordRepository, TelegramWalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.walletRepository = walletRepository;
    }

    public void executorHandlers(Message message) throws TelegramApiException {
        Bot bot = new Bot(null, null);
        String chatId = message.getChatId().toString();
        String text = message.getText();

        if (BotState.userState.get(chatId) != null) {
            switch (BotState.userState.get(chatId)) {
                case DEFAULT: {
                    break;
                }
                case ASK_WALLET_0: {
                    WalletHandler walletHandler = new WalletHandler();

                    bot.execute(walletHandler.qiwiCheck(chatId, text, walletRepository));
                    break;
                }
                case ASK_WALLET_1: {
                    WalletHandler walletHandler = new WalletHandler();

                    bot.execute(walletHandler.walletCheck(chatId, text, 1, walletRepository));
                    break;
                }
                case ASK_WALLET_2: {
                    WalletHandler walletHandler = new WalletHandler();

                    bot.execute(walletHandler.walletCheck(chatId, text, 2, walletRepository));
                    break;
                }
                case ASK_SUM_WITHDRAWAL_0: {
                    bot.execute(withdrawMoney(chatId, 0, text));
                    break;
                }
                case ASK_SUM_WITHDRAWAL_1: {
                    bot.execute(withdrawMoney(chatId, 1, text));
                    break;
                }
                case ASK_SUM_WITHDRAWAL_2: {
                    bot.execute(withdrawMoney(chatId, 2, text));
                    break;
                }
                case ASK_PASSWORD: {
                    PasswordHandler passwordHandler = new PasswordHandler();
                    SendMessage sendMessage = passwordHandler.passwordVerification(chatId, text, passwordRepository);

                    bot.execute(sendMessage);

                    if (sendMessage.getText().equals("Пароль принят")) {
                        TelegramUser user = new TelegramUser(chatId, null, 0, 0, 0, false);
                        userRepository.save(user);

                        TelegramWallet wallet = new TelegramWallet();

                        wallet.setUserId(sendMessage.getChatId());
                        walletRepository.save(wallet);

                        BotState.userState.put(chatId, BotState.DEFAULT);

                        StartMenuHandler menuHandler = new StartMenuHandler();

                        bot.execute(menuHandler.keyboardButtons(chatId, text, false));
                    }
                    break;
                }
            }
        }

        switch (text) {
            case "Бот выключен":
            case "Бот включен": {
                TelegramUser user = userRepository.findByUserId(chatId);

                if (user.getFailedOrders() < 2) {
                    BotState.userState.put(chatId, BotState.DEFAULT);
                    StartMenuHandler menuHandler = new StartMenuHandler();

                    user.setStateSearch(text.equals("Бот выключен"));
                    userRepository.save(user);

                    bot.execute(menuHandler.keyboardButtons(chatId, text, text.equals("Бот выключен")));
                }
                break;
            }
            case "Баланс": {
                BotState.userState.put(chatId, BotState.DEFAULT);

                BalanceHandler balanceHandler = new BalanceHandler();

                bot.execute(balanceHandler.showBalance(chatId, userRepository));
                break;
            }
            case "Игры": {
                BotState.userState.put(chatId, BotState.DEFAULT);
                GamesHandler game = new GamesHandler();

                bot.execute(game.gameSelection(chatId, userRepository));
                break;
            }
            case "Кошельки": {
                BotState.userState.put(chatId, BotState.DEFAULT);
                WalletHandler wallet = new WalletHandler();

                bot.execute(wallet.walletButtons(chatId));
                break;
            }
            case "Инфо": {
                BotState.userState.put(chatId, BotState.DEFAULT);
                InfoHandler info = new InfoHandler();

                bot.execute(info.infoText(chatId));
                break;
            }
            case "/start":
            case "/menu": {
                if (userRepository.existsByUserId(chatId)) {
                    BotState.userState.put(chatId, BotState.DEFAULT);
                    StartMenuHandler menuHandler = new StartMenuHandler();
                    TelegramUser telegramUser = userRepository.findByUserId(chatId);

                    bot.execute(menuHandler.keyboardButtons(chatId, text, telegramUser.getStateSearch()));
                } else {
                    PasswordHandler passwordHandler = new PasswordHandler();

                    bot.execute(passwordHandler.startText(chatId));

                    BotState.userState.put(chatId, BotState.ASK_PASSWORD);
                }
                break;
            }
        }
    }

    private SendMessage withdrawMoney(String chatId, int typeMessage, String sum) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        float userSum = userRepository.findByUserId(chatId).getSum();
        float parseSum;

        try {
            parseSum = round(Float.parseFloat(sum));

            if (parseSum < 1) {
                sendMessage.setText("Сумма не может быть меньше 1.00");
                return sendMessage;
            } else if (userSum < parseSum) {
                sendMessage.setText("Недостаточно средств для вывода");
                return sendMessage;
            }
        } catch (NumberFormatException e) {
            sendMessage.setText("Сумма введена не корректно");
            return sendMessage;
        }

        TelegramWallet wallet = walletRepository.findByUserId(chatId);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

        switch (typeMessage) {
            case 0: {
                sendMessage.setText("Qiwi:  " + wallet.getQiwi() + " " + "\nСумма:  " + parseSum + " руб." + "\nВсё верно?");

                keyboardButton.setCallbackData("withdraw_method-0");
                break;
            }
            case 1: {
                sendMessage.setText("ЮMoney:  " + wallet.getYoomoney() + " " + "\nСумма:  " + parseSum + " руб." + "\nВсё верно?");

                keyboardButton.setCallbackData("withdraw_method-1");
                break;
            }
            case 2: {
                sendMessage.setText("Банковская карта: " + wallet.getCard() + " " + "\nСумма:  " + parseSum + " руб." + "\nВсё верно?");

                keyboardButton.setCallbackData("withdraw_method-2");
                break;
            }
        }

        keyboardButton.setText("Отправить");
        rowsInline.add(Collections.singletonList(keyboardButton));

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        BotState.userState.put(chatId, BotState.DEFAULT);

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
