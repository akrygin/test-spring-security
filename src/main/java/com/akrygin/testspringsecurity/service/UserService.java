package com.akrygin.testspringsecurity.service;

import com.akrygin.testspringsecurity.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
