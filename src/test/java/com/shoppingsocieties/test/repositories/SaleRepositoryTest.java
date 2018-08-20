package com.shoppingsocieties.test.repositories;

import com.shoppingsocieties.models.Country;
import com.shoppingsocieties.models.Currency;
import com.shoppingsocieties.models.Product;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.repositories.SaleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaleRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private SaleRepository saleRepository;
    private Product product;
    private Country country;
    private Currency currency;


    @Before
    public void init() {
        currency = new Currency("SGD", 1);
        em.persist(currency);
        product = new Product(13.3f, currency);
        product = em.persist(product);
        country = new Country("SG");
        country = em.persist(country);
    }

    @Test
    public void saveTest() {
        Sale sale = new Sale(product, country, 10, Timestamp.valueOf("2017-07-07 00:00:00"), Timestamp.valueOf("2017-07-08 00:00:00"));
        sale = saleRepository.save(sale);
        Sale expected = em.find(Sale.class, sale.getId());
        Assert.assertEquals(expected.getProduct().getId(), sale.getProduct().getId());
        Assert.assertEquals(expected.getCountry().getId(), sale.getCountry().getId());
        Assert.assertEquals(expected.getTotalItems().compareTo(sale.getTotalItems()), 0);
        Assert.assertEquals(expected.getItemsLeft().compareTo(sale.getItemsLeft()), 0);
        Assert.assertTrue(expected.getEndTime().equals(sale.getEndTime()));
    }

    @Test
    public void deleteTest() {
        Sale sale = new Sale(product, country, 10, Timestamp.valueOf("2017-07-07 00:00:00"), Timestamp.valueOf("2017-07-08 00:00:00"));
        sale = em.persist(sale);
        saleRepository.delete(sale);
        Sale expected = em.find(Sale.class, sale.getId());
        Assert.assertNull(expected);
    }

    @Test
    public void updateTest() {
        Sale sale = new Sale(product, country, 10, Timestamp.valueOf("2017-07-07 00:00:00"), Timestamp.valueOf("2017-07-08 00:00:00"));
        sale = em.persist(sale);
        sale.setTotalItems(20);
        saleRepository.save(sale);
        Sale expected = em.find(Sale.class, sale.getId());
        Assert.assertTrue(expected.getTotalItems().equals(sale.getTotalItems()));
    }

    @Test
    public void findByIdTest() {
        Sale sale = new Sale(product, country, 10, Timestamp.valueOf("2017-07-07 00:00:00"), Timestamp.valueOf("2017-07-08 00:00:00"));
        sale = em.persist(sale);
        Optional<Sale> expected = saleRepository.findById(sale.getId());
        Assert.assertTrue(expected.isPresent());
        Assert.assertEquals(expected.get().getProduct().getId(), sale.getProduct().getId());
    }

    @Test
    public void getCurrentSalesByCountryTest() {
        List<Long> expectedIds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        Sale sale = new Sale(product, country, 10, Timestamp.valueOf("2018-07-07 00:00:00"), Timestamp.valueOf("2018-10-08 00:00:00"));
        expectedIds.add((Long) em.persistAndGetId(sale));
        product = new Product(13.3f, currency);
        product = em.persist(product);
        sale = new Sale(product, country, 10, Timestamp.valueOf("2018-08-07 00:00:00"), Timestamp.valueOf("2018-08-31 00:00:00"));
        expectedIds.add((Long) em.persistAndGetId(sale));
        product = new Product( 13.3f, currency);
        product = em.persist(product);
        em.persist(new Sale(product, country, 10, Timestamp.valueOf("2018-08-07 00:00:00"), Timestamp.valueOf("2018-08-10 00:00:00")));
        product = new Product(13.3f, currency);
        product = em.persist(product);
        em.persist(new Sale(product, country, 10, Timestamp.valueOf("2018-10-30 00:00:00"), Timestamp.valueOf("2018-10-31 00:00:00")));
        Timestamp now = Timestamp.from(Instant.now());
        saleRepository.findByCountryAndStartTimeBeforeAndEndTimeAfter(country, now, now).forEach(s -> ids.add(s.getId()));
        Assert.assertArrayEquals(expectedIds.toArray(), ids.toArray());
    }
}
