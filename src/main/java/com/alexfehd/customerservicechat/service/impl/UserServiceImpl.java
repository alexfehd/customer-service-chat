package com.alexfehd.customerservicechat.service.impl;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.repository.UserRepository;
import com.alexfehd.customerservicechat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    @Override
    public void disconnect(User user) {
        var storedUser = repository.findById(user.getCustomerId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    @Override
    public User findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }
}
