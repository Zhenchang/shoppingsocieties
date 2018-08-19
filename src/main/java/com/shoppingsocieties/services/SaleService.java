package com.shoppingsocieties.services;

import com.shoppingsocieties.exceptions.PaymentException;
import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.*;
import com.shoppingsocieties.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WalletRepositories walletRepositories;

    /**
     * Get current available sales by country code.
     *
     * @param countryCode country code
     * @return a list of {@link Sale} objects or null if no sales are available.
     */
    public List<Sale> getCurrentSalesByCountry(String countryCode) {
        Optional<Country> optionalCountry = countryRepository.getCountryByCode(countryCode);
        if (!optionalCountry.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid country code [%s].", countryCode));
        }
        return saleRepository.getCurrentSalesByCountryCode(optionalCountry.get());
    }

    public void purchase(long productId, long customerId) throws PurchaseException {
        // Check if the provided product id is valid.
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid product id [%d].", productId));
        }
        Product product = productOptional.get();
        // Check if flash sale for the provided product exists.
        Sale sale = product.getSale();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (sale == null || (sale.getStartTime().compareTo(now) <= 0 && sale.getEndTime().compareTo(now) >= 0)) {
            throw new IllegalArgumentException(String.format("No available sales for the provided product id [%d].", productId));
        }
        // Check if the product has enough stock.
        if (sale.getItemsLeft() < 1)
            throw new PurchaseException("No enough stock for the required product.");
        // Check if the provided customer id is valid.
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid customer id [%d].", customerId));
        }
        // Check if the customer has enough balance.
        Wallet customerWallet = optionalCustomer.get().getWallet();
        if (customerWallet == null) {
            throw new PaymentException("No wallet available. Please bind a wallet first.");
        }
        BigDecimal balanceInSGD = BigDecimal.valueOf(customerWallet.getBalance()).multiply(BigDecimal.valueOf(customerWallet.getCurrency().getExchangeRate()));
        BigDecimal priceInSGD = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(product.getCurrency().getExchangeRate()));
        if (balanceInSGD.compareTo(priceInSGD) < 0) {
            throw new PaymentException(String.format("No enough balance in wallet. Need [%f%s], [%f%s] is available.",
                    product.getPrice(), product.getCurrency().getCode(),
                    customerWallet.getBalance(), customerWallet.getCurrency().getCode()));
        }
        // Do purchase, update stock, transfer from customer's wallet to merchant's wallet.
        sale.setItemsLeft(sale.getItemsLeft() - 1);
        BigDecimal transferAmountInCustomerCurrency = BigDecimal.valueOf(product.getPrice())
                .multiply(BigDecimal.valueOf(product.getCurrency().getExchangeRate()))
                .divide(BigDecimal.valueOf(customerWallet.getCurrency().getExchangeRate()), RoundingMode.UP);
        customerWallet.setBalance(BigDecimal.valueOf(customerWallet.getBalance()).subtract(transferAmountInCustomerCurrency).floatValue());
        Optional<Wallet> walletOptional = walletRepositories.findById(1L);
        if (!walletOptional.isPresent())
            throw new PaymentException("Failed to transfer to merchant. Transaction failed.");
        Wallet merchantWallet = walletOptional.get();
        merchantWallet.setBalance(BigDecimal.valueOf(merchantWallet.getBalance()).add(
                transferAmountInCustomerCurrency.multiply(BigDecimal.valueOf(customerWallet.getCurrency().getExchangeRate()))
                        .divide(BigDecimal.valueOf(merchantWallet.getCurrency().getExchangeRate()), RoundingMode.UP)
        ).floatValue());
    }

    /**
     * Get wallet information by the given id.
     *
     * @param id wallet id
     * @return an {@link Wallet} object with the given id or null if the wallet is not found.
     */
    public Wallet query(long id) {
        Optional<Wallet> optional = walletRepositories.findById(id);
        return optional.orElse(null);
    }
}
