package com.techexchange.mobileapps.lab14;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

class Balls {
    private Ball[] balls;

    public Balls(int numBalls, float screenWidth, float screenHeight) {
        this.balls = new Ball[numBalls];
        createBalls(numBalls, screenWidth, screenHeight);
    }

    public Ball[] getBalls() {
        return balls;
    }

    private void createBalls(int numBalls, float screenWidth, float screenHeight) {
        Random r = new Random();
        for (int i = 0; i < balls.length; ++i) {
            float X = i % 5 * 100 + 5 * i;
            float Y = i / 5 * 100 + 5 * i;
            balls[i] = new Ball(X, Y, X + 100, Y + 100, r.nextInt(500), r.nextInt(30));
        }
    }

    public void drawOnCanvas(Canvas canvas) {
        for (Ball ball : balls) canvas.drawOval(
                ball.getleft(), ball.gettop(), ball.getright(),ball.getbottom(),
                ball.getcolor());
    }
}
