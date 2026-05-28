package com.skillforge.backend.controller;

import com.skillforge.backend.dto.CourseResponse;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Map<String, String> request,
                                          @AuthenticationPrincipal User user) {
        if (user.getRole() != User.UserRole.INSTRUCTOR) {
            return ResponseEntity.status(403).body(Map.of("error", "Only instructors can create courses"));
        }

        Course course = courseService.createCourse(
                request.get("title"),
                request.get("description"),
                user
        );

        return ResponseEntity.ok(Map.of(
                "id", course.getId().toString(),
                "title", course.getTitle(),
                "message", "Course created"
        ));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses()
                .stream()
                .map(CourseResponse::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable UUID id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CourseResponse>> getMyCourses(@AuthenticationPrincipal User user) {
        List<CourseResponse> courses = courseService.getCoursesByInstructor(user.getId())
                .stream()
                .map(CourseResponse::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable UUID id,
                                          @RequestBody Map<String, String> request,
                                          @AuthenticationPrincipal User user) {
        Course course = courseService.updateCourse(
                id,
                request.get("title"),
                request.get("description"),
                user
        );
        return ResponseEntity.ok(Map.of("message", "Course updated", "id", course.getId().toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID id,
                                          @AuthenticationPrincipal User user) {
        courseService.deleteCourse(id, user);
        return ResponseEntity.ok(Map.of("message", "Course deleted"));
    }
}