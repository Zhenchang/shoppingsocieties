package com.shoppingsocieties.listeners;

import com.shoppingsocieties.models.*;
import com.shoppingsocieties.repositories.*;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DatabaseSeeder {

    private CountryRepository countryRepository;
    private CurrencyRepository currencyRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private SaleRepository saleRepository;
    private WalletRepositories walletRepositories;

    public DatabaseSeeder(CountryRepository countryRepository, CurrencyRepository currencyRepository,
                          ProductRepository productRepository, UserRepository userRepository,
                          SaleRepository saleRepository, WalletRepositories walletRepositories) {
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
        this.walletRepositories = walletRepositories;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void seedDatabase() {
        System.out.println("Start seeding database...");
        // Seed currency table.
        Currency currency = new Currency("SGD", 1f);
        currencyRepository.save(currency);
        System.out.println(String.format("Currency {%d, %s, %f} added.", currency.getId(), currency.getCode(),
                currency.getExchangeRate()));
        // Seed product table.
        Product product = new Product(8.8f, currency);
        Product product2 = new Product(0f, currency);
        Product product3 = new Product(28.8f, currency);
        Product product4 = new Product(38.8f, currency);
        Product product5 = new Product(48.8f, currency);
        productRepository.save(product);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        System.out.println(String.format("Products added. Ids: [%d, %d, %d, %d, %d].", product.getId(),
                product2.getId(), product3.getId(), product4.getId(), product5.getId()));
        // Seed country table.
        Country country = new Country("SG");
        countryRepository.save(country);
        System.out.println(String.format("Country {%d, %s} added.", country.getId(), country.getCode()));
        // Seed sale table.
        Timestamp t2000 = Timestamp.valueOf("2000-01-01 00:00:00");
        Timestamp t2001 = Timestamp.valueOf("2001-01-01 00:00:00");
        Timestamp t2059 = Timestamp.valueOf("2059-01-01 00:00:00");
        Timestamp t2099 = Timestamp.valueOf("2099-01-01 00:00:00");
        Sale sale = new Sale(product, country, 15, t2001, t2059);
        Sale sale2 = new Sale(product2, country, 999999999, t2001, t2059);
        Sale sale3 = new Sale(product3, country, 35, t2000, t2001);
        Sale sale4 = new Sale(product4, country, 45, t2059, t2099);
        Sale sale5 = new Sale(product5, country, 55, t2001, t2059);
        saleRepository.save(sale);
        saleRepository.save(sale2);
        saleRepository.save(sale3);
        saleRepository.save(sale4);
        saleRepository.save(sale5);
        System.out.println("5 sales under 'SG' added. 3 of them are currently available.");
        // Seed user table.
        User user = new User();
        User user2 = new User();
        User user3 = new User();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        System.out.println(String.format("Users added. Ids: [%d, %d, %d].", user.getId(), user2.getId(), user3.getId()));
        // Seed wallet table
        // Seed merchant wallet.
        Wallet wallet = new Wallet(0, currency, user);
        walletRepositories.save(wallet);
        System.out.println(String.format("Merchant wallet added, id: %s, balance: %f", wallet.getId(), wallet.getBalance()));
        Wallet wallet2 = new Wallet(9999, currency, user2);
        Wallet wallet3 = new Wallet(1, currency, user3);
        walletRepositories.save(wallet2);
        walletRepositories.save(wallet3);
        System.out.println(String.format("Customer wallets added. Ids: [%d, %d].", wallet2.getId(), wallet3.getId()));
        System.out.println("Database initialized.");
    }
}
