package com.gold_hunter.gold_hunter.services;

import com.gold_hunter.gold_hunter.models.Order;
import com.gold_hunter.gold_hunter.repositorys.OrderRepository;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.exception.BadResponseException;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private final OrderRepository orderRepository;
    private final MailSender mailSender;

    private final String secretKey = "eyJ2ZXJzaW9uIjoiUDJQIiwiZGF0YSI6eyJwYXlpbl9tZXJjaGFudF9zaXRlX3VpZCI6IjNyaXJlei0wMCIsInVzZXJfaWQiOiI3OTg4MDA2MTQ1OSIsInNlY3JldCI6IjZkZTIwMWI1MjBmYTdiNmZiNzMyZGNkMDdlNzE2NjEwZDhlZmM1NTkwMzBiOTg1MDY0NjIwYTUwZDdlYjQ1ZGIifX0=";
    private final BillPaymentClient client = BillPaymentClientFactory.createDefault(secretKey);

    public SchedulerService(OrderRepository orderRepository, MailSender mailSender) {
        this.orderRepository = orderRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(initialDelayString = "43200000", fixedDelayString = "43200000")
    private void dataBaseCleaning() {
        Iterable<Order> orders1 = orderRepository.findAllByLifeTimeLessThanAndState(System.currentTimeMillis() - 172800000, 0);
        Iterable<Order> orders2 = orderRepository.findAllByLifeTimeLessThan(System.currentTimeMillis() - 1296000000);

        orderRepository.deleteAll(orders1);
        orderRepository.deleteAll(orders2);
    }

    @Scheduled(initialDelayString = "5400000", fixedDelayString = "5400000")
    private void executeOrderAutomatically() {
        Iterable<Order> orders = orderRepository.findAllByStateLessThanAndStateTelegramAndLifeTimeLessThan(4, 4, System.currentTimeMillis() - 172800000);

        for (Order order : orders) {
            order.setState(4);
            orderRepository.save(order);
        }
    }

    @Scheduled(initialDelayString = "150000", fixedDelayString = "150000")
    private void paymentVerification() {
        Iterable<Order> orders = orderRepository.findAllByStateAndPaymentSystemNotAndBillIdNotNull(0, "ЮMoney");

        for (Order order : orders) {
            String status;

            try {
                BillResponse billResponse = client.getBillInfo(order.getBillId());

                status = billResponse.getStatus().getValue().getValue();
            } catch (BadResponseException responseBody) {
                String[] response = responseBody.getMessage().split("\"", 25);

                status = response[23];
            }

            if (status.equals("PAID")) {
                order.setState(1);
                order.setLifeTime(System.currentTimeMillis());
                orderRepository.save(order);

                IssuingChecks issuingChecks = new IssuingChecks();
                issuingChecks.getToken();
                String receipt = issuingChecks.newTransaction(order.getAmount() + order.getUnit() + " для " + order.getGame(), order.getSum());

                mailSender.send(order.getEmail(), "Чек заказа #" + order.getId(), "Вы успешно оплатили заказ", order.getUrl(), "", receipt);
            }
        }
    }

    @Scheduled(initialDelayString = "30000", fixedDelayString = "30000")
    private void checkNotification() {
        Iterable<Order> orders = orderRepository.findAllByStateGreaterThanEqual(1);

        for (Order order : orders) {
            if (order.getState() == 1 && order.getStateNotification() == 0) {
                mailSender.send(order.getEmail(), "Вы оплатили заказ #" + order.getId(), "Вы успешно оплатили заказ", order.getUrl(), "Чтобы посмотреть статус заказа, перейдите на страницу заказа", "");

                order.setStateNotification(1);
                orderRepository.save(order);
            } else if ((order.getState() == 2 || order.getState() == 3) && order.getStateNotification() == 1 && !order.getDelivery().equals("Почта")) {
                mailSender.send(order.getEmail(), "Мы готовы для обмена", "Мы готовы для обмена", order.getUrl(), "Наш оператор готов выполнить Ваш заказ, для того чтобы продолжить, подтвердите действия на странице заказа", "");

                order.setStateNotification(2);
                orderRepository.save(order);
            } else if (order.getState() == 4 && order.getStateNotification() != 4) {
                mailSender.send(order.getEmail(), "Заказ успешно выполнен", "Заказ успешно выполнен", order.getUrl(), "Будем рады видеть Вас снова, посоветуйте нас своим друзьям, если считаете, что мы хорошо выполнили свою работу.<br></br><br></br>Если не сложно оставьте отзыв о проделанной работе!", "");

                order.setStateNotification(4);
                orderRepository.save(order);
            }
        }
    }
}
