package com.sergey1021.homefinance.repository;

import com.sergey1021.homefinance.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {


    Users findByEmail(String username);
    boolean existsByEmail(String email);

}
