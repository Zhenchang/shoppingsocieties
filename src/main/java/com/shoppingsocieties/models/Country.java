package com.shoppingsocieties.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Country extends Model {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String code;

    public String getName() {
        return name;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Country setCode(String code) {
        this.code = code;
        return this;
    }
}
