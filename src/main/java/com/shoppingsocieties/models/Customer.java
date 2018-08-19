package com.shoppingsocieties.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Customer extends Model {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

    public Long getId() {
        return id;
    }

    public Customer setId(Long id) {
        this.id = id;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Customer setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}
