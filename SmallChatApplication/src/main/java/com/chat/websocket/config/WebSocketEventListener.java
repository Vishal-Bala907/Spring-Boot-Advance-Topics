package com.chat.websocket.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chat.modals.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Several ApplicationContext events are published and can be received by 
 * implementing Spring’s ApplicationListener interface:
 * 
 * BrokerAvailabilityEvent: Indicates when the broker becomes available or unavailable. 
 * 
 * SessionConnectEvent: Published when a new STOMP CONNECT is received to indicate 
 * the start of a new client session. The event contains the message that represents 
 * the connect, including the session ID, user information (if any), and any custom 
 * headers the client sent.
 * 
 * SessionConnectedEvent: Published shortly after a SessionConnectEvent 
 * when the broker has sent a STOMP CONNECTED frame in response to the CONNECT.
 *  At this point, the STOMP session can be considered fully established.
 *  
 *  SessionSubscribeEvent: Published when a new STOMP SUBSCRIBE is received.
 *
 *	SessionUnsubscribeEvent: Published when a new STOMP UNSUBSCRIBE is received.
 *
 *	SessionDisconnectEvent: Published when a STOMP session ends. 
 *	The DISCONNECT may have been sent from the client or it may be automatically generated 
 *	when the WebSocket session is closed. In some cases, this event is published more than 
 *	once per session. Components should be idempotent with regard to multiple disconnect 
 *	events.
 * */

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	private final SimpMessageSendingOperations operations;
	
	@EventListener
	public void handleWebsocketDisconnetListener(SessionDisconnectEvent disconnectEvent) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
		String username = (String)accessor.getSessionAttributes().get("username");
		if(username != null) {
			log.info("User disconnected {}" + username);
			var message = com.chat.modals.ChatMessage.builder()
										.type(MessageType.LEAVE)
										.sender(username)
										.build();
			operations.convertAndSend("/topic/public",message);
		}
	}
}
