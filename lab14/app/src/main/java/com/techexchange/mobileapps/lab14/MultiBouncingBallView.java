package com.techexchange.mobileapps.lab14;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class MultiBouncingBallView extends View {
    private static final String TAG = "MultiBouncingBallView";
    private Balls balls;
    private static long SLEEP_MS = 30;
    private static final float TIME_STEP = SLEEP_MS / 1000.f;
    private static int NUM_BALLS = 20;

    private int screenWidth;
    private int screenHeight;

    // Having this constructor is necessary.
    public MultiBouncingBallView(Context context) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        balls = new Balls(NUM_BALLS, screenWidth, screenHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        balls.drawOnCanvas(canvas);

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
        for (Ball ball : balls.getBalls()) {
            calcWallCollision(ball);
            ball.updateCoords();
            calcBallCollision(ball);
            ball.updateCoords();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
    }

    private void calcWallCollision(Ball ball) {
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
        ball.settop(ballTop + (ballSpeedY * TIME_STEP));
        ballTop = ball.gettop();
        ball.setbottom(ballBottom + (ballSpeedY * TIME_STEP));
        ballBottom = ball.getbottom();

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

    // If balls collide, update both balls distance and velocity.
    private void calcBallCollision(Ball ball) {
        float ballLeft = ball.getleft();
        float ballRight = ball.getright();
        float ballTop = ball.gettop();
        float ballBottom = ball.getbottom();
        float ballSpeedX = ball.getspeedX();
        float ballSpeedY = ball.getspeedY();
        float ballRadius = ball.getRadius();

        ball.setleft(ballLeft + (ballSpeedX * TIME_STEP));
        ballLeft = ball.getleft();
        ball.setright(ballRight + (ballSpeedX * TIME_STEP));
        ballRight = ball.getright();
        ball.settop(ballTop + (ballSpeedY * TIME_STEP));
        ballTop = ball.gettop();
        ball.setbottom(ballBottom + (ballSpeedY * TIME_STEP));
        ballBottom = ball.getbottom();

        for (Ball anotherBall : balls.getBalls()) {
            if (ball == anotherBall) continue;
            float anotherBallLeft = anotherBall.getleft();
            float anotherBallRight = anotherBall.getright();
            float anotherBallTop = anotherBall.gettop();
            float anotherBallBottom = anotherBall.getbottom();
            float anotherBallSpeedX = anotherBall.getspeedX();
            float anotherBallSpeedY = anotherBall.getspeedY();
            float anotherBallRadius = anotherBall.getRadius();

            float euclideanDistance = (float) Math.sqrt(
                    Math.pow((anotherBall.getCoordX() - ball.getCoordX()), 2) +
                            Math.pow((anotherBall.getCoordY() - ball.getCoordY()), 2));

            float ballRadiusSum = ballRadius + anotherBallRadius;

            if (euclideanDistance < ballRadiusSum) {
                float overflow = ballRadiusSum - euclideanDistance;
                ball.setspeedX(anotherBallSpeedX);
                ball.setspeedY(anotherBallSpeedY);
                anotherBall.setspeedX(ballSpeedX);
                anotherBall.setspeedY(ballSpeedY);

                if (ballLeft < anotherBallLeft) {
                    ball.setleft(ballLeft - overflow);
                    ball.setright(ballRight - overflow);
                    anotherBall.setleft(anotherBallLeft + overflow);
                    anotherBall.setright(anotherBallRight + overflow);

                } else {
                    ball.setleft(ballLeft + overflow);
                    ball.setright(ballRight + overflow);
                    anotherBall.setleft(anotherBallLeft - overflow);
                    anotherBall.setright(anotherBallRight - overflow);
                }

                if (ballTop < anotherBallTop) {
                    ball.settop(ballTop - overflow);
                    ball.setbottom(ballBottom - overflow);
                    anotherBall.settop(anotherBallTop + overflow);
                    anotherBall.setbottom(anotherBallBottom + overflow);
                } else {
                    ball.settop(ballTop + overflow);
                    ball.setbottom(ballBottom + overflow);
                    anotherBall.settop(anotherBallTop - overflow);
                    anotherBall.setbottom(anotherBallBottom - overflow);
                }

                ballSpeedX = anotherBallSpeedX;
                ballSpeedY = anotherBallSpeedY;
                ballLeft = ball.getleft();
                ballRight = ball.getright();
                ballTop = ball.gettop();
                ballBottom = ball.getbottom();
            }
        }
    }
}