package com.skillforge.backend.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class EnrollmentId implements Serializable {

    private UUID studentId;
    private UUID courseId;

    public EnrollmentId() {}

    public EnrollmentId(UUID studentId, UUID courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }
    public UUID getCourseId() { return courseId; }
    public void setCourseId(UUID courseId) { this.courseId = courseId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentId)) return false;
        EnrollmentId that = (EnrollmentId) o;
        return studentId.equals(that.studentId) && courseId.equals(that.courseId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode() + courseId.hashCode();
    }
}