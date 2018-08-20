package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Country;
import com.shoppingsocieties.models.Sale;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public interface CustomSaleRepository {

    /**
     * Get current available sales by country code.
     *
     * @param country an entity of {@link Country} with identifier.
     * @return a list of {@link Sale} objects
     */
    @NotNull
    List<Sale> findByCountryAndStartTimeBeforeAndEndTimeAfter(Country country, Timestamp start, Timestamp end);
}
