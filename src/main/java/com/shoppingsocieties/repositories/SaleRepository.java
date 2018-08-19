package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Sale;
import org.springframework.data.repository.CrudRepository;

public interface SaleRepository extends CrudRepository<Sale, Long>, CustomSaleRepository {
}