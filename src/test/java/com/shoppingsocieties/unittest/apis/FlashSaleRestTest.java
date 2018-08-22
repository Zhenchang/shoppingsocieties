package com.shoppingsocieties.unittest.apis;

import com.shoppingsocieties.apis.FlashSaleRest;
import com.shoppingsocieties.models.*;
import com.shoppingsocieties.services.SaleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FlashSaleRest.class)
public class FlashSaleRestTest {

    @MockBean
    private SaleService saleService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void retrieveWalletInfoById_InvalidIdTest() throws Exception {
        Mockito.when(saleService.retrieveWalletInfoById(123L)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/wallet/123")).andExpect(status().isBadRequest());
    }

    @Test
    public void retrieveWalletInfoByIdTest() throws Exception {
        User user = new User().setId(0L);
        Currency currency = new Currency("SGD", 1.0f);
        Wallet wallet = new Wallet(123.0f, currency, user).setId(123L);
        Mockito.when(saleService.retrieveWalletInfoById(123L)).thenReturn(wallet);
        mockMvc.perform(get("/wallet/123")).andExpect(status().isOk()).andExpect(content().json("{id:123, balance:123, currency: 'SGD'}"));
    }

    @Test
    public void getCurrentSalesByCountry_InvalidCountryCodeTest() throws Exception {
        Mockito.when(saleService.getCurrentSalesByCountry("SG")).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/sales/current?country=SG")).andExpect(status().isBadRequest());
    }

    @Test
    public void getCurrentSalesByCountry_EmptyListTest() throws Exception {
        Mockito.when(saleService.getCurrentSalesByCountry("SG")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/sales/current?country=SG")).andExpect(status().isOk()).andExpect(content().json("{sales: []}"));
    }

    @Test
    public void getCurrentSalesByCountryTest() throws Exception {
        Currency currency = new Currency("SGD", 1.0f);
        Country country = new Country("SG");
        Timestamp start = Timestamp.valueOf("2000-01-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2099-01-01 00:00:00");
        Product product = new Product(12.2f, currency).setId(1L);
        Sale sale = new Sale(product, country, 10, start, end);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        Mockito.when(saleService.getCurrentSalesByCountry("SG")).thenReturn(sales);
        mockMvc.perform(get("/sales/current?country=SG")).andExpect(status().isOk())
                .andExpect(content().json("{sales: [{product_id: 1, price: 12.2, currency: 'SGD', total_items: 10, items_left: 10}]}"));
    }

    @Test
    public void getCurrentSalesByCountry2Test() throws Exception {
        Currency currency = new Currency("SGD", 1.0f);
        Country country = new Country("SG");
        Timestamp start = Timestamp.valueOf("2000-01-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2099-01-01 00:00:00");
        Product product = new Product(12.2f, currency).setId(1L);
        Sale sale = new Sale(product, country, 10, start, end);
        Product product2 = new Product(12.2f, currency).setId(1L);
        Sale sale2 = new Sale(product2, country, 10, start, end);
        Product product3 = new Product(12.2f, currency).setId(1L);
        Sale sale3 = new Sale(product3, country, 10, start, end);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale2);
        sales.add(sale3);
        Mockito.when(saleService.getCurrentSalesByCountry("SG")).thenReturn(sales);
        mockMvc.perform(get("/sales/current?country=SG")).andExpect(status().isOk()).andExpect(jsonPath("$.sales", hasSize(3)));
    }

    @Test
    public void purchaseProduct_InvalidArgumentTest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException()).when(saleService).purchaseProduct(321L, 123L);
        mockMvc.perform(post("/products/321/purchase").contentType(MediaType.APPLICATION_JSON)
                .content("{'user_id': 123}")).andExpect(status().isBadRequest());
    }

    @Test
    public void purchaseProductTest() throws Exception {
        Mockito.doNothing().when(saleService).purchaseProduct(321L, 123L);
        mockMvc.perform(post("/products/321/purchase").contentType(MediaType.APPLICATION_JSON)
                .content("{'user_id': 123}")).andExpect(status().isBadRequest());
    }

}
