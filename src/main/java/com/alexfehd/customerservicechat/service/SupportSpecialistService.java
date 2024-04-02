package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.SupportSpecialist;

public interface SupportSpecialistService {

    void saveUser(SupportSpecialist user);

    void disconnect(SupportSpecialist user);

    SupportSpecialist findNextAvailableSpecialist();
}
