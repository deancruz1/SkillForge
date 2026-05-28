package com.skillforge.backend.dto;

import com.skillforge.backend.entity.LessonProgress;
import java.time.LocalDateTime;
import java.util.UUID;

public class LessonProgressResponse {

    private UUID lessonId;
    private String lessonTitle;
    private boolean completed;
    private LocalDateTime completedAt;

    public LessonProgressResponse(LessonProgress progress) {
        this.lessonId = progress.getLesson().getId();
        this.lessonTitle = progress.getLesson().getTitle();
        this.completed = progress.getCompleted();
        this.completedAt = progress.getCompletedAt();
    }

    public UUID getLessonId() { return lessonId; }
    public String getLessonTitle() { return lessonTitle; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}