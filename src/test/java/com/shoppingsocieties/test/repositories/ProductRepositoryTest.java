package com.shoppingsocieties.test.repositories;

import com.shoppingsocieties.models.Currency;
import com.shoppingsocieties.models.Product;
import com.shoppingsocieties.repositories.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ProductRepository productRepository;
    private long testProductId;
    private Currency currency;

    @Before
    public void init() {
        currency = em.find(Currency.class, 0L);
        if (currency != null && "SGD".equals(currency.getCode()))
            return;
        else {
            currency = new Currency("SGD", 1);
            em.persist(currency);
        }

        Product product = new Product( 1.5f, currency);
        testProductId = (long) em.persistAndGetId(product);
    }

    @Test
    public void findByIdTest() {
        Optional<Product> product = productRepository.findById(testProductId);
        Assert.assertTrue(product.isPresent());
        Assert.assertEquals(product.get().getPrice().compareTo(1.5f), 0);
        Assert.assertEquals(product.get().getCurrency().getId(), currency.getId());
    }

    @Test
    public void saveTest() {
        Product product = new Product( 999f, currency);
        long id = productRepository.save(product).getId();
        Product expected = em.find(Product.class, id);
        Assert.assertEquals(expected.getPrice().compareTo(999f), 0);
        Assert.assertEquals(expected.getCurrency().getId(), product.getCurrency().getId());
    }

    @Test
    public void updateTest() {
        Product product = em.find(Product.class, testProductId);
        product.setPrice(1.99f);
        productRepository.save(product);
        Product expected = em.find(Product.class, testProductId);
        Assert.assertEquals(expected.getPrice().compareTo(1.99f), 0);
    }

    @Test
    public void deleteTest() {
        long idToBeDeleted = (long) em.persistAndGetId(new Product(23.33f, currency));
        Assert.assertNotNull(em.find(Product.class, idToBeDeleted));
        productRepository.delete(new Product(idToBeDeleted));
        Assert.assertNull(em.find(Product.class, idToBeDeleted));
    }
}
