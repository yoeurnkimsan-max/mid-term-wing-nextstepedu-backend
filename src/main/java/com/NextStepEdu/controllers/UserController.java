package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.UpdateStatusRequest;
import com.NextStepEdu.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer id,
            @RequestBody UpdateStatusRequest request
    ) {
        userService.updateUserStatus(id, request.status());
        return ResponseEntity.ok("User status updated");
    }
}
