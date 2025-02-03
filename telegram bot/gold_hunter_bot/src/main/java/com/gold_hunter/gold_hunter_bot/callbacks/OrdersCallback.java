package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.models.Order;
import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.repositorys.OrderRepository;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import com.gold_hunter.gold_hunter_bot.utils.GameLists;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersCallback {

    public SendMessage orderProcessing(String chatId, String data, String text, TelegramUserRepository userRepository, OrderRepository orderRepository) {
        SendMessage sendMessage = new SendMessage();
        String[] orderId = text.split(" ", 4);
        TelegramUser user = userRepository.findByUserId(chatId);

        sendMessage.setChatId(chatId);

        if (data.equals("order_state-0")) {
            Iterable<Order> orders = orderRepository.findAllByExecutor(chatId);

            for (Order order : orders) {
                if (order.getState() != 4 && (order.getExecutorTime() == 0 || order.getExecutorTime() > System.currentTimeMillis() - 10800000)) {
                    sendMessage.setText("Вы не можете брать более одного заказа\nДождитесь, когда заказчик завершит заказ или пройдёт 3 часа");
                    return sendMessage;
                }
            }

            Order order = orderRepository.findById(Integer.parseInt(orderId[1]));

            if (order != null && order.getStateTelegram() == 1 && user.getFailedOrders() < 2) {
                //логика
                order.setStateTelegram(2);
                order.setState(2);
                order.setExecutor(chatId);
                if (order.getDelivery().equals("Почта")) {
                    order.setExecutorTime(System.currentTimeMillis());
                    sendMessage.setText("Детали заказа " + order.getId() + " " + "\nНикнейм:  " + order.getNickname() + "\nТип доставки:  " + order.getDelivery() + "\nКак будет происходить обмен:  " + getDeliveryMethod(order.getGame(), order.getDelivery()));
                } else {
                    sendMessage.setText("Детали заказа " + order.getId() + " " + "\nНикнейм:  " + order.getNickname() + "\nТип доставки:  " + order.getDelivery() + "\nКак будет происходить обмен:  " + getDeliveryMethod(order.getGame(), order.getDelivery()) + "\n\nНе начинайте выполнять заказ, пока заказчик не подтвердит свою готовность!");
                }

                orderRepository.save(order);


                //сообщение
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();

                keyboardButton.setText("Подтвердить выполнение");
                keyboardButton.setCallbackData("order_state-1");
                rowsInline.add(Collections.singletonList(keyboardButton));

                markupInline.setKeyboard(rowsInline);
                sendMessage.setReplyMarkup(markupInline);
            } else if (order == null) {
                sendMessage.setText("Заказ не существует");
            } else if (order.getStateTelegram() != 1) {
                sendMessage.setText("Заказ уже кто-то взял");
            }
        } else if (data.equals("order_state-1")) {
            Order order = orderRepository.findById(Integer.parseInt(orderId[2]));

            if (order != null && (order.getStateTelegram() == 2 && order.getDelivery().equals("Почта") || order.getStateTelegram() == 3)) {
                order.setStateTelegram(4);
                orderRepository.save(order);

                sendMessage.setText("Вы подтвердили выполнение заказа, дождитесь, когда заказчик подтвердит выполнение со своей стороны, чтобы получить деньги");
            } else if (order != null && (order.getStateTelegram() == 2 && !order.getDelivery().equals("Почта"))) {
                sendMessage.setText("Вы пока что не можете подтвердить заказ");
            }
        }

        return sendMessage;
    }

    private String getDeliveryMethod(String game, String delivery) {
        String deliveryMethod;

        if (!delivery.equals("Почта")) {
            deliveryMethod = GameLists.gameDeliveries.get(game);
        } else {
            deliveryMethod = "Заказ передаётся через почту в игре";
        }

        return deliveryMethod;
    }
}
