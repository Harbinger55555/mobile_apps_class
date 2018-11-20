package com.techexchange.mobileapps.lab7;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_FILE_NAME = "SHARED_PREFS";
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE_KEY";
    static final String KEY_RESTART_QUIZ = "RetakeQuiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Button againButton = findViewById(R.id.again_button);
        againButton.setOnClickListener(v -> onAgainButtonPressed());

        Button emailButton = findViewById(R.id.email_button);
        emailButton.setOnClickListener(v -> onEmailButtonPressed());

        TextView scoreText = findViewById(R.id.score_text);
        int score = getIntent().getIntExtra(SingleQuestionFragment.KEY_SCORE, 0);
        scoreText.setText("Quiz Score: " + score);

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        int highScore = sp.getInt(HIGH_SCORE_KEY, /* defValue= */ -1);
        if (score > highScore) {
            sp.edit()
                    .putInt(HIGH_SCORE_KEY, score)
                    .commit();
            Toast.makeText( this, "Congratulations on a new high score! " + score,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onAgainButtonPressed() {
        Intent data = new Intent();
        data.putExtra(KEY_RESTART_QUIZ, true);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void onEmailButtonPressed() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Scores");
        intent.putExtra(Intent.EXTRA_TEXT, ""); // Enter a string for the email body
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    "The activity could not be resolved.", Toast.LENGTH_SHORT).show();
        }
    }
}
