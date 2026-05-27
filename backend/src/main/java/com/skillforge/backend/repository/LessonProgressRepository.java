package com.skillforge.backend.repository;

import com.skillforge.backend.entity.LessonProgress;
import com.skillforge.backend.entity.LessonProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, LessonProgressId> {
    List<LessonProgress> findByStudentIdAndLessonCourseId(UUID studentId, UUID courseId);
    boolean existsByStudentIdAndLessonId(UUID studentId, UUID lessonId);
}