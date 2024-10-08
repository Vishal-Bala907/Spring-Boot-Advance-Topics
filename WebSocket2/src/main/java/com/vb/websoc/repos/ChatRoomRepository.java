package com.vb.websoc.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vb.websoc.chatroom.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
