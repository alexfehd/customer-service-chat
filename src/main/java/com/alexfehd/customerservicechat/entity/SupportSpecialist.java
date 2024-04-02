package com.alexfehd.customerservicechat.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class SupportSpecialist {
    @Id
    private String supportSpecialistId;
    private String fullName;
    private Status status;
}
