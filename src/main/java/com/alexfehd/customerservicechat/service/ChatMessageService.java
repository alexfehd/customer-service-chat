package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage message);

    List<ChatMessage> findChatMessages(String senderId, String recipientId);
}
