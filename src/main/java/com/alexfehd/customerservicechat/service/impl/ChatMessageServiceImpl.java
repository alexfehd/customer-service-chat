package com.alexfehd.customerservicechat.service.impl;

import com.alexfehd.customerservicechat.entity.ChatMessage;
import com.alexfehd.customerservicechat.repository.ChatMessageRepository;
import com.alexfehd.customerservicechat.service.ChatMessageService;
import com.alexfehd.customerservicechat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage message) {
        var chatId = chatRoomService
                .getChatRoomId(message.getSenderId(), message.getRecipientId(), true)
                .orElseThrow(); //TODO create custom exception
        message.setChatId(chatId);
        repository.save(message);
        return message;
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
