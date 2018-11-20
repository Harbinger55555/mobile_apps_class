package com.techexchange.mobileapps.assignment2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleQuestionFragment extends Fragment {
    private String selectedAnswer;
    private Question question;
    private OnSubmitPressedListener submitListener;

    // Container Activity must implement this interface
    public interface OnSubmitPressedListener {
        void onSubmitPressed();
    }

    public SingleQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_question, container, false);
        Bundle args = getArguments();
        if (args != null) {
            TextView questionField = rootView.findViewById(R.id.question_field);
            ArrayList<RadioButton> radioButtons = new ArrayList<>();
            ArrayList<String> answers = new ArrayList<>();
            selectedAnswer = "None";
            question = args.getParcelable("Question");

            questionField.setText(question.getQuestion());

            radioButtons.add(rootView.findViewById(R.id.radio1));
            radioButtons.add(rootView.findViewById(R.id.radio2));
            radioButtons.add(rootView.findViewById(R.id.radio3));
            radioButtons.add(rootView.findViewById(R.id.radio4));
            answers.add(question.getCorrectAnswer());
            answers.add(question.getWrongAnswers()[0]);
            answers.add(question.getWrongAnswers()[1]);
            answers.add(question.getWrongAnswers()[2]);

            Random rand = new Random();
            while(radioButtons.size() > 0) {
                RadioButton radioButton = radioButtons.remove(rand.nextInt(radioButtons.size()));
                radioButton.setText(answers.remove(rand.nextInt(answers.size())));
                radioButton.setOnClickListener(this::onRadioButtonClicked);
            }
            RadioGroup rg = rootView.findViewById(R.id.radio_group);
            if (question.getSelectedAnswer() != null) {
                for (int r = 0; r < rg.getChildCount(); ++r) {
                    RadioButton rb = (RadioButton) rg.getChildAt(r);
                    if (rb.getText().toString().equals(question.getSelectedAnswer()))
                        rb.setChecked(true);
                }
            }
        }
        Button submitButton = rootView.findViewById(R.id.submit_question);
        try {
            submitListener = (OnSubmitPressedListener) getContext();
        } catch (ClassCastException ex) {
            throw new ClassCastException(
                    "The Context did not implement OnSubmitPressedListener!");
        }
        submitButton.setOnClickListener(view -> {
            if (!selectedAnswer.equals("None")) {
                question.setState(Question.State.BLUE);
                question.setSelectedAnswer(selectedAnswer);
            }
            // Return back to main with updated state.
            submitListener.onSubmitPressed();
        });

        return rootView;
    }

    private void onRadioButtonClicked(View view) {
        // Is the button now checked?
        RadioButton radioButton = (RadioButton) view;
        boolean checked = radioButton.isChecked();
        if (checked) selectedAnswer = radioButton.getText().toString();
    }
}
