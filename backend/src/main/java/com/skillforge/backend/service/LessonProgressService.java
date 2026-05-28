package com.skillforge.backend.service;

import com.skillforge.backend.entity.Lesson;
import com.skillforge.backend.entity.LessonProgress;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.LessonProgressRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonService lessonService;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository, LessonService lessonService) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonService = lessonService;
    }

    public LessonProgress markComplete(UUID lessonId, User student) {
        if (student.getRole() != User.UserRole.STUDENT) {
            throw new RuntimeException("Only students can track lesson progress");
        }

        Lesson lesson = lessonService.getLessonById(lessonId);

        LessonProgress progress = lessonProgressRepository
                .findByStudentIdAndLessonId(student.getId(), lessonId)
                .orElse(new LessonProgress(student, lesson));

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        return lessonProgressRepository.save(progress);
    }

    public LessonProgress markIncomplete(UUID lessonId, User student) {
        if (student.getRole() != User.UserRole.STUDENT) {
            throw new RuntimeException("Only students can track lesson progress");
        }

        LessonProgress progress = lessonProgressRepository
                .findByStudentIdAndLessonId(student.getId(), lessonId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        progress.setCompleted(false);
        progress.setCompletedAt(null);
        return lessonProgressRepository.save(progress);
    }

    public List<LessonProgress> getProgressByCourse(UUID courseId, User student) {
        return lessonProgressRepository.findByStudentIdAndLessonCourseId(student.getId(), courseId);
    }

    public boolean isLessonCompleted(UUID lessonId, User student) {
        return lessonProgressRepository.existsByStudentIdAndLessonIdAndCompletedTrue(student.getId(), lessonId);
    }
}