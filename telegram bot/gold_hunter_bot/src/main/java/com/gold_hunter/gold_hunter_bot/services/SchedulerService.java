package com.gold_hunter.gold_hunter_bot.services;

import com.gold_hunter.gold_hunter_bot.Bot;
import com.gold_hunter.gold_hunter_bot.handlers.StartMenuHandler;
import com.gold_hunter.gold_hunter_bot.models.Order;
import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.repositorys.OrderRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchedulerService {

    private final OrderRepository orderRepository;
    private final TelegramUserRepository userRepository;
    private final SessionFactory sessionFactory;

    public SchedulerService(OrderRepository orderRepository, TelegramUserRepository userRepository, SessionFactory sessionFactory) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.sessionFactory = sessionFactory;
    }

    @Scheduled(initialDelayString = "40000", fixedDelayString = "40000")
    private void orderCompleted() {
        Iterable<Order> orders = orderRepository.findAllByStateAndStateTelegramLessThan(4, 5);

        if (orders.iterator().hasNext()) {
            Bot bot = new Bot(null, null);
            SendMessage sendMessage = new SendMessage();
            Session session = sessionFactory.openSession();

            try {
                session.beginTransaction();

                for (Order order : orders) {
                    TelegramUser user = userRepository.findByUserId(order.getExecutor());
                    float sum = order.getPrice() * order.getAmount();

                    order.setStateTelegram(5);
                    user.setCompletedOrders(user.getCompletedOrders() + 1);
                    user.setSum(user.getSum() + round(sum));

                    session.saveOrUpdate(user);
                    session.saveOrUpdate(order);

                    session.getTransaction().commit();

                    sendMessage.setChatId(order.getExecutor());
                    sendMessage.setText("Поздравляем, Вы выполнили заказ " + order.getId());

                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException ignored) {
                    }
                }
            } catch (Exception e) {
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
        }
    }

    @Scheduled(initialDelayString = "45000", fixedDelayString = "45000")
    private void orderCancel() {
        Iterable<Order> orders = orderRepository.findAllByStateAndStateTelegramLessThan(6, 6);

        if (orders.iterator().hasNext()) {
            Bot bot = new Bot(null, null);
            SendMessage sendMessage = new SendMessage();

            for (Order order : orders) {

                if (order.getExecutor() != null) {
                    sendMessage.setChatId(order.getExecutor());
                    sendMessage.setText("Заказчик отменил заказ " + order.getId());

                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException ignored) { }
                }

                order.setStateTelegram(6);
                order.setExecutor(null);
                order.setExecutorTime(0);
                orderRepository.save(order);
            }
        }
    }

    @Scheduled(initialDelayString = "1800000", fixedDelayString = "1800000")
    private void orderFailed() {
        Iterable<Order> orders = orderRepository.findAllByStateGreaterThanAndStateLessThan(1, 4);

        for (Order order : orders) {
            if (order.getExecutorTime() != 0 && order.getExecutorTime() <= System.currentTimeMillis() - 10800000 && order.getStateTelegram() < 4) {
                Bot bot = new Bot(null, null);
                SendMessage sendMessage = new SendMessage();
                TelegramUser user = userRepository.findByUserId(order.getExecutor());

                sendMessage.setChatId(order.getExecutor());
                sendMessage.setText("Вы провалили заказ " + order.getId() + "\nОн будет передан другим поставщикам, если заказ был провален по вине заказчика, обратитесь в тех. поддержку: @Gold_Hunter_Support");

                user.setFailedOrders(user.getFailedOrders() + 1);
                userRepository.save(user);

                order.setStateTelegram(0);
                order.setExecutor(null);
                order.setExecutorTime(0);
                order.setState(1);
                order.setStateNotification(1);
                orderRepository.save(order);

                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException ignored) {
                }
            }
        }
    }

    @Scheduled(initialDelayString = "3600000", fixedDelayString = "3600000")
    private void blockNotification() {
        Iterable<TelegramUser> users = userRepository.findAllByFailedOrders(2);

        if (users.iterator().hasNext()) {
            Bot bot = new Bot(null, null);
            SendMessage sendMessage = new SendMessage();
            StartMenuHandler menuHandler = new StartMenuHandler();

            for (TelegramUser user : users) {
                sendMessage.setChatId(user.getUserId());
                sendMessage.setText("Вы были заблокированы из-за 2 проваленных заказов. Теперь Вы не можете брать новые заказы.\n\nЕсли заказы были провалены не по Вашей вине, напишите в тех. поддержку: @Gold_Hunter_Support");

                user.setStateSearch(false);
                user.setFailedOrders(3);
                userRepository.save(user);

                try {
                    bot.execute(menuHandler.keyboardButtons(user.getUserId(), "ban", false));
                    bot.execute(sendMessage);
                } catch (TelegramApiException ignored) {
                }
            }
        }
    }

    @Scheduled(initialDelayString = "80000", fixedDelayString = "80000")
    private void readyToExchange() {
        Iterable<Order> orders = orderRepository.findAllByStateAndStateTelegram(3, 2);

        if (orders.iterator().hasNext()) {
            Bot bot = new Bot(null, null);
            SendMessage sendMessage = new SendMessage();

            for (Order order : orders) {
                order.setExecutorTime(System.currentTimeMillis());
                order.setStateTelegram(3);
                orderRepository.save(order);

                sendMessage.setChatId(order.getExecutor());
                sendMessage.setText("Заказчик готов к обмену, следуйте инструкциям, указанным выше");

                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException ignored) {
                }
            }
        }
    }

    @Scheduled(initialDelayString = "100000", fixedDelayString = "100000")
    private void sendingMessages1() {
        ordersDistribution(0);
    }

    @Scheduled(initialDelayString = "1500000", fixedDelayString = "1500000")
    private void sendingMessages2() {
        ordersDistribution(1);
    }

    private void ordersDistribution(int stateTelegram) {
        Iterable<Order> orders = orderRepository.findAllByStateAndStateTelegram(1, stateTelegram);

        if (orders.iterator().hasNext()) {
            Iterable<TelegramUser> users = userRepository.findAllByStateSearch(true);

            Bot bot = new Bot(null, null);
            SendMessage sendMessage = new SendMessage();
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

            keyboardButton.setText("Принять заказ");
            keyboardButton.setCallbackData("order_state-0");
            rowsInline.add(Collections.singletonList(keyboardButton));

            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);

            for (Order order : orders) {
                sendMessage.setText("Заказ " + order.getId() + " " + "\nИгра:  " + order.getGame() + "\nСервер:  " + order.getServer() + "\nКоличество:  " + order.getAmount() + " " + order.getUnit() + "\nКурс за единицу:  " + round(order.getPrice()) + " руб.");

                for (TelegramUser user : users) {
                    for (String games : user.getGames()) {
                        if (order.getGame().equals(games)) {
                            order.setStateTelegram(1);
                            orderRepository.save(order);

                            sendMessage.setChatId(user.getUserId());

                            try {
                                bot.execute(sendMessage);
                            } catch (TelegramApiException ignored) {
                            }
                        }
                    }
                }
            }
        }
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
