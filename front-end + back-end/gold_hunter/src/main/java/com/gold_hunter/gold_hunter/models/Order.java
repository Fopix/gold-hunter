package com.gold_hunter.gold_hunter.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "orders")
public class Order {

    public Order(@NotNull String url, @NotNull String gameId, @NotNull String game, @NotNull String server, @NotNull String nickname, @NotNull float amount, @NotNull float sum, @NotNull float price, String unit, @NotNull String paymentSystem, @NotNull String email, @NotNull String delivery, @NotNull int state, @NotNull int stateNotification, @NotNull int stateTelegram, String executor, @NotNull long lifeTime, long executorTime) {
        this.url = url;
        this.gameId = gameId;
        this.game = game;
        this.server = server;
        this.nickname = nickname;
        this.amount = amount;
        this.sum = sum;
        this.price = price;
        this.unit = unit;
        this.paymentSystem = paymentSystem;
        this.email = email;
        this.delivery = delivery;
        this.state = state;
        this.stateNotification = stateNotification;
        this.stateTelegram = stateTelegram;
        this.executor = executor;
        this.lifeTime = lifeTime;
        this.executorTime = executorTime;
    }

    public Order() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "game_id", nullable = false)
    private String gameId;

    @Column(name = "bill_id")
    private String billId;

    @NotNull
    @Column(name = "game", nullable = false)
    private String game;

    @NotNull
    @Column(name = "server", nullable = false)
    private String server;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @NotNull
    @Column(name = "amount", nullable = false)
    private float amount;

    @NotNull
    @Column(name = "sum", nullable = false)
    private float sum;

    @NotNull
    @Column(name = "price", nullable = false)
    private float price;

    private String unit;

    @NotNull
    @Column(name = "payment_system", nullable = false)
    private String paymentSystem;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "delivery", nullable = false)
    private String delivery;

    @NotNull
    @Column(name = "state", nullable = false)
    private int state;

    @NotNull
    @Column(name = "state_notification", nullable = false)
    private int stateNotification;

    @NotNull
    @Column(name = "state_telegram", nullable = false)
    private int stateTelegram;

    private String executor;

    @NotNull
    @Column(name = "life_time", nullable = false)
    private long lifeTime;

    @Column(name = "executor_time")
    private long executorTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStateNotification() {
        return stateNotification;
    }

    public void setStateNotification(int stateNotification) {
        this.stateNotification = stateNotification;
    }

    public int getStateTelegram() {
        return stateTelegram;
    }

    public void setStateTelegram(int stateTelegram) {
        this.stateTelegram = stateTelegram;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) { this.lifeTime = lifeTime; }

    public long getExecutorTime() {
        return executorTime;
    }

    public void setExecutorTime(long executorTime) {
        this.executorTime = executorTime;
    }
}
