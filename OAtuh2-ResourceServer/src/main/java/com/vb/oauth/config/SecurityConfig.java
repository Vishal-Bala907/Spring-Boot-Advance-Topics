package com.vb.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
//@CrossOrigin("*")
public class SecurityConfig {

	@Bean
	SecurityFilterChain chain(HttpSecurity security) throws Exception {

		security.cors(Customizer.withDefaults());
		security.csrf(AbstractHttpConfigurer::disable);

		security.authorizeHttpRequests(req -> req.
				requestMatchers("api/v1/**").authenticated()
				.anyRequest().permitAll());
		security.oauth2ResourceServer(authServer -> authServer.jwt(Customizer.withDefaults()));

		return security.build();
	}

}
