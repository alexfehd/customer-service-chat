package com.alexfehd.customerservicechat.controller;

import com.alexfehd.customerservicechat.entity.SupportSpecialist;
import com.alexfehd.customerservicechat.service.SupportSpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SupportSpecialistController {

    private final SupportSpecialistService supportSpecialistService;

    @MessageMapping("/support.addUser")
    @SendTo("/support/public")
    public SupportSpecialist addUser(
            @Payload SupportSpecialist user
    ) {
        supportSpecialistService.saveUser(user);
        return user;
    }

    @MessageMapping("/support.disconnectUser")
    @SendTo("/support/public")
    public SupportSpecialist disconnectUser(
            @Payload SupportSpecialist user
    ) {
        supportSpecialistService.disconnect(user);
        return user;
    }

    @GetMapping("/support-specialist")
    public ResponseEntity<SupportSpecialist> findSupportSpecialist() {
        return ResponseEntity.ok(supportSpecialistService.findNextAvailableSpecialist());
    }

}
