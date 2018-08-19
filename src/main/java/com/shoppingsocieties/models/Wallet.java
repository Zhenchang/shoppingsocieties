package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class Wallet extends Model {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "WALLET_ID_SEQ", initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private long id;
    private Float balance;
    @ManyToOne
    private Currency currency;
    @OneToOne(optional = false)
    private Customer customer;


    public Currency getCurrency() {
        return currency;
    }

    public Wallet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public long getId() {
        return id;
    }

    public Wallet setId(long id) {
        this.id = id;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Wallet setCustomer(Customer customer) {
        this.customer = customer;
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
