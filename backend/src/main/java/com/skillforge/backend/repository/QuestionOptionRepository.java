package com.skillforge.backend.repository;

import com.skillforge.backend.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {
    List<QuestionOption> findByQuestionIdOrderByOptionLabelAsc(UUID questionId);
}