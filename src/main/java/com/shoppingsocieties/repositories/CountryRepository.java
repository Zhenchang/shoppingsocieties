package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

    Optional<Country> getCountryByCode(String countryCode);
}
