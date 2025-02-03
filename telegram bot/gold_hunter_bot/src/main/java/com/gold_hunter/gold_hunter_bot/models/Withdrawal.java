package com.gold_hunter.gold_hunter_bot.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "withdrawal")
public class Withdrawal {

    public Withdrawal(@NotNull String userId, @NotNull float sum, @NotNull String paymentSystem, @NotNull String wallet) {
        this.userId = userId;
        this.sum = sum;
        this.paymentSystem = paymentSystem;
        this.wallet = wallet;
    }

    public Withdrawal() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotNull
    @Column(name = "sum", nullable = false)
    private float sum;

    @NotNull
    @Column(name = "payment_system", nullable = false)
    private String paymentSystem;

    @NotNull
    @Column(name = "wallet", nullable = false)
    private String wallet;


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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
