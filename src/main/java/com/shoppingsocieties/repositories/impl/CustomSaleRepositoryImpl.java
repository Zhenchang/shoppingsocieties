package com.shoppingsocieties.repositories.impl;

import com.shoppingsocieties.models.Country;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.repositories.CustomSaleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CustomSaleRepositoryImpl implements CustomSaleRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Sale> getCurrentSalesByCountryCode(Country country) {
        TypedQuery<Sale> query = em.createQuery("select s from Sale s where s.country = :country " +
                "and s.startTime <= :currentTime and s.endTime > :currentTime", Sale.class);
        query.setParameter("country", country);
        query.setParameter("currentTime", Timestamp.valueOf(LocalDateTime.now()));
        return query.getResultList();
    }
}
