package com.gold_hunter.gold_hunter.controllers;

import com.gold_hunter.gold_hunter.models.Order;
import com.gold_hunter.gold_hunter.models.PaymentMethod;
import com.gold_hunter.gold_hunter.models.Product;
import com.gold_hunter.gold_hunter.repositorys.OrderRepository;
import com.gold_hunter.gold_hunter.repositorys.PaymentMethodRepository;
import com.gold_hunter.gold_hunter.repositorys.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    public ProductController(ProductRepository productRepository, PaymentMethodRepository paymentMethodRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/games/{id}")
    public String product(@PathVariable("id") String urlId, @RequestParam(defaultValue = "empty") String k, Model model) {
        if (!productRepository.existsById(urlId)) return "error";

        Product product = productRepository.findById(urlId).get();
        ArrayList<Float> priceList = new ArrayList<>();

        for (float price : product.getPrice()) {
            priceList.add(price + price * 75 / 100);
        }

        product.setPrice(priceList);

        model.addAttribute("paymentMethod", paymentMethodRepository.findAll());
        model.addAttribute("product", product);

        //пустышка
        Order order = new Order();
        model.addAttribute("order", order);

        if (!k.equals("empty") && orderRepository.existsByUrl(k)) {
            order = orderRepository.findByUrl(k);

            if (order.getState() == 0) {
                model.addAttribute("order", order);
            }
        }

        return "product";
    }

    @PostMapping("/games/{id}")
    public String checkout(@PathVariable("id") String urlId, @RequestParam(defaultValue = "empty") String k, @RequestParam String server, @RequestParam String paymentMethod, @RequestParam String delivery, @RequestParam float payment, @RequestParam String nickname, @RequestParam String email) throws NoSuchAlgorithmException {
        if (!productRepository.existsById(urlId) || email.contains("\"")) return "error";

        List<PaymentMethod> paymentM = paymentMethodRepository.findByPaymentSystem(paymentMethod);
        Product product = productRepository.findById(urlId).get();
        float price = product.getPrice().get(product.getServers().indexOf(server));
        float productPrice = price;
        price = price + price * 75 / 100;

        //Валидация
        if (payment >= 4000) {
            price = price - price * 7f / 100;
        } else if (payment >= 2500) {
            price = price - price * 5f / 100;
        } else if (payment >= 1500) {
            price = price - price * 3f / 100;
        } else if (payment >= 1000) {
            price = price - price * 2f / 100;
        } else if (payment >= 500) {
            price = price - price / 100;
        }

        float amount = round(payment / price);
        boolean minAmount = Math.ceil(300 / price) <= amount;

        if (!nickname.equals("") && !email.equals("") && product.getServers().contains(server) && paymentM.size() != 0 && product.getDelivery().contains(delivery) && minAmount) {
            //Добовление данных в таблицу orders
            String urlOutput;
            Order order;

            order = orderRepository.findByUrl(k);

            //Обновление старой записи или создание новой
            if (!k.equals("empty") && orderRepository.existsByUrl(k) && order.getState() == 0) {
                urlOutput = k;

                order.setGameId(product.getId());
                order.setBillId(null);
                order.setGame(product.getGame());
                order.setServer(server);
                order.setNickname(nickname);
                order.setAmount(amount);
                order.setSum(payment);
                order.setPrice(productPrice);
                order.setUnit(product.getUnit());
                order.setPaymentSystem(paymentMethod);
                order.setEmail(email);
                order.setDelivery(delivery);
                order.setState(0);
                order.setStateNotification(0);
                order.setLifeTime(System.currentTimeMillis());
            } else {
                //Шифрование
                String urlInput = System.currentTimeMillis() + product.getGame() + server + paymentMethod + delivery + payment + nickname + email;
                MessageDigest digester = MessageDigest.getInstance("SHA-256");
                byte[] byteInput = urlInput.getBytes();
                byte[] urlHash = digester.digest(byteInput);
                urlOutput = DatatypeConverter.printHexBinary(urlHash).toLowerCase();

                order = new Order(urlOutput, product.getId(), product.getGame(), server, nickname, amount, payment, productPrice, product.getUnit(), paymentMethod, email, delivery, 0, 0, 0, null, System.currentTimeMillis(), 0L);
            }

            orderRepository.save(order);

            return "redirect:/orders/" + urlOutput;
        } else {
            return "error";
        }
    }

    private static float round(float number) {
        int pow = 10;
        for (int i = 1; i < 2; i++)
            pow *= 10;
        float tmp = number * pow;

        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}