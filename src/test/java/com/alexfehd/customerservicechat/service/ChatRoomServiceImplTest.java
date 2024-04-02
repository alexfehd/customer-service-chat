package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.ChatRoom;
import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.repository.ChatRoomRepository;
import com.alexfehd.customerservicechat.repository.UserRepository;
import com.alexfehd.customerservicechat.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Test
    void testGetChatRoomIdWhenRoomExists() {
        // Given
        String id = "id";
        String senderId = "senderId";
        String recipientId = "recipientId";
        String chatId = "chatId";
        ChatRoom chatRoom = new ChatRoom(id, chatId, senderId, recipientId);
        when(chatRoomRepositoryMock.findBySenderIdAndRecipientId(senderId, recipientId)).thenReturn(Optional.of(chatRoom));

        // When
        Optional<String> result = chatRoomService.getChatRoomId(senderId, recipientId, false);

        // Then
        verify(chatRoomRepositoryMock).findBySenderIdAndRecipientId(senderId, recipientId);
        assert(result.isPresent());
        assert(result.get().equals(chatId));
    }

    @Test
    void testGetChatRoomIdWhenRoomDoesNotExistAndCreateNewRoomIsTrue() {
        // Given
        String senderId = "senderId";
        String recipientId = "recipientId";
        String chatId = senderId + "_" + recipientId;
        when(chatRoomRepositoryMock.findBySenderIdAndRecipientId(senderId, recipientId)).thenReturn(Optional.empty());

        // When
        Optional<String> result = chatRoomService.getChatRoomId(senderId, recipientId, true);

        // Then
        verify(chatRoomRepositoryMock).findBySenderIdAndRecipientId(senderId, recipientId);
        assert(result.isPresent());
        assert(result.get().equals(chatId));
        verify(chatRoomRepositoryMock, times(2)).save(any(ChatRoom.class));
    }

    @Test
    void testGetChatRoomIdWhenRoomDoesNotExistAndCreateNewRoomIsFalse() {
        // Given
        String senderId = "senderId";
        String recipientId = "recipientId";
        when(chatRoomRepositoryMock.findBySenderIdAndRecipientId(senderId, recipientId)).thenReturn(Optional.empty());

        // When
        Optional<String> result = chatRoomService.getChatRoomId(senderId, recipientId, false);

        // Then
        verify(chatRoomRepositoryMock).findBySenderIdAndRecipientId(senderId, recipientId);
        assert(result.isEmpty());
        verify(chatRoomRepositoryMock, never()).save(any(ChatRoom.class));
    }

    @Test
    void testFindAllUsersWithOpenChatRooms() {
        // Given
        String recipientId = "recipientId";

        ChatRoom chatRoom1 = ChatRoom.builder()
                .chatId("chatId1")
                .senderId("senderId1")
                .recipientId("recipientId")
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .chatId("chatId2")
                .senderId("senderId2")
                .recipientId("recipientId")
                .build();
        List<ChatRoom> list = new ArrayList<>();
        list.add(chatRoom1);
        list.add(chatRoom2);

        User user1 = new User();
        User user2 = new User();
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user2);

        when(chatRoomRepositoryMock.findAllByRecipientId(any())).thenReturn(list);
        when(userRepositoryMock.findByCustomerId(anyString())).thenReturn(user1, user2);

        //When
        List<User> result = chatRoomService.findAllUsersWithOpenChatRooms(recipientId);

        // Then
        verify(chatRoomRepositoryMock).findAllByRecipientId(recipientId);
        verify(userRepositoryMock, times(2)).findByCustomerId(anyString());
        assert(result.size() == 2);
        assert(result.containsAll(expectedUsers));
    }
}
