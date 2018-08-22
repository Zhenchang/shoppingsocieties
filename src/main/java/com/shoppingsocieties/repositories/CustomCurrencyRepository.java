package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Currency;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CustomCurrencyRepository {

    /**
     * Get currency by code.
     *
     * @param code currency code
     * @return an instance of {@link Currency}
     */
    @NotNull
    Optional<Currency> findByCode(String code);
}
