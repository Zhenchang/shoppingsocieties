package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long>, CustomCurrencyRepository {
}
