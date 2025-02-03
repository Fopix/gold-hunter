package com.gold_hunter.gold_hunter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "payment")
public class PaymentMethod {

    public PaymentMethod(String paymentSystem, float commission) {
        this.paymentSystem = paymentSystem;
        this.commission = commission;
    }

    public PaymentMethod() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String paymentSystem;

    private float commission;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }
}
