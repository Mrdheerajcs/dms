package com.dmsBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // Get the Authorization header
//        String authHeader = request.getHeader("Authorization");
//        String username = null;
//        String jwtToken = null;
//
//        // Check if the header is present and starts with "Bearer "
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwtToken = authHeader.substring(7); // Extract token
//            username = jwtUtil.extractUsername(jwtToken); // Extract username from token
//        }
//
//        // Check if username is not null and no authentication is set in the context
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Load user details
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            // Validate the token
//            if (jwtUtil.validateToken(jwtToken, userDetails)) {
//                // Create an authentication token
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                // Set the authentication in the context
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        // Continue the filter chain
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        String username = null;
        String jwtToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Extract token
            username = jwtUtil.extractUsername(jwtToken); // Extract username from token
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}