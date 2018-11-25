package com.techexchange.mobileapps.assignment3;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private final String TAG = "MainActivity";
    private GestureDetectorCompat mDetector;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        mDetector = new GestureDetectorCompat(this, this);
        setContentView(gameView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(TAG, "onFling called!");
        if (velocityX >= 0) {
            // 1st quadrant.
            if (velocityY <= 0) {
                if (Math.abs(velocityX) >= Math.abs(velocityY)) gameView.onFling(1);
                else gameView.onFling(4);
            } else {
                // 4th quadrant.
                if (Math.abs(velocityX) >= Math.abs(velocityY)) gameView.onFling(1);
                else gameView.onFling(2);
            }
        } else {
            // 2nd quadrant.
            if (velocityY <= 0) {
                if (Math.abs(velocityX) >= Math.abs(velocityY)) gameView.onFling(3);
                else gameView.onFling(4);
            } else {
                // 3rd quadrant.
                if (Math.abs(velocityX) >= Math.abs(velocityY)) gameView.onFling(3);
                else gameView.onFling(2);
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        float x = event.getX();
        float y = event.getY() - 280;
        gameView.onSingleTap(x, y);
        return true;
    }
}
