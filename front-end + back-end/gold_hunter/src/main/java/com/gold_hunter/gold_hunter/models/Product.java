package com.gold_hunter.gold_hunter.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@Entity(name = "products")
public class Product {

    public Product(String game, String currency, String unit, String prices, String servers, String delivery, String keywords) {
        this.game = game;
        this.currency = currency;
        this.unit = unit;
        this.prices = prices;
        this.servers = servers;
        this.delivery = delivery;
        this.keywords = keywords;
    }

    public Product() { }

    @Id
    private String id;

    @Column(name = "game", nullable = false)
    private String game;

    @Column(name = "currency", nullable = false)
    private String currency;

    private String unit;

    @Column(name = "prices", columnDefinition = "TEXT", nullable = false)
    private String prices;

    @Column(name = "servers", columnDefinition = "TEXT", nullable = false)
    private String servers;

    @Column(name = "delivery", columnDefinition = "TEXT", nullable = false)
    private String delivery;

    @Column(name = "keywords", nullable = false)
    private String keywords;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(List<Float> prices) {
        StringJoiner stringJoiner = new StringJoiner(", ");

        for (float num : prices) {
            stringJoiner.add("" + num);
        }

        this.prices = stringJoiner.toString();
    }

    public List<Float> getPrice() {
        List<Float> result = new ArrayList<>();

        for (String s : this.prices.split(", ")) {
            result.add(Float.valueOf(s));
        }

        return result;
    }

    public void setServers(List<String> servers) {
        this.servers = String.join(", ", servers);
    }

    public List<String> getServers() {
        List<String> result = new ArrayList<>();

        Collections.addAll(result, this.servers.split(", "));

        return result;
    }

    public void setDelivery(List<String> delivery) {
        this.delivery = String.join(", ", delivery);
    }

    public List<String> getDelivery() {
        List<String> result = new ArrayList<>();

        Collections.addAll(result, this.delivery.split(", "));

        return result;
    }

    public String getKeywords() {
        return keywords;
    }
}
