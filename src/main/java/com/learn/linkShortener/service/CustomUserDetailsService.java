package com.learn.linkShortener.service;

import com.learn.linkShortener.entity.UserEntity;
import com.learn.linkShortener.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* 
   MANDATORY (Classic Spring Security Trap) 
   Without this import, bean exists But is never used for authentication
   Result → 401 every time
*/
import org.springframework.security.core.userdetails.UserDetailsService; 

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//System.out.println("\n>>> loadUserByUsername CALLED for: " + username);
    	log.info("Authenticating user");
    	
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                	log.warn("User not found");
                	return new UsernameNotFoundException("User not found");
                });
        
        log.info("User authenticated successfully");

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .authorities(
                        user.getRoles().stream()
                                .map(role -> "ROLE_" + role.getName())
                                .toArray(String[]::new)
                )
                .build();
    }
}

