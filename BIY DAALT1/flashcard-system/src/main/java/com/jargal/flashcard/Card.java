package com.jargal.flashcard;

public class Card {
    private String question;
    private String answer;
    private int correctAnswers;
    private boolean answeredCorrectlyLastTime;
    private int attempts;
    private long lastAnswerTime;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.correctAnswers = 0;
        this.answeredCorrectlyLastTime = true;
        this.attempts = 0;
        this.lastAnswerTime = 0;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
    }

    public boolean isAnsweredCorrectlyLastTime() {
        return answeredCorrectlyLastTime;
    }

    public void setAnsweredCorrectlyLastTime(boolean answeredCorrectlyLastTime) {
        this.answeredCorrectlyLastTime = answeredCorrectlyLastTime;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public long getLastAnswerTime() {
        return lastAnswerTime;
    }

    public void setLastAnswerTime(long lastAnswerTime) {
        this.lastAnswerTime = lastAnswerTime;
    }
}