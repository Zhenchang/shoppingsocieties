package com.shoppingsocieties.test.services;

import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.*;
import com.shoppingsocieties.repositories.*;
import com.shoppingsocieties.services.SaleService;
import com.shoppingsocieties.services.impl.SaleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class SaleServiceTest {

    @MockBean
    private SaleRepository saleRepository;
    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private WalletRepositories walletRepository;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private SaleService saleService;
    private Currency currency;
    private Country country;

    @Before
    public void setUp() {
        currency = new Currency("SGD", 1).setId(0L);
        country = new Country("SG").setId(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveWalletInfoById_InvalidWalletIdTest() {
        try {
            saleService.retrieveWalletInfoById(123);
        } catch (Exception e) {
            Assert.assertTrue("Invalid wallet id [123].".equals(e.getMessage()));
            throw e;
        }
    }

    @Test
    public void retrieveWalletInfoByIdTest() {
        User user = new User().setId(0L);
        Wallet walletExpected = new Wallet(123.11f, currency, user).setId(2L);
        Mockito.when(walletRepository.findById(2L)).thenReturn(Optional.of(walletExpected));
        Wallet wallet = saleService.retrieveWalletInfoById(2L);
        Assert.assertEquals(wallet.getId(), walletExpected.getId());
    }

    @Test
    public void getCurrentSalesByCountry_EmptyListTest() {
        Country country = new Country("CHN");
        Mockito.when(countryRepository.getCountryByCode("CHN")).thenReturn(Optional.of(country));
        Timestamp now = Timestamp.from(Instant.now());
        Mockito.when(saleRepository.findByCountryAndStartTimeBeforeAndEndTimeAfter(country, now, now)).thenReturn(new ArrayList<>());
        List<Sale> result = saleService.getCurrentSalesByCountry("CHN");
        Assert.assertArrayEquals(result.toArray(), new Sale[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCurrentSalesByCountry_InvalidCountryCodeTest() {
        try {
            saleService.getCurrentSalesByCountry("CHN");
        } catch (Exception e) {
            Assert.assertTrue("Invalid country code [CHN].".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void purchaseProduct_InvalidProductIdTest() throws PurchaseException {
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("Invalid product id [0].".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = PurchaseException.class)
    public void purchaseProduct_NoAvailableSalesTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.ofNullable(product));
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("No available sales for the provided product id [0].".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = PurchaseException.class)
    public void purchaseProduct_NoEnoughSockTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Sale sale = new Sale(product, country, 0, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2099-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("No enough stock for the required product.".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void purchaseProduct_InvalidCustomerTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Sale sale = new Sale(product, country, 3, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2099-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.empty());
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("Invalid customer id [0].".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = PurchaseException.class)
    public void purchaseProduct_NoWalletTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Sale sale = new Sale(product, country, 3, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2099-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        User user = new User().setId(0L);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.ofNullable(user));
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("No wallet available. Please bind a wallet first.".equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = PurchaseException.class)
    public void purchaseProduct_NoEnoughBalanceTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Sale sale = new Sale(product, country, 4, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2099-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        User user = new User().setId(0L);
        Wallet wallet = new Wallet(10f, currency, user);
        user.setWallet(wallet);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("No enough balance in wallet."));
            throw e;
        }
    }

    @Test(expected = PurchaseException.class)
    public void purchaseProduct_NoMerchantWalletTest() throws PurchaseException {
        Product product = new Product(12.2f, currency).setId(0L);
        Sale sale = new Sale(product, country, 5, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2099-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        User user = new User().setId(0L);
        Wallet wallet = new Wallet(100f, currency, user);
        user.setWallet(wallet);
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        try {
            saleService.purchaseProduct(0L, 0L);
        } catch (Exception e) {
            Assert.assertTrue("Failed to transfer to merchant.".equals(e.getMessage()));
            throw e;
        }
    }

    @Test
    public void purchaseProductTest() throws PurchaseException {
        Product product = new Product(50.5f, currency).setId(0L);
        Sale sale = new Sale(product, country, 1, Timestamp.valueOf("2018-01-01 00:00:00"),
                Timestamp.valueOf("2089-01-01 00:00:00")).setId(0L);
        product.setSale(sale);
        User user = new User().setId(0L);
        Wallet wallet = new Wallet(100f, currency, user).setId(2L);
        user.setWallet(wallet);
        Wallet merchantWallet = new Wallet(100f, currency, new User().setId(1L));
        Mockito.when(productRepository.findById(0L)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        Mockito.when(walletRepository.findById(1L)).thenReturn(Optional.of(merchantWallet));
        saleService.purchaseProduct(0L, 0L);
        Assert.assertTrue(sale.getItemsLeft().equals(0));
        Assert.assertTrue(wallet.getBalance().equals(49.5f));
        Assert.assertTrue(merchantWallet.getBalance().equals(150.5f));
    }

    @TestConfiguration
    static class SaleServiceImplTestContextConfiguration {
        @Bean
        public SaleService saleService() {
            return new SaleServiceImpl();
        }
    }
}
