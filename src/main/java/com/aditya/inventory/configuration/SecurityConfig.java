package com.aditya.inventory.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aditya.inventory.jwt.AuthEntryPoint;
import com.aditya.inventory.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	DataSource dataSource;

	@Autowired
	private AuthEntryPoint unauthorizehandler;

	// -----------------------------Custom Authentication Filter-------------------------------//

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	// -------------------------------Password Encoder----------------------------------//
	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	// -------------------------------Manage Authentication----------------------------//
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	// -----------------------------Custom security Filter Chain-------------------------------//
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				auth -> auth
				.requestMatchers("/auth/**", "/inventoryManagement/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**")
				.permitAll()
				.anyRequest()
				.authenticated());                                                                          //This endpoints not required any authentication and autherization
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //Removing session , no session stored at server
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizehandler));        // Custom Exeception handled for unauthorized user
		http.csrf(csrf -> csrf.disable());																	//Disable Csrf token because we are using Jwt
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);   //Run jwt filter before authentication filter
		return http.build();                                                                                //Build security configuration
	}
}
