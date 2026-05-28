package com.skillforge.backend.service;

import com.skillforge.backend.entity.*;
import com.skillforge.backend.repository.QuizAttemptRepository;
import com.skillforge.backend.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CourseService courseService;
    private final QuestionService questionService;

    public QuizService(QuizRepository quizRepository, QuizAttemptRepository quizAttemptRepository,
                       CourseService courseService, QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.courseService = courseService;
        this.questionService = questionService;
    }

    public Quiz createQuiz(UUID courseId, String title, String description, Integer passingScore, User instructor) {
        Course course = courseService.getCourseById(courseId);

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can create quizzes");
        }

        if (quizRepository.findByCourseId(courseId).isPresent()) {
            throw new RuntimeException("This course already has a quiz");
        }

        Quiz quiz = new Quiz(course, title, description, passingScore);
        return quizRepository.save(quiz);
    }

    public Quiz getQuizByCourse(UUID courseId) {
        return quizRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("No quiz found for this course"));
    }

    public QuizAttempt submitAttempt(UUID quizId, User student, List<String> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<Question> questions = questionService.getQuestionsByQuiz(quizId);

        if (answers.size() != questions.size()) {
            throw new RuntimeException("Must answer all questions");
        }

        int score = 0;
        int totalPoints = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            totalPoints += question.getPoints();

            if (answers.get(i) != null && answers.get(i).equalsIgnoreCase(String.valueOf(question.getCorrectAnswer()))) {
                score += question.getPoints();
            }
        }

        boolean passed = (score * 100 / totalPoints) >= quiz.getPassingScore();

        QuizAttempt attempt = new QuizAttempt(student, quiz, score, totalPoints, passed);
        return quizAttemptRepository.save(attempt);
    }

    public List<QuizAttempt> getAttempts(UUID quizId, User student) {
        return quizAttemptRepository.findByStudentIdAndQuizIdOrderByAttemptedAtDesc(student.getId(), quizId);
    }
}