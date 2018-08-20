package com.shoppingsocieties.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Country extends Model {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String code;

    public Country() {
    }

    public Country(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Country setCode(String code) {
        this.code = code;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Country setId(Long id) {
        this.id = id;
        return this;
    }
}
