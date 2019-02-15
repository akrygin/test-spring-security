package com.akrygin.testspringsecurity.repository;

import com.akrygin.testspringsecurity.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
