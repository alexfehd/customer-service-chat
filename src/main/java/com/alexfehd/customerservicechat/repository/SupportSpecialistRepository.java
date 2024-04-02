package com.alexfehd.customerservicechat.repository;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.SupportSpecialist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupportSpecialistRepository extends MongoRepository<SupportSpecialist, String> {

    List<SupportSpecialist> findAllByStatus(Status status);

}
