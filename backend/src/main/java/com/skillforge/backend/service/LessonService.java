package com.skillforge.backend.service;

import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.Lesson;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    public LessonService(LessonRepository lessonRepository, CourseService courseService) {
        this.lessonRepository = lessonRepository;
        this.courseService = courseService;
    }

    public Lesson createLesson(UUID courseId, String title, String content, String videoUrl, Integer orderIndex, User instructor) {
        Course course = courseService.getCourseById(courseId);

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can add lessons");
        }

        Lesson lesson = new Lesson(course, title, content, videoUrl, orderIndex);
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByCourse(UUID courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    public Lesson getLessonById(UUID id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public Lesson updateLesson(UUID id, String title, String content, String videoUrl, Integer orderIndex, User instructor) {
        Lesson lesson = getLessonById(id);

        if (!lesson.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can update lessons");
        }

        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setVideoUrl(videoUrl);
        lesson.setOrderIndex(orderIndex);
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(UUID id, User instructor) {
        Lesson lesson = getLessonById(id);

        if (!lesson.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can delete lessons");
        }

        lessonRepository.delete(lesson);
    }
}