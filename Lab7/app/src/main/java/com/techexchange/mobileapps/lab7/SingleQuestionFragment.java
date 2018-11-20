package com.techexchange.mobileapps.lab7;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SingleQuestionFragment extends Fragment {
    interface OnQuestionAnsweredListener {
        void onQuestionAnswered(String selectedAnswer, int questionId);
    }

    private static final String ARG_QUESTION_TEXT = "ARG_QUESTION_TEXT";
    private static final String ARG_CORRECT_ANSWER = "ARG_CORRECT_ANSWER";
    private static final String ARG_WRONG_ANSWER = "ARG_WRONG_ANSWER";
    private static final String ARG_QUESTION_INDEX = "ARG_QUESTION_INDEX";
    private static final String ARG_SELECTED_ANSWER = "ARG_SELECTED_ANSWER";
    private static final String ARG_CORRECT_TEXT = "ARG_CORRECT_TEXT";
    private static final String ARG_LEFT_BUTTON_TEXT = "ARG_LEFT_BUTTON_TEXT";
    private static final String ARG_RIGHT_BUTTON_TEXT = "ARG_RIGHT_BUTTON_TEXT";
    private static final String ARG_LEFT_BUTTON_ENABLED = "ARG_LEFT_BUTTON_ENABLED";
    private static final String ARG_RIGHT_BUTTON_ENABLED = "ARG_RIGHT_BUTTON_ENABLED";
    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private OnQuestionAnsweredListener answerListener;

    private int questionIndex;
    private int currentScore;

    static final String KEY_SCORE = "Score";

    static SingleQuestionFragment createFragmentFromQuestion(
            Question question, int questionIndex) {

        Bundle fragArgs = new Bundle();
        fragArgs.putString(ARG_QUESTION_TEXT, question.getQuestion());
        fragArgs.putString(ARG_CORRECT_ANSWER, question.getCorrectAnswer());
        fragArgs.putString(ARG_WRONG_ANSWER, question.getWrongAnswer());
        fragArgs.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragArgs.putString(ARG_SELECTED_ANSWER, question.getSelectedAnswer());

        SingleQuestionFragment frag = new SingleQuestionFragment();
        frag.setArguments(fragArgs);
        return frag;
    }

    public SingleQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", questionIndex);
        outState.putInt("Score", currentScore);
        outState.putString(ARG_QUESTION_TEXT, questionText.getText().toString());
        outState.putString(ARG_CORRECT_TEXT, correctText.getText().toString());
        outState.putString(ARG_LEFT_BUTTON_TEXT, leftButton.getText().toString());
        outState.putString(ARG_RIGHT_BUTTON_TEXT, rightButton.getText().toString());
        outState.putBoolean(ARG_LEFT_BUTTON_ENABLED, leftButton.isEnabled());
        outState.putBoolean(ARG_RIGHT_BUTTON_ENABLED, rightButton.isEnabled());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_question, container, false);
        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        correctText = rootView.findViewById(R.id.correct_incorrect_text);
        Bundle args = getArguments();
        if (args != null) {
            questionText.setText(args.getString(ARG_QUESTION_TEXT));
            if (Math.random() < 0.5) {
                leftButton.setText(args.getString(ARG_CORRECT_ANSWER));
                rightButton.setText(args.getString(ARG_WRONG_ANSWER));
            } else {
                rightButton.setText(args.getString(ARG_CORRECT_ANSWER));
                leftButton.setText(args.getString(ARG_WRONG_ANSWER));
            }
            correctText.setText(R.string.initial_correct_incorrect);
            questionIndex = args.getInt(ARG_QUESTION_INDEX);
            rightButton.setEnabled(true);
            leftButton.setEnabled(true);
        }
        if (savedInstanceState != null) {
            questionText.setText(savedInstanceState.getString(ARG_QUESTION_TEXT));
            leftButton.setText(savedInstanceState.getString(ARG_LEFT_BUTTON_TEXT));
            rightButton.setText(savedInstanceState.getString(ARG_RIGHT_BUTTON_TEXT));
            correctText.setText(savedInstanceState.getString(ARG_CORRECT_TEXT));
            leftButton.setEnabled(savedInstanceState.getBoolean(ARG_LEFT_BUTTON_ENABLED));
            rightButton.setEnabled(savedInstanceState.getBoolean(ARG_RIGHT_BUTTON_ENABLED));
        }

        return rootView;
    }

    private void onAnswerButtonPressed(View v) {
        Button selectedButton = (Button) v;
        Bundle args = getArguments();
        if (args.getString(ARG_CORRECT_ANSWER).contentEquals(selectedButton.getText())) {
            correctText.setText("Correct!");
            currentScore++;
        } else {
            correctText.setText("Wrong!");
        }
        answerListener.onQuestionAnswered(correctText.getText().toString(), questionIndex);
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            answerListener = (OnQuestionAnsweredListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(
                    "The Context did not implement OnQuestionAnsweredListener!");
        }
    }
}
