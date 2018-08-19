package com.shoppingsocieties.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Currency extends Model {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String code;
    // Exchange rate to USD.
    private Float exchangeRate;

    public Currency() {
    }

    public Currency(String name, String code, float exchangeRate) {
        this.name = name;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

    public String getName() {
        return name;
    }

    public Currency setName(String name) {
        this.name = name;
        return this;
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
