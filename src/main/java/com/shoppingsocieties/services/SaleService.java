package com.shoppingsocieties.services;

import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.models.Wallet;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SaleService {

    /**
     * Get current available sales by country code.
     *
     * @param countryCode country code
     * @return a list of {@link Sale} objects.
     * @throws IllegalArgumentException when the provided country code doesn't exist.
     */
    @NotNull
    List<Sale> getCurrentSalesByCountry(String countryCode);

    /**
     * Customer purchases the selected product. Update customer wallet and flash sale information.
     *
     * @param productId id of the selected product
     * @param userId    id of the customer
     * @throws IllegalArgumentException when product id or user id is invalid.
     * @throws PurchaseException        other exceptions during the purchaseProduct process.
     */
    void purchaseProduct(long productId, long userId) throws PurchaseException;

    /**
     * Get wallet information by user id.
     *
     * @param userId user id.
     * @return an {@link Wallet} instance or {@link IllegalArgumentException} if no wallet found.
     * @throws java.lang.IllegalArgumentException when the provided used id doesn't exist or the wallet is not found for the given user.
     */
    @NotNull
    Wallet retrieveWalletInfoByUserId(long userId);
}
