package com.shoppingsocieties.services.impl;

import com.shoppingsocieties.exceptions.PaymentException;
import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.*;
import com.shoppingsocieties.repositories.*;
import com.shoppingsocieties.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepositories walletRepositories;

    @Override
    public List<Sale> getCurrentSalesByCountry(String countryCode) {
        Optional<Country> optionalCountry = countryRepository.getCountryByCode(countryCode);
        if (!optionalCountry.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid country code [%s].", countryCode));
        }
        Timestamp now = Timestamp.from(Instant.now());
        return saleRepository.findByCountryAndStartTimeBeforeAndEndTimeAfter(optionalCountry.get(), now, now);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void purchaseProduct(long productId, long userId) throws PurchaseException {
        // Check if the provided product id is valid.
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid product id [%d].", productId));
        }
        Product product = productOptional.get();
        // Check if flash sale for the provided product exists.
        Sale sale = product.getSale();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (sale == null || !(sale.getStartTime().compareTo(now) < 0 && sale.getEndTime().compareTo(now) > 0)) {
            throw new PurchaseException(String.format("No available sales for the provided product id [%d].", productId));
        }
        // Check if the product has enough stock.
        if (sale.getItemsLeft() < 1)
            throw new PurchaseException("No enough stock for the required product.");
        // Check if the provided customer id is valid.
        Optional<User> optionalCustomer = userRepository.findById(userId);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException(String.format("Invalid customer id [%d].", userId));
        }
        // Check if the customer has enough balance.
        Wallet customerWallet = optionalCustomer.get().getWallet();
        if (customerWallet == null) {
            throw new PaymentException("No wallet available. Please bind a wallet first.");
        }
        if (customerWallet.getId().equals(1L)) {
            throw new PaymentException("Can not make payment by merchant account.");
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
            throw new PaymentException("Failed to transfer to merchant.");
        Wallet merchantWallet = walletOptional.get();
        merchantWallet.setBalance(BigDecimal.valueOf(merchantWallet.getBalance()).add(
                transferAmountInCustomerCurrency.multiply(BigDecimal.valueOf(customerWallet.getCurrency().getExchangeRate()))
                        .divide(BigDecimal.valueOf(merchantWallet.getCurrency().getExchangeRate()), RoundingMode.UP)
        ).floatValue());
    }

    @Override
    public Wallet retrieveWalletInfoById(long walletId) {
        Optional<Wallet> walletOptional = walletRepositories.findById(walletId);
        if (!walletOptional.isPresent())
            throw new IllegalArgumentException(String.format("Invalid wallet id [%s].", walletId));
        return walletOptional.get();
    }
}
