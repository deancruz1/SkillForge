package com.skillforge.backend.service;

import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.Enrollment;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
    }

    public Enrollment enroll(UUID courseId, User student) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Course course = courseService.getCourseById(courseId);
        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(UUID studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getCourseStudents(UUID courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public void unenroll(UUID courseId, User student) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new RuntimeException("Not enrolled in this course"));

        enrollmentRepository.delete(enrollment);
    }
}