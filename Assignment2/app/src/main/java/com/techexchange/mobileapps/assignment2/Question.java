package com.techexchange.mobileapps.assignment2;

import android.os.Parcel;
import android.os.Parcelable;

class Question implements Parcelable{
    final private String question;
    final private String correctAnswer;
    final private String[] wrongAnswers;
    private State state;
    private String selectedAnswer;

    public enum State {
        WHITE, BLUE, RED, GREEN;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }

    public State getState() {
        return state;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Question(String question, String correctAnswer, String[] wrongAnswers, State state) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
        this.state = state;
        this.selectedAnswer = "None";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.question,
                this.correctAnswer,
                this.wrongAnswers[0],
                this.wrongAnswers[1],
                this.wrongAnswers[2],
                this.state.name(),
                this.selectedAnswer});
    }

    // Parcelling part
    public Question(Parcel in){
        String[] data = new String[7];
        String[] wrongAnswers = new String[3];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.question = data[0];
        this.correctAnswer = data[1];
        wrongAnswers[0] = data[2];
        wrongAnswers[1] = data[3];
        wrongAnswers[2] = data[4];
        this.wrongAnswers = wrongAnswers;
        this.state = State.valueOf(data[5]);
        this.selectedAnswer = data[6];
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(50);
        res.append("Question: ").append(question).append("\n");
        res.append("Your answer: ").append(selectedAnswer).append("\n");
        res.append("Correct Answer = ").append(correctAnswer).append("\n").append("Points: ");
        if (selectedAnswer.equals(correctAnswer)) {
            res.append("1\n\n");
        } else {
            res.append("0\n\n");
        }
        return res.toString();
    }
}
