package com.vb.websoc.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {
	
	/*
	 * The SimpMessagingTemplate is a Spring component used to send messages to
	 * WebSocket-connected clients. It provides convenient methods for sending
	 * messages to specific users or broadcasting them to all clients.
	 */
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	@Autowired
    private ChatMessageService chatMessageService;

	/*
	 * The @MessageMapping("/chat") annotation indicates that this method will
	 * handle messages sent to the /app/chat destination from WebSocket clients.
	 * When a client sends a message to /app/chat, it will be routed to this method
	 * for processing.
	 */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
		/*
		 * The method convertAndSendToUser() sends a message to a specific user. 
		 * Here’s how it works: 
		 * Recipient ID: chatMessage.getRecipientId() specifies the ID of the user 
		 * 				who will receive the message. 
		 * Destination: "/queue/messages" is the destination where the message will 
		 * 					be sent. This destination is typically where the 
		 * 					recipient is subscribed to receive messages. 
		 * Message Payload: A new ChatNotification object is created with details 
		 * 					about the saved message,
		 * 					including: 
		 * 					savedMsg.getId(): The ID of the saved message.
		 * 					savedMsg.getSenderId(): The sender's ID. 
		 * 					savedMsg.getRecipientId(): The recipient's 
		 * 					ID. savedMsg.getContent(): The content of the chat message.
		 */
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}