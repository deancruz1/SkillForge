package com.skillforge.backend.dto;

import com.skillforge.backend.entity.Course;
import java.time.LocalDateTime;
import java.util.UUID;

public class CourseResponse {

    private UUID id;
    private String title;
    private String description;
    private String instructorName;
    private LocalDateTime createdAt;

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.instructorName = course.getInstructor().getFullName();
        this.createdAt = course.getCreatedAt();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorName() { return instructorName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}