package com.techexchange.mobileapps.lab2;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.util.Preconditions;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList;
    private int currentQuestionIndex;
    private int currentScore;

    private static final String TAG = MainActivity.class.getSimpleName();
    static final String KEY_SCORE = "Score";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", currentQuestionIndex);
        outState.putInt("Score", currentScore);
        Log.d(TAG, "onSaveInstanceState() was called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = initQuestionList();
        questionText = findViewById(R.id.question_text);
        leftButton = findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        nextButton = findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(v -> onNextButtonPressed());

        correctText = findViewById(R.id.correct_incorrect_text);
        currentQuestionIndex = 0;
        currentScore = 0;
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("CurrentIndex");
            currentScore = savedInstanceState.getInt("Score");
        }
        Log.d(TAG, "onCreate() was called");
    }

    @SuppressLint("RestrictedAPI")
    private List<Question> initQuestionList() {
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] correctAnswers = res.getStringArray(R.array.correct_answers);
        String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

        // Make sure that all arrays have the same length.
        Preconditions.checkState(questions.length == correctAnswers.length);
        Preconditions.checkState(questions.length == wrongAnswers.length);

        List<Question> qList = new ArrayList<>();

        for (int i = 0; i < questions.length; ++i) {
            qList.add(new Question(questions[i], correctAnswers[i], wrongAnswers[i]));
        }
        return qList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
        Log.d(TAG, "onResume() was called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() was called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() was called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() was called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() was called");
    }

    private void updateView() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());
        if (Math.random() < 0.5) {
            leftButton.setText(currentQuestion.getCorrectAnswer());
            rightButton.setText(currentQuestion.getWrongAnswer());
        } else {
            rightButton.setText(currentQuestion.getCorrectAnswer());
            leftButton.setText(currentQuestion.getWrongAnswer());
        }
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
        nextButton.setEnabled(false);
        correctText.setText(R.string.initial_correct_incorrect);
    }

    private void onAnswerButtonPressed(View v) {
        Button selectedButton = (Button) v;
        Question ques = questionList.get(currentQuestionIndex);
        if (ques.getCorrectAnswer().contentEquals(selectedButton.getText())) {
            correctText.setText("Correct!");
            currentScore++;
        } else {
            correctText.setText("Wrong!");
        }
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private void onNextButtonPressed() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            updateView();
        } else {
            Intent scoreIntent = new Intent(this, ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
            finish();
        }
        boolean restartQuiz = data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ, false);
        if (restartQuiz) {
            currentQuestionIndex = 0;
            currentScore = 0;
            updateView();
        }
    }
}
