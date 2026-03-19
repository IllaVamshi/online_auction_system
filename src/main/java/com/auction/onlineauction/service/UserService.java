package com.auction.onlineauction.service;

import com.auction.onlineauction.dto.RegisterRequest;
import com.auction.onlineauction.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    String authenticate(String email, String password);
    User findByEmail(String email);
    User updateUser(User user);
    void changePassword(String email, String currentPassword, String newPassword, String confirmPassword);
    void deleteAllUsers();
}
