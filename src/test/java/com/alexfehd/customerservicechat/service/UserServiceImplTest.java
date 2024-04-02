package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.repository.UserRepository;
import com.alexfehd.customerservicechat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repositoryMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser() {
        // Given
        User user = new User();
        user.setCustomerId("testCustomerId");

        // When
        userService.saveUser(user);

        // Then
        verify(repositoryMock).save(user);
        assert(user.getStatus() == Status.ONLINE);
    }

    @Test
    void testDisconnect() {
        // Given
        User storedUser = new User();
        storedUser.setCustomerId("testCustomerId");
        storedUser.setStatus(Status.ONLINE);
        when(repositoryMock.findById("testCustomerId")).thenReturn(Optional.of(storedUser));

        // When
        userService.disconnect(storedUser);

        // Then
        verify(repositoryMock).save(storedUser);
        assert(storedUser.getStatus() == Status.OFFLINE);
    }

}
