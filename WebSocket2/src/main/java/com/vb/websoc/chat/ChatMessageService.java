package com.vb.websoc.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vb.websoc.chatroom.ChatRoomService;
import com.vb.websoc.repos.ChatMessageRepository;

@Service
public class ChatMessageService {
	@Autowired
	private ChatMessageRepository repository;
	@Autowired
	private ChatRoomService chatRoomService;

	public ChatMessage save(ChatMessage chatMessage) {
		var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
				.orElseThrow(); // You can create your own dedicated exception
		chatMessage.setChatId(chatId);
		repository.save(chatMessage);
		return chatMessage;
	}

	public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
		var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
		return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
	}
}
