package com.techexchange.mobileapps.lab2;

class Question {
    final private String question;
    final private String correctAnswer;
    final private String wrongAnswer;

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public Question(String question, String correctAnswer, String wrongAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;

        this.wrongAnswer = wrongAnswer;
    }
}
