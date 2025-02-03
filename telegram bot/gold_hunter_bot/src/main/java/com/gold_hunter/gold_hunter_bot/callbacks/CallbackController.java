package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.Bot;
import com.gold_hunter.gold_hunter_bot.repositorys.OrderRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramWalletRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CallbackController {

    private final TelegramUserRepository userRepository;
    private final TelegramWalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final SessionFactory sessionFactory;

    public CallbackController(TelegramUserRepository userRepository, TelegramWalletRepository walletRepository, OrderRepository orderRepository, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.orderRepository = orderRepository;
        this.sessionFactory = sessionFactory;
    }

    public void executorCallbacks(CallbackQuery callbackQuery) throws TelegramApiException {
        Bot bot = new Bot(null, null);
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();
        String text = callbackQuery.getMessage().getText();
        String[] globalCallback = data.split("-");

        switch (globalCallback[0]) {
            case "order_state": {
                OrdersCallback ordersCallback = new OrdersCallback();

                bot.execute(ordersCallback.orderProcessing(chatId, data, text, userRepository, orderRepository));
                break;
            }
            case "button_balance": {
                BalanceCallback balanceCallback = new BalanceCallback();

                bot.execute(balanceCallback.selectWallet(chatId, data, walletRepository));
                break;
            }
            case "add_wallet": {
                WalletCallback walletCallback = new WalletCallback();

                bot.execute(walletCallback.walletSelect(chatId, data));
                break;
            }
            case "game_select": {
                GamesCallback gamesCallback = new GamesCallback();

                bot.execute(gamesCallback.gameSelect(callbackQuery, userRepository));
                break;
            }
            case "withdraw_method": {
                WithdrawCallback withdrawCallback = new WithdrawCallback();

                bot.execute(withdrawCallback.sendMoney(chatId, data, text, userRepository, sessionFactory));
                break;
            }
        }
    }
}
