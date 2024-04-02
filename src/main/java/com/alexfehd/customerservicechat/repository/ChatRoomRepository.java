package com.alexfehd.customerservicechat.repository;

import com.alexfehd.customerservicechat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

    List<ChatRoom> findAllByRecipientId(String recipientId);

}
