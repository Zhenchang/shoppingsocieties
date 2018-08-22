package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "user")
    private Wallet wallet;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public User setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}
