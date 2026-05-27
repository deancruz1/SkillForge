package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    Optional<Quiz> findByCourseId(UUID courseId);
}