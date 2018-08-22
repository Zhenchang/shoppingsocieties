package com.shoppingsocieties.unittest.repositories;

import com.shoppingsocieties.models.Country;
import com.shoppingsocieties.repositories.CountryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void Test() {
        Country country = new Country("SG");
        em.persist(country);
        Optional<Country> optional = countryRepository.getCountryByCode("SG");
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(optional.get().getCode(), "SG");
    }
}
