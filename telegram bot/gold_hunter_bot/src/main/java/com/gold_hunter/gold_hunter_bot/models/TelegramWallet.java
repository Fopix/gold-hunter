package com.gold_hunter.gold_hunter_bot.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "telegram_wallets")
public class TelegramWallet {

    public TelegramWallet(String userId, String qiwi, String yoomoney, String card) {
        this.userId = userId;
        this.qiwi = qiwi;
        this.yoomoney = yoomoney;
        this.card = card;
    }

    public TelegramWallet() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    private String qiwi;

    private String yoomoney;

    private String card;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQiwi() {
        return qiwi;
    }

    public void setQiwi(String qiwi) {
        this.qiwi = qiwi;
    }

    public String getYoomoney() {
        return yoomoney;
    }

    public void setYoomoney(String yoomoney) {
        this.yoomoney = yoomoney;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
