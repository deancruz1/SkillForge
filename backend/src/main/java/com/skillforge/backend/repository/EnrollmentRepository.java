package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Enrollment;
import com.skillforge.backend.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    List<Enrollment> findByStudentId(UUID studentId);
    List<Enrollment> findByCourseId(UUID courseId);
    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);
}