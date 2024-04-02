package com.alexfehd.customerservicechat.service.impl;

import com.alexfehd.customerservicechat.entity.User;
import com.alexfehd.customerservicechat.repository.ChatRoomRepository;
import com.alexfehd.customerservicechat.repository.UserRepository;
import com.alexfehd.customerservicechat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.alexfehd.customerservicechat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return  Optional.empty();
                });
    }

    @Override
    public List<User> findAllUsersWithOpenChatRooms(String recipientId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByRecipientId(recipientId);
        return chatRooms.stream().map(chatRoom -> userRepository.findByCustomerId(chatRoom.getSenderId()))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }


    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

}
