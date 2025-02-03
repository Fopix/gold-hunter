package com.gold_hunter.gold_hunter.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "reviews")
public class Review {

    public Review(@NotNull int orderId, @NotNull String game, @NotNull String name, @NotNull int rating, @NotNull String date, @NotNull String message) {
        this.orderId = orderId;
        this.game = game;
        this.name = name;
        this.rating = rating;
        this.date = date;
        this.message = message;
    }

    public Review() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @NotNull
    @Column(name = "game", nullable = false)
    private String game;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "rating", nullable = false)
    private int rating;

    @NotNull
    @Column(name = "date", nullable = false)
    private String date;

    @NotNull
    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
