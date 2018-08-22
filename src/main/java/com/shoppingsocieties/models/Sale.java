package com.shoppingsocieties.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Sale extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(optional = false)
    private Product product;
    @ManyToOne
    private Country country;
    private Integer totalItems;
    private Integer itemsLeft;
    private Timestamp startTime;
    private Timestamp endTime;

    public Sale() {
    }

    public Sale(Product product, Country country, int totalItems, Timestamp startTime, Timestamp endTime) {
        this.product = product;
        this.country = country;
        this.totalItems = totalItems;
        this.itemsLeft = totalItems;
        this.startTime = startTime;
        this.endTime = endTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public Sale setId(long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public Sale setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public Sale setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public Integer getItemsLeft() {
        return itemsLeft;
    }

    public Sale setItemsLeft(Integer itemsLeft) {
        this.itemsLeft = itemsLeft;
        return this;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Sale setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Sale setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public Country getCountry() {
        return country;
    }

    public Sale setCountry(Country country) {
        this.country = country;
        return this;
    }
}
