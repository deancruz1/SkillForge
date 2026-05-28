package com.skillforge.backend.controller;

import com.skillforge.backend.dto.LessonProgressResponse;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.LessonProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;

    public LessonProgressController(LessonProgressService lessonProgressService) {
        this.lessonProgressService = lessonProgressService;
    }

    @PostMapping("/lessons/{lessonId}/complete")
    public ResponseEntity<?> markComplete(@PathVariable UUID lessonId,
                                          @AuthenticationPrincipal User user) {
        lessonProgressService.markComplete(lessonId, user);
        return ResponseEntity.ok(Map.of("message", "Lesson marked complete"));
    }

    @PostMapping("/lessons/{lessonId}/incomplete")
    public ResponseEntity<?> markIncomplete(@PathVariable UUID lessonId,
                                            @AuthenticationPrincipal User user) {
        lessonProgressService.markIncomplete(lessonId, user);
        return ResponseEntity.ok(Map.of("message", "Lesson marked incomplete"));
    }

    @GetMapping("/courses/{courseId}/progress")
    public ResponseEntity<List<LessonProgressResponse>> getProgress(@PathVariable UUID courseId,
                                                                    @AuthenticationPrincipal User user) {
        List<LessonProgressResponse> progress = lessonProgressService
                .getProgressByCourse(courseId, user)
                .stream()
                .map(LessonProgressResponse::new)
                .toList();
        return ResponseEntity.ok(progress);
    }
}