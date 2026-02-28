package com.example.feedback.service;

import com.example.feedback.domain.Role;
import com.example.feedback.domain.User;
import com.example.feedback.dto.user.UserDto;
import com.example.feedback.exception.BusinessException;
import com.example.feedback.exception.NotFoundException;
import com.example.feedback.mapper.FeedbackMapper;
import com.example.feedback.repository.UserRepository;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDto toDto(User user) {
        return FeedbackMapper.toUserDto(user);
    }
}

