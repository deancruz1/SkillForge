package com.skillforge.backend.dto;

import com.skillforge.backend.entity.Enrollment;
import java.time.LocalDateTime;
import java.util.UUID;

public class EnrollmentResponse {

    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;

    public EnrollmentResponse(Enrollment enrollment) {
        this.studentId = enrollment.getStudent().getId();
        this.studentName = enrollment.getStudent().getFullName();
        this.courseId = enrollment.getCourse().getId();
        this.courseTitle = enrollment.getCourse().getTitle();
        this.enrolledAt = enrollment.getEnrolledAt();
    }

    public UUID getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public UUID getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
}