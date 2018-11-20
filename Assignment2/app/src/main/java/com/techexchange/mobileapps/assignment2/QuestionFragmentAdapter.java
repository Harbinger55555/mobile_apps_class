package com.techexchange.mobileapps.assignment2;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class QuestionFragmentAdapter extends RecyclerView.Adapter<QuestionFragmentAdapter.QuestionViewHolder> {
    // Container Activity must implement this interface
    public interface OnQuestionSelectedListener {
        void onQuestionSelected(int position);
    }

    private List<Question> questionList;
    private Context context;
    private OnQuestionSelectedListener questionListener;
    private boolean clickableQuestions;

    public void setClickableQuestions(boolean clickableQuestions) {
        this.clickableQuestions = clickableQuestions;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout questionContainer;
        public TextView questionNum;
        public TextView question;
        public QuestionViewHolder(LinearLayout v) {
            super(v);
            questionContainer = v;
            questionNum = v.findViewById(R.id.question_num);
            question = v.findViewById(R.id.question);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionFragmentAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
        this.clickableQuestions = true;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QuestionFragmentAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_view, parent, false);
        QuestionViewHolder vh = new QuestionViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.questionNum.setText("Question #" + (position + 1));
        holder.question.setText(questionList.get(position).getQuestion());
        try {
            questionListener = (OnQuestionSelectedListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(
                    "The Context did not implement OnQuestionAnsweredListener!");
        }
        holder.questionContainer.setOnClickListener(view -> {
            // Return back to main with the question index
            questionListener.onQuestionSelected(position);
        });
        switch(questionList.get(position).getState()) {
            case WHITE:
                ((GradientDrawable) holder.questionContainer.getBackground()).setColor(Color.WHITE);
                break;
            case BLUE:
                ((GradientDrawable) holder.questionContainer.getBackground()).setColor(Color.parseColor("#33ccff"));
                break;
            case RED:
                ((GradientDrawable) holder.questionContainer.getBackground()).setColor(Color.parseColor("#ff5050"));
                break;
            case GREEN:
                ((GradientDrawable) holder.questionContainer.getBackground()).setColor(Color.parseColor("#99cc00"));
                break;
        }
        if (!clickableQuestions)
            holder.questionContainer.setEnabled(false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
