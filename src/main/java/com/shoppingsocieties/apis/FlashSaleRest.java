package com.shoppingsocieties.apis;

import com.shoppingsocieties.exceptions.PurchaseException;
import com.shoppingsocieties.models.Sale;
import com.shoppingsocieties.models.Wallet;
import com.shoppingsocieties.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FlashSaleRest {

    @Autowired
    private SaleService saleService;

    @RequestMapping(value = "/wallet/{walletId}", method = RequestMethod.GET)
    public Map<String, Object> getWalletInfoById(@PathVariable("walletId") long walletId) {
        Wallet wallet = saleService.retrieveWalletInfoById(walletId);
        Map<String, Object> result = new HashMap<>();
        result.put("id", wallet.getId());
        result.put("balance", wallet.getBalance());
        result.put("currency", wallet.getCurrency().getCode());
        return result;
    }

    @RequestMapping(value = "/products/{productId}/purchase", method = RequestMethod.POST)
    public void purchaseProduct(@PathVariable("productId") long productId, @RequestBody Map<String, Long> body)
            throws PurchaseException {
        long userId = body.get("user_id");
        saleService.purchaseProduct(productId, userId);
    }

    @RequestMapping(value = "/sales/current", method = RequestMethod.GET)
    public Map<String, Object> getCurrentSalesByCountryCode(@RequestParam("country") String countryCode) {
        List<Sale> sales = saleService.getCurrentSalesByCountry(countryCode);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> salesCpy = new ArrayList<>();
        sales.forEach(sale -> {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("product_id", sale.getProduct().getId());
            tmp.put("price", sale.getProduct().getPrice());
            tmp.put("currency", sale.getProduct().getCurrency().getCode());
            tmp.put("total_items", sale.getTotalItems());
            tmp.put("items_left", sale.getItemsLeft());
            tmp.put("time_left", sale.getEndTime().toInstant().getEpochSecond() - Instant.now().getEpochSecond());
            salesCpy.add(tmp);
        });
        result.put("sales", salesCpy);
        return result;
    }
}
