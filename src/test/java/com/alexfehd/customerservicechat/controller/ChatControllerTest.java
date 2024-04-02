package com.alexfehd.customerservicechat.controller;

import com.alexfehd.customerservicechat.entity.ChatMessage;
import com.alexfehd.customerservicechat.entity.ChatNotification;
import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.service.ChatMessageService;
import com.alexfehd.customerservicechat.service.ChatRoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplateMock;

    @Mock
    private ChatMessageService chatMessageServiceMock;

    @Mock
    private ChatRoomService chatRoomServiceMock;

    @InjectMocks
    private ChatController chatController;

    @Test
    void testProcessMessage() {
        // Given
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRecipientId("recipientId");
        ChatMessage savedMsg = new ChatMessage();
        savedMsg.setId("1");
        savedMsg.setSenderId("senderId");
        savedMsg.setRecipientId("recipientId");
        savedMsg.setContent("Test message");
        when(chatMessageServiceMock.save(chatMessage)).thenReturn(savedMsg);

        // When
        chatController.processMessage(chatMessage);

        // Then
        verify(chatMessageServiceMock).save(chatMessage);
        verify(messagingTemplateMock).convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(savedMsg.getId(), savedMsg.getSenderId(),
                        savedMsg.getRecipientId(), savedMsg.getContent()));
    }

    @Test
    void testFindChatMessages() {
        // Given
        String senderId = "senderId";
        String recipientId = "recipientId";
        List<ChatMessage> chatMessages = new ArrayList<>();
        when(chatMessageServiceMock.findChatMessages(senderId, recipientId)).thenReturn(chatMessages);

        // When
        ResponseEntity<List<ChatMessage>> responseEntity = chatController.findChatMessages(senderId, recipientId);

        // Then
        verify(chatMessageServiceMock).findChatMessages(senderId, recipientId);
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == chatMessages);
    }

    @Test
    void testFindAllOpenChats() {
        // Given
        String recipientId = "recipientId";
        List<User> users = new ArrayList<>();
        when(chatRoomServiceMock.findAllUsersWithOpenChatRooms(recipientId)).thenReturn(users);

        // When
        ResponseEntity<List<User>> responseEntity = chatController.findAllOpenChats(recipientId);

        // Then
        verify(chatRoomServiceMock).findAllUsersWithOpenChatRooms(recipientId);
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == users);
    }
}
