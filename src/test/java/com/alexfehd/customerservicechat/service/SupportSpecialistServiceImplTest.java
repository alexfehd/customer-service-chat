package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.SupportSpecialist;
import com.alexfehd.customerservicechat.repository.SupportSpecialistRepository;
import com.alexfehd.customerservicechat.service.impl.SupportSpecialistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportSpecialistServiceImplTest {

    @Mock
    private SupportSpecialistRepository repositoryMock;

    @InjectMocks
    private SupportSpecialistServiceImpl serviceImpl;

    @Test
    void testSaveUser() {
        // Given
        SupportSpecialist supportSpecialist = new SupportSpecialist();

        // When
        serviceImpl.saveUser(supportSpecialist);

        // Then
        verify(repositoryMock).save(supportSpecialist);
    }

    @Test
    void testDisconnect() {
        // Given
        SupportSpecialist supportSpecialist = new SupportSpecialist();
        supportSpecialist.setSupportSpecialistId("123");

        when(repositoryMock.findById("123")).thenReturn(Optional.of(supportSpecialist));

        // When
        serviceImpl.disconnect(supportSpecialist);

        // Then
        verify(repositoryMock).save(supportSpecialist);
        assert(supportSpecialist.getStatus() == Status.OFFLINE);
    }

    @Test
    void testFindNextAvailableSpecialist() {
        // Given
        SupportSpecialist specialist1 = new SupportSpecialist();
        specialist1.setSupportSpecialistId("1");
        SupportSpecialist specialist2 = new SupportSpecialist();
        specialist2.setSupportSpecialistId("2");

        List<SupportSpecialist> availableSpecialists = new ArrayList<>();
        availableSpecialists.add(specialist1);
        availableSpecialists.add(specialist2);

        when(repositoryMock.findAllByStatus(Status.ONLINE)).thenReturn(availableSpecialists);

        // When
        SupportSpecialist result = serviceImpl.findNextAvailableSpecialist();

        // Then
        assert(availableSpecialists.contains(result));
    }
}
