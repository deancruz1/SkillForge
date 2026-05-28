package com.skillforge.backend.controller;

import com.skillforge.backend.entity.Lesson;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.skillforge.backend.dto.LessonResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@PathVariable UUID courseId,
                                          @RequestBody Map<String, String> request,
                                          @AuthenticationPrincipal User user) {
        Lesson lesson = lessonService.createLesson(
                courseId,
                request.get("title"),
                request.get("content"),
                request.get("videoUrl"),
                request.get("orderIndex") != null ? Integer.parseInt(request.get("orderIndex")) : 0,
                user
        );
        return ResponseEntity.ok(Map.of("id", lesson.getId().toString(), "message", "Lesson created"));
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getLessons(@PathVariable UUID courseId) {
        List<LessonResponse> lessons = lessonService.getLessonsByCourse(courseId)
                .stream()
                .map(LessonResponse::new)
                .toList();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getLesson(@PathVariable UUID lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(new LessonResponse(lesson));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable UUID lessonId,
                                          @RequestBody Map<String, String> request,
                                          @AuthenticationPrincipal User user) {
        Lesson lesson = lessonService.updateLesson(
                lessonId,
                request.get("title"),
                request.get("content"),
                request.get("videoUrl"),
                request.get("orderIndex") != null ? Integer.parseInt(request.get("orderIndex")) : 0,
                user
        );
        return ResponseEntity.ok(Map.of("message", "Lesson updated", "id", lesson.getId().toString()));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable UUID lessonId,
                                          @AuthenticationPrincipal User user) {
        lessonService.deleteLesson(lessonId, user);
        return ResponseEntity.ok(Map.of("message", "Lesson deleted"));
    }
}