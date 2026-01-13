package com.example.libra.service;

import com.example.libra.model.Role;
import com.example.libra.model.User;
import com.example.libra.repository.MemberRepo;
import com.example.libra.repository.UserRepo;
import com.example.libra.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepo userRepo;
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public String register(String username, String email, String password, Role role, Long memberId) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        
        if (memberId != null) {
            user.setMember(memberRepo.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found")));
        }
        
        user.setEnabled(true);
        userRepo.save(user);
        
        return jwtUtil.generateToken(username, role.name());
    }
    
    public String login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }
        
        return jwtUtil.generateToken(username, user.getRole().name());
    }
}