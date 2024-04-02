package com.alexfehd.customerservicechat.repository;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository  extends MongoRepository<User, String> {

    User findByCustomerId(String customerId);
}
