package com.skillforge.backend.dto;

import com.skillforge.backend.entity.Lesson;
import java.time.LocalDateTime;
import java.util.UUID;

public class LessonResponse {

    private UUID id;
    private String title;
    private String content;
    private String videoUrl;
    private Integer orderIndex;
    private UUID courseId;
    private String courseTitle;
    private LocalDateTime createdAt;

    public LessonResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.content = lesson.getContent();
        this.videoUrl = lesson.getVideoUrl();
        this.orderIndex = lesson.getOrderIndex();
        this.courseId = lesson.getCourse().getId();
        this.courseTitle = lesson.getCourse().getTitle();
        this.createdAt = lesson.getCreatedAt();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getVideoUrl() { return videoUrl; }
    public Integer getOrderIndex() { return orderIndex; }
    public UUID getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}