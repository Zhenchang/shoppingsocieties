package com.shoppingsocieties.services;

import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.models.Wallet;

import java.util.List;

public interface SaleService {

    /**
     * Get current available sales by country code.
     *
     * @param countryCode country code
     * @return a list of {@link Sale} objects or null if no sales are available.
     */
    List<Sale> getCurrentSalesByCountry(String countryCode);

    /**
     * Customer purchases the selected product. Update customer wallet and flash sale information.
     *
     * @param productId  id of the selected product
     * @param customerId id of the customer
     * @throws PurchaseException Exception happened during the purchase process.
     */
    void purchase(long productId, long customerId) throws PurchaseException;

    /**
     * Get wallet information by the given id.
     *
     * @param id wallet id
     * @return an {@link Wallet} object with the given id or null if the wallet is not found.
     */
    Wallet query(long id);
}
