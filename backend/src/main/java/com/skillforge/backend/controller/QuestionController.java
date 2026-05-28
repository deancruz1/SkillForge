package com.skillforge.backend.controller;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable UUID quizId,
                                         @RequestBody Map<String, Object> request,
                                         @AuthenticationPrincipal User user) {
        String questionText = (String) request.get("questionText");
        Character correctAnswer = ((String) request.get("correctAnswer")).charAt(0);
        Integer orderIndex = ((Number) request.get("orderIndex")).intValue();
        Integer points = request.get("points") != null
                ? ((Number) request.get("points")).intValue() : 1;
        List<Map<String, String>> options = (List<Map<String, String>>) request.get("options");

        questionService.addQuestion(quizId, questionText, correctAnswer, orderIndex, points, options, user);
        return ResponseEntity.ok(Map.of("message", "Question added"));
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<?> getQuestions(@PathVariable UUID quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuiz(quizId));
    }

    @GetMapping("/questions/{questionId}/options")
    public ResponseEntity<?> getOptions(@PathVariable UUID questionId) {
        return ResponseEntity.ok(questionService.getOptions(questionId));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable UUID questionId,
                                            @AuthenticationPrincipal User user) {
        questionService.deleteQuestion(questionId, user);
        return ResponseEntity.ok(Map.of("message", "Question deleted"));
    }
}