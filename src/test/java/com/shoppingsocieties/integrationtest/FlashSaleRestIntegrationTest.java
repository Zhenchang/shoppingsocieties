package com.shoppingsocieties.integrationtest;

import com.shoppingsocieties.Application;
import com.shoppingsocieties.listeners.DatabaseSeeder;
import com.shoppingsocieties.models.Product;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.models.User;
import com.shoppingsocieties.models.Wallet;
import com.shoppingsocieties.repositories.ProductRepository;
import com.shoppingsocieties.repositories.UserRepository;
import com.shoppingsocieties.repositories.WalletRepositories;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class FlashSaleRestIntegrationTest {

    private static boolean oneTimeSetupDone = false;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DatabaseSeeder databaseSeeder;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepositories walletRepositories;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if (oneTimeSetupDone)
            return;
        databaseSeeder.seedDatabase();
        oneTimeSetupDone = true;
    }

    @Test
    public void retrieveWalletInfoById_InvalidIdTest() throws Exception {
        mockMvc.perform(get("/wallet/999")).andExpect(status().isBadRequest());
    }

    @Test
    public void retrieveWalletInfoByIdTest() throws Exception {
        mockMvc.perform(get("/wallet/1")).andExpect(status().isOk()).andExpect(content().json("{id:1, balance:0, currency: 'SGD'}"));
    }

    @Test
    public void getCurrentSalesByCountry_InvalidCountryCodeTest() throws Exception {
        mockMvc.perform(get("/sales/current?country=CHN")).andExpect(status().isBadRequest());
    }

    @Test
    public void getCurrentSalesByCountryTest() throws Exception {
        mockMvc.perform(get("/sales/current?country=SG")).andExpect(status().isOk())
                .andExpect(content().json("{sales: [{product_id: 1, price: 8.8, currency: 'SGD', " +
                        "total_items: 15},{product_id: 2, price: 0.0, currency: 'SGD', " +
                        "total_items: 999999999},{product_id: 5, price: 48.8, currency: 'SGD', " +
                        "total_items: 55}]}"));
    }

    @Test
    public void purchaseProduct_InvalidArgumentTest() throws Exception {
        mockMvc.perform(post("/products/999/purchase").contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_id\": 1}")).andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":'Invalid product id [999].'}"));
    }

    @Test
    public void purchaseProduct_NoEnoughBalanceArgumentTest() throws Exception {
        mockMvc.perform(post("/products/1/purchase").contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_id\": 3}")).andExpect(status().isBadRequest())
                .andExpect(content().string(StringContains.containsString("No enough balance in wallet.")));
    }

    @Test
    public void purchaseProductTest() throws Exception {
        mockMvc.perform(post("/products/1/purchase").contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_id\": 2}")).andExpect(status().isOk());
        Product product = productRepository.findById(1L).orElse(null);
        assert product != null;
        Sale sale = product.getSale();
        Assert.assertTrue(sale.getItemsLeft().equals(14));
        User customer = userRepository.findById(2L).orElse(null);
        Wallet merchantWallet = walletRepositories.findById(1L).orElse(null);
        assert customer != null;
        System.out.println(customer.getWallet().getBalance());
        Assert.assertTrue(customer.getWallet().getBalance().equals(9990.2F));
        assert merchantWallet != null;
        Assert.assertTrue(merchantWallet.getBalance().equals(8.8F));
    }
}
