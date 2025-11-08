package com.aditya.inventory.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	// authenticate users using JWTs instead of session cookies.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String jwt = parseJwt(request);                                                             // Extract JWT from the request header.
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {                                        // Validate the JWT.
				String username = jwtUtils.getUserNameFromJwtToken(jwt);                                // Extract the username from the JWT.

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);              // Load user details from the database.

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());                                // Create an Authentication object with user info and roles.
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // Set the authentication in Spring Security’s context.

				SecurityContextHolder.getContext().setAuthentication(authentication);                    // Continue the request processing.
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication ", e);
		}

		filterChain.doFilter(request, response);
	}

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
        throws ServletException{
            String path = request.getServletPath();
            return path.startsWith("/auth/") || path.startsWith("/User/signup") || path.startsWith("/User/verify");

    }

	// --------Extract Jwt from header----------//
	private String parseJwt(HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt == null || jwt.trim().isEmpty() || jwt.equalsIgnoreCase("null")) {
            return null;
        }
		return jwt;
	}
}
