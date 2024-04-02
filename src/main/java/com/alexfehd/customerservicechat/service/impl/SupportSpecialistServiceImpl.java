package com.alexfehd.customerservicechat.service.impl;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.SupportSpecialist;
import com.alexfehd.customerservicechat.repository.SupportSpecialistRepository;
import com.alexfehd.customerservicechat.service.SupportSpecialistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportSpecialistServiceImpl implements SupportSpecialistService {

    private final SupportSpecialistRepository repository;

    @Override
    public void saveUser(SupportSpecialist supportSpecialist) {
        supportSpecialist.setStatus(Status.ONLINE);
        repository.save(supportSpecialist);
    }

    @Override
    public void disconnect(SupportSpecialist supportSpecialist) {
        var storedSpecialist = repository.findById(supportSpecialist.getSupportSpecialistId()).orElse(null);
        if (storedSpecialist != null) {
            storedSpecialist.setStatus(Status.OFFLINE);
            repository.save(storedSpecialist);
        }
    }

    @Override
    public SupportSpecialist findNextAvailableSpecialist() {
        List<SupportSpecialist> availableSpecialists = repository.findAllByStatus(Status.ONLINE);
        if (availableSpecialists.isEmpty()) {
            log.info("SupportSpecialist is null");
            return null;
        } else {
            //TODO implement algorithm for choosing the next available specialist
            Random random = new Random();
            int randomIndex = random.nextInt(availableSpecialists.size());
            SupportSpecialist supportSpecialist = availableSpecialists.get(randomIndex);
            log.info("SupportSpecialist is: " + supportSpecialist);
            return supportSpecialist;
        }
    }
}
