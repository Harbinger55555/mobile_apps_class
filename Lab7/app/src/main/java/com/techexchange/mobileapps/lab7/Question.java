package com.techexchange.mobileapps.lab7;

class Question {
    final private String question;
    final private String correctAnswer;
    final private String wrongAnswer;
    private String selectedAnswer;

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Question(String question, String correctAnswer, String wrongAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;

        this.wrongAnswer = wrongAnswer;
        this.selectedAnswer = null;
    }
}
