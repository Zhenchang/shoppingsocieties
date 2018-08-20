package com.shoppingsocieties.repositories;

import com.shoppingsocieties.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
