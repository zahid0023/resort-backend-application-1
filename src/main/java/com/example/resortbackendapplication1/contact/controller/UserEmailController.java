package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.contact.dto.request.useremail.CreateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.request.useremail.UpdateUserEmailRequest;
import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;
import com.example.resortbackendapplication1.contact.service.UserEmailService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{user-id}/emails")
public class UserEmailController {

    private final UserEmailService userEmailService;
    private final UserService userService;

    public UserEmailController(UserEmailService userEmailService, UserService userService) {
        this.userEmailService = userEmailService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody CreateUserEmailRequest request) {
        UserEntity userEntity = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEmailService.create(request, userEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("user-id") Long userId,
            @ParameterObject PaginatedRequest paginatedRequest) {
        userService.getUserById(userId);
        return ResponseEntity.ok(userEmailService.getAll(userId, paginatedRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("user-id") Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(userEmailService.getById(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("user-id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserEmailRequest request) {
        UserEmailEntity entity = userEmailService.getEntityById(userId, id);
        return ResponseEntity.ok(userEmailService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("user-id") Long userId,
            @PathVariable Long id) {
        UserEmailEntity entity = userEmailService.getEntityById(userId, id);
        return ResponseEntity.ok(userEmailService.delete(entity));
    }
}
