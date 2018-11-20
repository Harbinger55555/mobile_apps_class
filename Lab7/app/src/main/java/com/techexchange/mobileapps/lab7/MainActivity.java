package com.techexchange.mobileapps.lab7;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Preconditions;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.techexchange.mobileapps.lab7.SingleQuestionFragment.KEY_SCORE;

public class MainActivity extends AppCompatActivity implements SingleQuestionFragment.OnQuestionAnsweredListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private int currentScore;
    private FragmentManager fm;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        currentScore = savedInstanceState != null ? savedInstanceState.getInt("Score") : 0;

        viewPager = findViewById(R.id.contact_pager);
        viewPager.setAdapter(new QuestionFragmentAdapter(fm));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Score", currentScore);
    }

    private final class QuestionFragmentAdapter
            extends FragmentStatePagerAdapter {
        private List<Question> questionList;

        public QuestionFragmentAdapter(FragmentManager fm) {
            super(fm);
            questionList = initQuestionList();
        }

        @Override
        public Fragment getItem(int position) {
            Question question = questionList.get(position);
            return SingleQuestionFragment.createFragmentFromQuestion(question, position);
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    @SuppressLint("RestrictedAPI")
    private List<com.techexchange.mobileapps.lab7.Question> initQuestionList() {
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] correctAnswers = res.getStringArray(R.array.correct_answers);
        String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

        // Make sure that all arrays have the same length.
        Preconditions.checkState(questions.length == correctAnswers.length);
        Preconditions.checkState(questions.length == wrongAnswers.length);

        List<com.techexchange.mobileapps.lab7.Question> qList = new ArrayList<>();

        for (int i = 0; i < questions.length; ++i) {
            qList.add(new com.techexchange.mobileapps.lab7.Question(questions[i], correctAnswers[i], wrongAnswers[i]));
        }
        return qList;
    }

    @Override
    public void onQuestionAnswered(String selectedAnswer, int questionId) {
        Log.d(TAG, "The " + selectedAnswer + " button was pressed!");
        currentScore = selectedAnswer.equals("Correct!") ? currentScore + 1 : currentScore;
        if (questionId == getResources().getStringArray(R.array.questions).length - 1) {
            Intent scoreIntent = new Intent(this, ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }
    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
            finish();
        }
        boolean restartQuiz = data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ, false);
        if (restartQuiz) {
            currentScore = 0;
            viewPager.setAdapter(new QuestionFragmentAdapter(fm));
        }
    }
}
