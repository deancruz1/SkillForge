package com.skillforge.backend.repository;

import com.skillforge.backend.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    List<QuizAttempt> findByStudentIdAndQuizIdOrderByAttemptedAtDesc(UUID studentId, UUID quizId);
}