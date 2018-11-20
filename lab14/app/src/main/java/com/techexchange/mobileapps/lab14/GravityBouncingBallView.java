package com.techexchange.mobileapps.lab14;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class GravityBouncingBallView extends View {
    private static final String TAG = "BouncingBallView";
    private Ball ball;
    private static long SLEEP_MS = 30;
    private static final float TIME_STEP = SLEEP_MS / 1000.f;
    private static float GRAVITY = 500f;

    private int screenWidth;
    private int screenHeight;

    // Having this constructor is necessary.
    public GravityBouncingBallView(Context context) {
        super(context);
        ball = new Ball(100.f, 100.f, 200.f, 200.f, 500, 10);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ball.drawOnCanvas(canvas);

        update();

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException ex) {
            Log.e(TAG, "Sleep interrupted!", ex);
        }

        invalidate(); // Force a redraw.
    }

    private void update() {
        // Le physics simulation.
        float ballLeft = ball.getleft();
        float ballRight = ball.getright();
        float ballTop = ball.gettop();
        float ballBottom = ball.getbottom();
        float ballSpeedX = ball.getspeedX();
        float ballSpeedY = ball.getspeedY();

        ball.setleft(ballLeft + (ballSpeedX * TIME_STEP));
        ballLeft = ball.getleft();
        ball.setright(ballRight + (ballSpeedX * TIME_STEP));
        ballRight = ball.getright();
        ball.settop(ballTop + (ballSpeedY * TIME_STEP) + 0.5f * GRAVITY * (TIME_STEP * TIME_STEP));
        ballTop = ball.gettop();
        ball.setbottom(ballBottom + (ballSpeedY * TIME_STEP) + 0.5f * GRAVITY * (TIME_STEP * TIME_STEP));
        ballBottom = ball.getbottom();

        // Update speed with gravity.
        ball.setspeedY(ball.getspeedY() + (GRAVITY * TIME_STEP));

        // Ball reaches or overflows the right of the screen.
        if (ballRight > screenWidth) {
            ball.setspeedX(-ballSpeedX);
            float overflowWidth = ballRight - screenWidth;
            ball.setright(ballRight - (2 * overflowWidth));
            ball.setleft(ballLeft - (2 * overflowWidth));
        }

        // Ball reaches or overflows the bottom of the screen.
        if (ballBottom > screenHeight) {
            ball.setspeedY(-ballSpeedY);
            float overflowHeight = ballBottom - screenHeight;
            ball.setbottom(ballBottom - (2 * overflowHeight));
            ball.settop(ballTop - (2 * overflowHeight));
        }

        // Ball reaches or overflows the left of the screen.
        if (ballLeft < 0) {
            ball.setspeedX(-ballSpeedX);
            float overflowWidth = 0 - ballLeft;
            ball.setleft(ballLeft + (2 * overflowWidth));
            ball.setright(ballRight + (2 * overflowWidth));
        }

        // Ball reaches or overflows the top of the screen.
        if (ballTop < 0) {
            ball.setspeedY(-ballSpeedY);
            float overflowHeight = 0 - ballTop;
            ball.settop(ballTop + (2 * overflowHeight));
            ball.setbottom(ballBottom + (2 * overflowHeight));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
    }
}