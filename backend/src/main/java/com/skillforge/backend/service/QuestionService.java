package com.skillforge.backend.service;

import com.skillforge.backend.entity.*;
import com.skillforge.backend.repository.QuestionOptionRepository;
import com.skillforge.backend.repository.QuestionRepository;
import com.skillforge.backend.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionOptionRepository questionOptionRepository,
                           QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.quizRepository = quizRepository;
    }

    public Question addQuestion(UUID quizId, String questionText, Character correctAnswer,
                                Integer orderIndex, Integer points, List<Map<String, String>> options,
                                User instructor) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (!quiz.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can add questions");
        }

        Question question = new Question(quiz, questionText, correctAnswer, orderIndex, points);
        question = questionRepository.save(question);

        for (Map<String, String> option : options) {
            QuestionOption qo = new QuestionOption(
                    question,
                    option.get("label").charAt(0),
                    option.get("text")
            );
            questionOptionRepository.save(qo);
        }

        return question;
    }

    public List<Question> getQuestionsByQuiz(UUID quizId) {
        return questionRepository.findByQuizIdOrderByOrderIndexAsc(quizId);
    }

    public List<QuestionOption> getOptions(UUID questionId) {
        return questionOptionRepository.findByQuestionIdOrderByOptionLabelAsc(questionId);
    }

    public void deleteQuestion(UUID questionId, User instructor) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getQuiz().getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("Only the course instructor can delete questions");
        }

        questionRepository.delete(question);
    }
}