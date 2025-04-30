package com.example.backend.repository;

import com.example.backend.entity.Users.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.backend.dto.Users.RegisterRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class UsersRepositoryTest {

    @Autowired
    private com.example.backend.repository.Users.UsersRepository UsersRepository;

    @Test
    void findAll() {
        List<Users> users = UsersRepository.findAll();
        for (Users user : users) {
            System.out.println(user);
        }
    }

}