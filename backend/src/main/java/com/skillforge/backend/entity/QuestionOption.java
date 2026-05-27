package com.skillforge.backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "question_options")
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_label", nullable = false)
    private Character optionLabel;

    @Column(name = "option_text", nullable = false, columnDefinition = "TEXT")
    private String optionText;

    public QuestionOption() {}

    public QuestionOption(Question question, Character optionLabel, String optionText) {
        this.question = question;
        this.optionLabel = optionLabel;
        this.optionText = optionText;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public Character getOptionLabel() { return optionLabel; }
    public void setOptionLabel(Character optionLabel) { this.optionLabel = optionLabel; }
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    @Override
    public String toString() { return optionLabel + ": " + optionText; }
}