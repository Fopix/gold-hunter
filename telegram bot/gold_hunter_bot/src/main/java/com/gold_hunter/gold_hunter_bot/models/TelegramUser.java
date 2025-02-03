package com.gold_hunter.gold_hunter_bot.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "telegram_users")
public class TelegramUser {

    public TelegramUser(@NotNull String userId, String games, @NotNull float sum, @NotNull int completedOrders, @NotNull int failedOrders, @NotNull boolean stateSearch) {
        this.userId = userId;
        this.games = games;
        this.sum = sum;
        this.completedOrders = completedOrders;
        this.failedOrders = failedOrders;
        this.stateSearch = stateSearch;
    }

    public TelegramUser() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "games", columnDefinition = "TEXT")
    private String games;

    @NotNull
    @Column(name = "sum", nullable = false)
    private float sum;

    @NotNull
    @Column(name = "completed_orders", nullable = false)
    private int completedOrders;

    @NotNull
    @Column(name = "failed_orders", nullable = false)
    private int failedOrders;

    @NotNull
    @Column(name = "state_search", nullable = false)
    private boolean stateSearch;


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

    public List<String> getGames() {
        List<String> result = new ArrayList<>();

        if (this.games != null) {
            Collections.addAll(result, this.games.split(", "));
        }

        return result;
    }

    public void setGames(List<String> games) {
        if (games.size() == 0) {
            this.games = null;
        } else {
            this.games = String.join(", ", games);
        }
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public int getFailedOrders() {
        return failedOrders;
    }

    public void setFailedOrders(int failedOrders) {
        this.failedOrders = failedOrders;
    }

    public boolean getStateSearch() {
        return stateSearch;
    }

    public void setStateSearch(boolean stateSearch) {
        this.stateSearch = stateSearch;
    }
}
