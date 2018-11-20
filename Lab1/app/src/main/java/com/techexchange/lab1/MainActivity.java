package com.techexchange.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        });

        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
        });
    }
}
