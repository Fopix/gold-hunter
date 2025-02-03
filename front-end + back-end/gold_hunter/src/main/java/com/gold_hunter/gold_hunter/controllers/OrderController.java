package com.gold_hunter.gold_hunter.controllers;

import com.gold_hunter.gold_hunter.models.Order;
import com.gold_hunter.gold_hunter.models.Review;
import com.gold_hunter.gold_hunter.repositorys.OrderRepository;
import com.gold_hunter.gold_hunter.repositorys.ReviewsRepository;
import com.gold_hunter.gold_hunter.services.IssuingChecks;
import com.gold_hunter.gold_hunter.services.MailSender;
import com.gold_hunter.gold_hunter.services.YooMoneyCreateBill;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.exception.BadResponseException;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.UUID;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final ReviewsRepository reviewsRepository;
    private final MailSender mailSender;

    private final String secretKey = "eyJ2ZXJzaW9uIjoiUDJQIiwiZGF0YSI6eyJwYXlpbl9tZXJjaGFudF9zaXRlX3VpZCI6IjNyaXJlei0wMCIsInVzZXJfaWQiOiI3OTg4MDA2MTQ1OSIsInNlY3JldCI6IjZkZTIwMWI1MjBmYTdiNmZiNzMyZGNkMDdlNzE2NjEwZDhlZmM1NTkwMzBiOTg1MDY0NjIwYTUwZDdlYjQ1ZGIifX0=";
    private final BillPaymentClient client = BillPaymentClientFactory.createDefault(secretKey);

    public OrderController(OrderRepository orderRepository, ReviewsRepository reviewsRepository, MailSender mailSender) {
        this.orderRepository = orderRepository;
        this.reviewsRepository = reviewsRepository;
        this.mailSender = mailSender;
    }

    @GetMapping("/orders/{url}")
    public String product(@PathVariable("url") String url, Model model) {
        if (!orderRepository.existsByUrl(url)) return "error";

        Order order = orderRepository.findByUrl(url);
        model.addAttribute("order", order);

        if (order.getState() == 0) {
            if (order.getBillId() == null) {
                String billId = UUID.randomUUID().toString();
                String payUrl;

                if (order.getPaymentSystem().equals("ЮMoney")) {
                    YooMoneyCreateBill yooMoney = new YooMoneyCreateBill();

                    float sumWithCommission = order.getSum() + (order.getSum() * 1 / 100);
                    payUrl = yooMoney.createBill(order.getAmount() + order.getUnit() + " для " + order.getGame(), order.getUrl(), order.getId(), sumWithCommission);

                    order.setBillId(payUrl);
                } else {
                    CreateBillInfo billInfo = new CreateBillInfo(billId, new MoneyAmount(BigDecimal.valueOf(order.getSum()), Currency.getInstance("RUB")), "Gold Hunter Заказ #" + order.getId(), ZonedDateTime.now().plusDays(2), new Customer(order.getEmail(), billId, ""), "https://goldhunter.fun/orders/" + url);

                    try {
                        BillResponse billResponse = client.createBill(billInfo);
                        payUrl = billResponse.getPayUrl();
                    } catch (BadResponseException | URISyntaxException response) {
                        payUrl = response.getMessage().split("\"", 65)[63];
                    }

                    order.setBillId(billId);
                }

                model.addAttribute("payUrl", payUrl);
                orderRepository.save(order);
            } else if (!order.getPaymentSystem().equals("ЮMoney")) {
                String payUrl;
                String status;

                try {
                    BillResponse billResponse = client.getBillInfo(order.getBillId());

                    payUrl = billResponse.getPayUrl();
                    status = billResponse.getStatus().getValue().getValue();
                } catch (BadResponseException responseBody) {
                    String[] response = responseBody.getMessage().split("\"", 65);

                    payUrl = response[63];
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

                    return "order-status";
                }

                model.addAttribute("payUrl", payUrl);
            } else if (order.getPaymentSystem().equals("ЮMoney")) {
                model.addAttribute("payUrl", order.getBillId());
            }

            return "order-information";
        } else {
            return "order-status";
        }
    }

    @GetMapping("/orders/ready/{url}")
    public String readyToTrade(@PathVariable("url") String url) {
        if (!orderRepository.existsByUrl(url)) return "error";

        Order order = orderRepository.findByUrl(url);

        if (order.getState() == 2) {
            order.setState(3);
            orderRepository.save(order);
        }

        return "redirect:/orders/" + url;
    }

    @GetMapping("/orders/completed/{url}")
    public String orderCompleted(@PathVariable("url") String url) {
        if (!orderRepository.existsByUrl(url)) return "error";

        Order order = orderRepository.findByUrl(url);

        if (order.getState() == 3 || (order.getState() == 2 && order.getDelivery().equals("Почта"))) {
            order.setState(4);
            orderRepository.save(order);
        }

        return "redirect:/orders/" + url;
    }

    @PostMapping("/orders/{url}")
    public String sendReview(@PathVariable("url") String url, @RequestParam(defaultValue = "empty") String name, @RequestParam int rating, @RequestParam String message) {
        if (!orderRepository.existsByUrl(url)) return "error";

        if (!name.equals("") && !message.equals("")) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String date = localDate.format(formatter);

            Order order = orderRepository.findByUrl(url);

            if (reviewsRepository.existsByOrderId(order.getId())) {
                Review review = reviewsRepository.findByOrderId(order.getId());

                if (name.equals("empty")) {
                    review.setName("Заказ " + order.getId());
                } else {
                    review.setName(name);
                }
                review.setRating(rating);
                review.setDate(date);
                review.setMessage(message);

                reviewsRepository.save(review);
            } else {
                Review review = new Review(order.getId(), order.getGame(), name, rating, date, message);

                if (name.equals("empty")) {
                    review.setName("Заказ " + order.getId());
                }

                reviewsRepository.save(review);
            }
        }

        return "redirect:/orders/" + url;
    }

    @PostMapping(value = "/orders/confirmationpayment", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String confirmationPayment(@RequestParam String notification_type, @RequestParam String operation_id, @RequestParam float amount, @RequestParam String currency, @RequestParam String datetime, @RequestParam String sender, @RequestParam boolean codepro, @RequestParam String label, @RequestParam String sha1_hash) {
        String notification_secret = "9dggVeDEIJtMIYxi/XCoXztX";
        String allParameters = notification_type + "&" + operation_id + "&" + amount + "&" + currency + "&" + datetime + "&" + sender + "&" + codepro + "&" + notification_secret + "&" + label;
        String myHash = DigestUtils.sha1Hex(allParameters);

        if (myHash.equals(sha1_hash)) {
            Order order = orderRepository.findByUrl(label);

            if (order.getState() == 0) {
                order.setState(1);
                order.setLifeTime(System.currentTimeMillis());
                orderRepository.save(order);

                IssuingChecks issuingChecks = new IssuingChecks();
                issuingChecks.getToken();
                String receipt = issuingChecks.newTransaction(order.getAmount() + order.getUnit() + " для " + order.getGame(), order.getSum());

                mailSender.send(order.getEmail(), "Чек заказа #" + order.getId(), "Вы успешно оплатили заказ", order.getUrl(), "", receipt);
            }
        }

        return "YES";
    }

    @PostMapping(value = "/updater", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String updateStatus(@RequestParam String url, @RequestParam int status) {
        if (status == 0) return "YES";

        if (orderRepository.findByUrl(url).getState() == status) return "NO";

        return "YES";
    }
}