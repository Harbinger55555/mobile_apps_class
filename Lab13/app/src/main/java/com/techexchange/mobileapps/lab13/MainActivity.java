package com.techexchange.mobileapps.lab13;

import android.animation.ObjectAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout rootView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private List<TextView > textViews;
    private View shapeView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootLayout);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textViews = Arrays.asList(textView1, textView2, textView3);
        shapeView = findViewById(R.id.shapeView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this::onClickAnimate);
    }

    private void onClickAnimate(View view) {
        int duration = 3000;
        // Animate the shape width, top, bottom, and color.
        float scaleToFillWidth = (float) rootView.getWidth() / shapeView.getWidth();

        ObjectAnimator shape2ScaleXAnimator =
                ObjectAnimator.ofFloat(shapeView, "scaleX", 1f, scaleToFillWidth).setDuration(duration);
        shape2ScaleXAnimator.start();

        // Set the top to be the top of the first TextView.
        ObjectAnimator shapeTopAnimator =
                ObjectAnimator.ofInt(shapeView, "top", shapeView.getTop(), textView1.getTop()).setDuration(duration);
        shapeTopAnimator.start();

        // Set the bottom to be the bottom of the third TextView.
        ObjectAnimator shapeBottomAnimator =
                ObjectAnimator.ofInt(shapeView, "bottom", shapeView.getBottom(), textView3.getBottom()).setDuration(duration);
        shapeBottomAnimator.start();

        int startColor = ContextCompat.getColor(MainActivity.this, R.color.shapeStartColor);
        int endColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
        ObjectAnimator shape2ColorAnimator =
                ObjectAnimator.ofArgb(shapeView, "backgroundColor", startColor, endColor).setDuration(duration);
        shape2ColorAnimator.start();

        // Loop through the TextViews, set their alpha and X position (as a random offset).
        for (View v : textViews) {
            ObjectAnimator alphaAnimator =
                    ObjectAnimator.ofFloat(v, "alpha", 0f, 1f).setDuration(duration);
            alphaAnimator.start();


            // Get a random value between negative one-half-screen-width, and positive one-half-screen-width.
            // Set that as the x position of the TextView.
            float randomOffset = (float) (Math.random() * rootView.getWidth()) - rootView.getWidth() / 2;
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + randomOffset).setDuration(duration);
            xAnimator.start();


            // Animate Button's Y position, rotation, and alpha.
            // Set button y position to be a random offset from the bottom of the screen.
            ObjectAnimator buttonYAnimator =
                    ObjectAnimator.ofFloat(button, "y", button.getY(), rootView.getBottom() + randomOffset).setDuration(duration);
            buttonYAnimator.start();


            float randomRotation = (float) (Math.random() * 40) - 20;
            ObjectAnimator rotationAnimator =
                    ObjectAnimator.ofFloat(button, "rotation", 0f, randomRotation).setDuration(duration);
            rotationAnimator.start();

            alphaAnimator =
                    ObjectAnimator.ofFloat(button, "alpha", 1f, 0f).setDuration(duration);
            alphaAnimator.start();
        }
    }

}
