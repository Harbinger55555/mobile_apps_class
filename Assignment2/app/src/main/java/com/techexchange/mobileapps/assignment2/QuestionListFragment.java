package com.techexchange.mobileapps.assignment2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionListFragment extends Fragment {
    private List<Question> questionList;
    private QuestionFragmentAdapter adapter;
    private Button sendEmail;
    private int score;
    private OnEmailButtonPressed emailListener;

    // Container Activity must implement this interface
    public interface OnEmailButtonPressed {
        void onEmailButtonPressed(int score);
    }

    public QuestionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_question_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        questionList = getArguments().getParcelableArrayList("QuestionList");
        score = 0;

        adapter = new QuestionFragmentAdapter(questionList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button finalSubmit = rootView.findViewById(R.id.final_submit);
        sendEmail = rootView.findViewById(R.id.send_email);
        sendEmail.setEnabled(false);
        finalSubmit.setOnClickListener(this::onFinalSubmitPressed);

        try {
            emailListener = (OnEmailButtonPressed) getContext();
        } catch (ClassCastException ex) {
            throw new ClassCastException(
                    "The Context did not implement OnQuestionAnsweredListener!");
        }
        sendEmail.setOnClickListener(view -> {
            // Return back to main with the score.
            emailListener.onEmailButtonPressed(score);
        });


        return rootView;
    }

    private void onFinalSubmitPressed(View view) {
        for (Question question : questionList) {
            if (question.getSelectedAnswer().equals(question.getCorrectAnswer())) {
                question.setState(Question.State.GREEN);
                score++;
            }else{
                question.setState(Question.State.RED);
            }
        }
        sendEmail.setEnabled(true);
        adapter.setClickableQuestions(false);
        adapter.notifyDataSetChanged();
    }
}
