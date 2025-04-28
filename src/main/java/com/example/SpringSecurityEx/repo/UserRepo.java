package com.example.SpringSecurityEx.repo;

import com.example.SpringSecurityEx.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);

}
