package com.skillforge.backend.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class LessonProgressId implements Serializable {

    private UUID studentId;
    private UUID lessonId;

    public LessonProgressId() {}

    public LessonProgressId(UUID studentId, UUID lessonId) {
        this.studentId = studentId;
        this.lessonId = lessonId;
    }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }
    public UUID getLessonId() { return lessonId; }
    public void setLessonId(UUID lessonId) { this.lessonId = lessonId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonProgressId)) return false;
        LessonProgressId that = (LessonProgressId) o;
        return studentId.equals(that.studentId) && lessonId.equals(that.lessonId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode() + lessonId.hashCode();
    }
}