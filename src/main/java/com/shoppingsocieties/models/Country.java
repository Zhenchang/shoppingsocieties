package com.shoppingsocieties.models;

import javax.persistence.*;

@Entity
public class Country extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
