package com.skillforge.backend.service;

import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String title, String description, User instructor) {
        Course course = new Course(title, description, instructor);
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getCoursesByInstructor(UUID instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public Course updateCourse(UUID id, String title, String description, User instructor) {
        Course course = getCourseById(id);

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can update this course");
        }

        course.setTitle(title);
        course.setDescription(description);
        return courseRepository.save(course);
    }

    public void deleteCourse(UUID id, User instructor) {
        Course course = getCourseById(id);

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can delete this course");
        }

        courseRepository.delete(course);
    }
}