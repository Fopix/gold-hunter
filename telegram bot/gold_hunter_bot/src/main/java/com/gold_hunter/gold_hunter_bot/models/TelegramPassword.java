package com.gold_hunter.gold_hunter_bot.models;

import javax.persistence.*;

@Entity(name = "telegram_passwords")
public class TelegramPassword {

    public TelegramPassword(String password, String userId) {
        this.password = password;
        this.userId = userId;
    }

    public TelegramPassword() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_id")
    private String userId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
