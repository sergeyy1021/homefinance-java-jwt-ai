package com.sergey1021.homefinance.service;

import com.sergey1021.homefinance.entity.UserPrincipal;
import com.sergey1021.homefinance.entity.Users;
import com.sergey1021.homefinance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    public UserDetails loadUserByEmail(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByEmail(username);
    }

    public String registerUser(Users user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "User already exists";
        } else {
            userRepository.save(user);
            return "User registered successfully";
        }
    }
}

