package com.chat.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/*
	 * The registerStompEndpoints() method registers the /ws endpoint for websocket
	 * connections.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}

	/*
	 * The configureMessageBroker() method implements the default method in
	 * WebSocketMessageBrokerConfigurer to configure the message broker. It starts
	 * by calling enableSimpleBroker() to enable a simple memory-based message
	 * broker to carry the greeting messages back to the client on destinations
	 * prefixed with /topic
	 * It also designates the /app prefix for messages that are bound for methods 
	 * annotated with @MessageMapping. This prefix will be used to define all the 
	 * message mappings.
	 */

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

}
