package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
