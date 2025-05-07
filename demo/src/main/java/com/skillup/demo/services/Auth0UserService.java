package com.skillup.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.User;
import com.skillup.demo.repository.UserRepository;

import java.util.Optional;

@Service
public class Auth0UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get the current authenticated user from Auth0 JWT token
     */
    public User getCurrentUser() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException("User not authenticated");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        
        if (email == null) {
            throw new UserException("Email not found in token");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // Auto-register user if they don't exist
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(jwt.getClaim("nickname"));
        newUser.setName(jwt.getClaim("name"));
        
        // Set a secure random password since we're using Auth0 for authentication
        newUser.setPassword("AUTH0_USER");
        
        // Set profile picture if available
        if (jwt.getClaim("picture") != null) {
            newUser.setImage(jwt.getClaim("picture"));
        }
        
        return userRepository.save(newUser);
    }
}
