package com.vb.websoc.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/*
	 * The configureMessageBroker() method implements the default method in
	 * WebSocketMessageBrokerConfigurer to configure the message broker. It starts
	 * by calling enableSimpleBroker() to enable a simple memory-based message
	 * broker to carry the greeting messages back to the client on destinations
	 * prefixed with /topic It also designates the /app prefix for messages that are
	 * bound for methods annotated with @MessageMapping. This prefix will be used to
	 * define all the message mappings.
	 */

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		/*
		 * Purpose: This method enables a simple, in-memory message broker that handles
		 * messages sent to destinations prefixed with /user.
		 */

		/*
		 * The broker will send messages to users who are subscribed to specific
		 * destinations. For example, when a message is sent to /user/someUserId/queue,
		 * it will be routed to a particular user. The /user prefix is typically used
		 * for user-specific queues. In other words, messages sent to /user/{username}
		 * will be delivered directly to a specific user.
		 */

		registry.enableSimpleBroker("/user");

		/*
		 * This method sets a prefix for messages that are bound for message-handling
		 * methods in your Spring controllers (i.e., annotated with @MessageMapping).
		 * 
		 * Any message whose destination starts with /app will be routed to
		 * message-handling methods within your Spring application. For example, a
		 * client sends a message to /app/chat, and the framework will look for a method
		 * annotated with @MessageMapping("/chat") in your controller to handle that
		 * message.
		 */

		registry.setApplicationDestinationPrefixes("/app");

		/*
		 * This method sets the prefix used for user-specific destinations (i.e.,
		 * messages targeted at specific users).
		 * 
		 * It tells Spring that any destination starting with /user should be treated as
		 * a user destination. When using this, Spring internally resolves /user/
		 * destinations and translates them into destinations for individual users. For
		 * example, if you send a message to /user/someUser/queue/private, Spring will
		 * resolve this to /queue/private for the user someUser.
		 */
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		/**
		 * Register a STOMP over WebSocket endpoint at the given mapping path. Our
		 * Webscket path
		 */
		/*
		 * This method defines a WebSocket endpoint at the specified path (/ws in this
		 * case). 
		 * Clients can connect to this endpoint to establish a WebSocket
		 * connection. This endpoint is where the WebSocket server will listen for
		 * incoming connections from clients.
		 * 
		 * withSockJS() This method enables SockJS support for the WebSocket endpoint.
		 */
		registry.addEndpoint("/ws").withSockJS();
	}

	/*
	 * This method overrides a method in a superclass or an interface. The purpose
	 * is to customize the list of message converters used by the Spring framework.
	 * The method takes a List<MessageConverter> as a parameter, which represents
	 * the current message converters.
	 */
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		/*
		 * A DefaultContentTypeResolver is instantiated to resolve content types for
		 * messages. setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON) sets the default
		 * MIME type to JSON. This means that if the content type is not specified, it
		 * will default to application/json.
		 */
		DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
		contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		
		
		/*
		 * A MappingJackson2MessageConverter is created, which is a message converter
		 * that uses Jackson for JSON serialization and deserialization.
		 * setObjectMapper(new ObjectMapper()) allows you to configure a custom
		 * ObjectMapper for serialization and deserialization of JSON. The ObjectMapper
		 * can be customized for various features (like pretty printing, handling of
		 * nulls, etc.). setContentTypeResolver(contentTypeResolver) sets the previously
		 * created DefaultContentTypeResolver to this converter, which defines how
		 * content types are resolved.
		 */
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		converter.setContentTypeResolver(contentTypeResolver);
		
		/*
		 * The configured MappingJackson2MessageConverter is added to the list of
		 * message converters provided as an argument to the method.
		 */
		messageConverters.add(converter);
		
		/*
		 * The method returns false, which typically indicates that no additional
		 * default converters should be added beyond those already configured.
		 */
		return false;
	}
	
	

}
