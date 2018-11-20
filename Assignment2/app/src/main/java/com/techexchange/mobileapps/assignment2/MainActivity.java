package com.techexchange.mobileapps.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuestionFragmentAdapter.OnQuestionSelectedListener, SingleQuestionFragment.OnSubmitPressedListener, QuestionListFragment.OnEmailButtonPressed {
    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = QuestionListFactory.readCSV(getResources().openRawResource(R.raw.country_list));

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.fragment_container);

        if (frag == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("QuestionList", (ArrayList<? extends Parcelable>) questionList);
            frag = new QuestionListFragment();
            frag.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_container, frag).commit();
        }
    }

    @Override
    public void onQuestionSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = new SingleQuestionFragment();
        Question question = questionList.get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Question", question);
        frag.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }

    @Override
    public void onSubmitPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = new QuestionListFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("QuestionList", (ArrayList<? extends Parcelable>) questionList);
        frag.setArguments(bundle);
        fm.popBackStack();
    }

    @Override
    public void onEmailButtonPressed(int score) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Score Report");
        StringBuilder emailBody = new StringBuilder(questionList.size() * 50);
        emailBody.append("Summary: ").append(Integer.toString(score)).append(" out of ");
        emailBody.append(Integer.toString(questionList.size())).append("\n\nHere is the complete score report:\n\n");
        for (Question question : questionList) {
            emailBody.append(question.toString());
        }

        intent.putExtra(Intent.EXTRA_TEXT, emailBody.toString()); // Enter a string for the email body
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    "The activity could not be resolved.", Toast.LENGTH_SHORT).show();
        }
    }
}
