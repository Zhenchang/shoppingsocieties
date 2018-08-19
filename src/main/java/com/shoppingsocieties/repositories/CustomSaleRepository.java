package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Country;
import com.shoppingsocieties.models.Sale;

import java.util.List;

public interface CustomSaleRepository {

    /**
     * Get current available sales by country code.
     *
     * @param country an instance of country
     * @return a list of {@link Sale} objects
     */
    List<Sale> getCurrentSalesByCountryCode(Country country);
}
