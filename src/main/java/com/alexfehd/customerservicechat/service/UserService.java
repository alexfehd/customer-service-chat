package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.User;

public interface UserService {

    void saveUser(User user);

    void disconnect(User user);

    User findByCustomerId(String customerId);
}
