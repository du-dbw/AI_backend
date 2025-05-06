package com.example.backend.repository.Users;
import com.example.backend.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}