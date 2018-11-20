package com.techexchange.mobileapps.lab9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {
    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList;
    private int currentQuestionIndex;
    private int currentScore;

    private static final String TAG = QuizFragment.class.getSimpleName();
    static final String KEY_SCORE = "Score";

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseApp app = FirebaseApp.initializeApp(getActivity());
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);
        database.goOnline();

        DatabaseReference ref = database.getReference();
        ref = ref.child("lab9").child("questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onQuestionListChanged(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The database transaction was cancelled",
                        databaseError.toException());
            }
        });

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(v -> onNextButtonPressed());

        correctText = rootView.findViewById(R.id.correct_incorrect_text);
        currentQuestionIndex = 0;
        currentScore = 0;
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("CurrentIndex");
            currentScore = savedInstanceState.getInt("Score");
        }
        Log.d(TAG, "onCreate() was called");

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", currentQuestionIndex);
        outState.putInt("Score", currentScore);
        Log.d(TAG, "onSaveInstanceState() was called");
    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
            getActivity().finish();
        }
        boolean restartQuiz = data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ, false);
        if (restartQuiz) {
            currentQuestionIndex = 0;
            currentScore = 0;
            updateView();
        }
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
            Intent scoreIntent = new Intent(QuizFragment.this.getActivity(), ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }
    }

    private void onQuestionListChanged(DataSnapshot dataSnapshot) {
        questionList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Question question = child.getValue(Question.class);
            questionList.add(question);
        }
        updateView();
    }
}
