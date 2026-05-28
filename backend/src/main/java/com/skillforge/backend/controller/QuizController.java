package com.skillforge.backend.controller;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/courses/{courseId}/quiz")
    public ResponseEntity<?> createQuiz(@PathVariable UUID courseId,
                                        @RequestBody Map<String, Object> request,
                                        @AuthenticationPrincipal User user) {
        String title = (String) request.get("title");
        String description = (String) request.get("description");
        Integer passingScore = request.get("passingScore") != null
                ? ((Number) request.get("passingScore")).intValue() : 60;

        quizService.createQuiz(courseId, title, description, passingScore, user);
        return ResponseEntity.ok(Map.of("message", "Quiz created"));
    }

    @GetMapping("/courses/{courseId}/quiz")
    public ResponseEntity<?> getQuiz(@PathVariable UUID courseId) {
        return ResponseEntity.ok(quizService.getQuizByCourse(courseId));
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable UUID quizId,
                                        @RequestBody Map<String, List<String>> request,
                                        @AuthenticationPrincipal User user) {
        var attempt = quizService.submitAttempt(quizId, user, request.get("answers"));
        return ResponseEntity.ok(Map.of(
                "score", attempt.getScore(),
                "totalPoints", attempt.getTotalPoints(),
                "passed", attempt.getPassed()
        ));
    }

    @GetMapping("/quizzes/{quizId}/attempts")
    public ResponseEntity<?> getAttempts(@PathVariable UUID quizId,
                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizService.getAttempts(quizId, user));
    }
}