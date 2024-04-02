package com.alexfehd.customerservicechat.service;

import com.alexfehd.customerservicechat.entity.ChatRoom;
import com.alexfehd.customerservicechat.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {

    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);

    List<User> findAllUsersWithOpenChatRooms(String recipientId);

}
