package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class Product extends Model {

    @Id
    @GeneratedValue
    private Long id;
    private Float price;
    @ManyToOne(optional = false)
    private Currency currency;
    @OneToOne(mappedBy = "product")
    private Sale sale;

    public Product() {
    }

    public Product(long id) {
        this.id = id;
    }

    public Product(float price, Currency currency) {
        this.id = id;
        this.price = price;
        this.currency = currency;
    }

    public Sale getSale() {
        return sale;
    }

    public Product setSale(Sale sale) {
        this.sale = sale;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Product setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public Product setPrice(Float price) {
        this.price = price;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }
}
