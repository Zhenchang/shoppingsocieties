package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class Wallet extends Model {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "WALLET_ID_SEQ", initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    private Float balance;
    @ManyToOne
    private Currency currency;
    @OneToOne(optional = false)
    private User user;

    public Wallet() {
    }

    public Wallet(float balance, Currency currency, User user) {
        this.balance = balance;
        this.currency = currency;
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Wallet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Wallet setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Wallet setUser(User customer) {
        this.user = customer;
        return this;
    }

    public Float getBalance() {
        return balance;
    }

    public Wallet setBalance(Float balance) {
        this.balance = balance;
        return this;
    }
}
