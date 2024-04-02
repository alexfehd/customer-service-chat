package com.alexfehd.customerservicechat.controller;

import com.alexfehd.customerservicechat.entity.SupportSpecialist;
import com.alexfehd.customerservicechat.service.SupportSpecialistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportSpecialistControllerTest {

    @Mock
    private SupportSpecialistService supportSpecialistServiceMock;

    @InjectMocks
    private SupportSpecialistController supportSpecialistController;

    @Test
    void testAddUser() {
        // Given
        SupportSpecialist user = new SupportSpecialist();

        // When
        supportSpecialistController.addUser(user);

        // Then
        verify(supportSpecialistServiceMock).saveUser(user);
    }

    @Test
    void testDisconnectUser() {
        // Given
        SupportSpecialist user = new SupportSpecialist();

        // When
        supportSpecialistController.disconnectUser(user);

        // Then
        verify(supportSpecialistServiceMock).disconnect(user);
    }

    @Test
    void testFindSupportSpecialist() {
        // Given
        SupportSpecialist specialist = new SupportSpecialist();
        when(supportSpecialistServiceMock.findNextAvailableSpecialist()).thenReturn(specialist);

        // When
        ResponseEntity<SupportSpecialist> responseEntity = supportSpecialistController.findSupportSpecialist();

        // Then
        verify(supportSpecialistServiceMock).findNextAvailableSpecialist();
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == specialist);
    }
}