package com.chat.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.chat.modals.ChatMessage;

@Controller
public class ChatController {

	/*
	 * The @MessageMapping annotation ensures that, if a message is sent to the
	 * /hello destination, the sendMessage() method is called.
	 * 
	 * The payload of the message is bound to a ChatMessage object, which is passed
	 * into sendMessage().
	 * 
	 * The return value is broadcast to all subscribers of /topic/public, as
	 * specified in the @SendTo annotation
	 */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage message) {
		return message;
	}
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, 
			SimpMessageHeaderAccessor accessor) {
		// Add username in the web Socket session
		accessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}
}
