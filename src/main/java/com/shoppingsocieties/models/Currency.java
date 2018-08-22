package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class Currency extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    // Exchange rate to USD.
    @Column(scale = 2)
    private Float exchangeRate;

    public Currency() {
    }

    public Currency(String code, float exchangeRate) {
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

    public String getCode() {
        return code;
    }

    public Currency setCode(String code) {
        this.code = code;
        return this;
    }

    public Float getExchangeRate() {
        return exchangeRate;
    }

    public Currency setExchangeRate(Float exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Currency setId(Long id) {
        this.id = id;
        return this;
    }
}
