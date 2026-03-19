package com.auction.onlineauction.controller;

import com.auction.onlineauction.dto.ChangePasswordRequest;
import com.auction.onlineauction.dto.UserProfileDto;
import com.auction.onlineauction.dto.UserUpdateRequest;
import com.auction.onlineauction.entity.User;
import com.auction.onlineauction.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        // Get current user from security context
        String email = com.auction.onlineauction.config.SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);

        UserProfileDto profile = new UserProfileDto();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setRole("USER"); // For now, all users have USER role
        profile.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserUpdateRequest request) {
        String email = com.auction.onlineauction.config.SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);

        user.setName(request.getName());
        // Email updates might require additional verification, so we'll skip for now

        user = userService.updateUser(user);

        UserProfileDto profile = new UserProfileDto();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setRole("USER");
        profile.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        String email = com.auction.onlineauction.config.SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));

        userService.changePassword(email, request.getCurrentPassword(),
                                 request.getNewPassword(), request.getConfirmPassword());

        return ResponseEntity.ok().body(java.util.Map.of("message", "Password changed successfully"));
    }
}