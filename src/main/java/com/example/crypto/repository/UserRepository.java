package com.example.crypto.repository;

import com.example.crypto.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByChatId(Long chatId);

    void deleteUserByChatId(Long chatId);
}
