package com.alexfehd.customerservicechat.controller;

import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private UserController userController;

    @Test
    void testAddUser() {
        // Given
        User user = new User();

        // When
        userController.addUser(user);

        // Then
        verify(userServiceMock).saveUser(user);
    }

    @Test
    void testDisconnectUser() {
        // Given
        User user = new User();

        // When
        userController.disconnectUser(user);

        // Then
        verify(userServiceMock).disconnect(user);
    }
}
