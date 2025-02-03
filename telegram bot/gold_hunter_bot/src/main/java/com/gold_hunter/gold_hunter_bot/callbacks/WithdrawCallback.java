package com.gold_hunter.gold_hunter_bot.callbacks;

import com.gold_hunter.gold_hunter_bot.models.TelegramUser;
import com.gold_hunter.gold_hunter_bot.models.Withdrawal;
import com.gold_hunter.gold_hunter_bot.repositorys.TelegramUserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WithdrawCallback {

    public SendMessage sendMoney(String chatId, String data, String text, TelegramUserRepository userRepository, SessionFactory sessionFactory) {
        String[] withdrawalDetails = text.split(" ", 7);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        float userSum = userRepository.findByUserId(chatId).getSum();
        float parseSum;

        try {
            parseSum = round(Float.parseFloat(withdrawalDetails[5]));

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

        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            TelegramUser user = userRepository.findByUserId(chatId);
            Withdrawal withdrawal = new Withdrawal();

            user.setSum(user.getSum() - parseSum);

            withdrawal.setUserId(chatId);
            withdrawal.setSum(parseSum);
            withdrawal.setWallet(withdrawalDetails[2]);

            switch (data) {
                case "withdraw_method-0": {
                    withdrawal.setPaymentSystem("Qiwi");
                    break;
                }
                case "withdraw_method-1": {
                    withdrawal.setPaymentSystem("ЮMoney");
                    break;
                }
                case "withdraw_method-2": {
                    withdrawal.setPaymentSystem("Банковская карта");
                    break;
                }
            }

            session.saveOrUpdate(user);
            session.saveOrUpdate(withdrawal);

            session.getTransaction().commit();

            sendMessage.setText("Заявка на вывод успешно создана\nОбработка заявок происходит каждый день с 18:00 до 19:00 по МСК");
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

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
