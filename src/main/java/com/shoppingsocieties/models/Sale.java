package com.shoppingsocieties.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Sale extends Model {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne(optional = false)
    private Product product;
    @ManyToOne
    private Country country;
    private int totalItems;
    private int itemsLeft;
    private Timestamp startTime;
    private Timestamp endTime;

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

    public int getTotalItems() {
        return totalItems;
    }

    public Sale setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public int getItemsLeft() {
        return itemsLeft;
    }

    public Sale setItemsLeft(int itemsLeft) {
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
