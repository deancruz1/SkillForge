package com.skillforge.backend.controller;

import com.skillforge.backend.dto.EnrollmentResponse;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<?> enroll(@PathVariable UUID courseId,
                                    @AuthenticationPrincipal User user) {
        enrollmentService.enroll(courseId, user);
        return ResponseEntity.ok(Map.of("message", "Enrolled successfully"));
    }

    @DeleteMapping("/courses/{courseId}/enroll")
    public ResponseEntity<?> unenroll(@PathVariable UUID courseId,
                                      @AuthenticationPrincipal User user) {
        enrollmentService.unenroll(courseId, user);
        return ResponseEntity.ok(Map.of("message", "Unenrolled successfully"));
    }

    @GetMapping("/enrollments/mine")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(@AuthenticationPrincipal User user) {
        List<EnrollmentResponse> enrollments = enrollmentService.getStudentEnrollments(user.getId())
                .stream()
                .map(EnrollmentResponse::new)
                .toList();
        return ResponseEntity.ok(enrollments);
    }
}