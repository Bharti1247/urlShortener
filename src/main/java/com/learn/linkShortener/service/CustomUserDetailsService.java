package com.learn.linkShortener.service;

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

import com.learn.linkShortener.entity.UserEntity;
import com.learn.linkShortener.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//System.out.println("\n>>> loadUserByUsername CALLED for: " + username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

