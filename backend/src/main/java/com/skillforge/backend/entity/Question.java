package com.skillforge.backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "correct_answer", nullable = false)
    private Character correctAnswer;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Integer points = 1;

    public Question() {}

    public Question(Quiz quiz, String questionText, Character correctAnswer, Integer orderIndex, Integer points) {
        this.quiz = quiz;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.orderIndex = orderIndex;
        this.points = points;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public Character getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(Character correctAnswer) { this.correctAnswer = correctAnswer; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    @Override
    public String toString() { return questionText; }
}