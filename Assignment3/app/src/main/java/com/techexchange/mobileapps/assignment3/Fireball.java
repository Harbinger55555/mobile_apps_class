package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class Fireball {
    private final String TAG = "Fireball";
    private Bitmap fireballSprite;
    private RectF rectF;
    private final float SPEED = 50;
    private boolean moving;
    private int ballDirection; // right, down, left, up = 1, 2, 3, 4
    private float X;
    private float Y;

    // These are used to check if the fireball is still moving.
    private float newX;
    private float newY;

    public Fireball(Bitmap fireballSprite, RectF rectF, int ballDirection) {
        Log.d(TAG, "Fireball initialized!");
        this.fireballSprite = fireballSprite;
        this.rectF = rectF;
        this.X = rectF.centerX();
        this.Y = rectF.centerY();
        this.ballDirection = ballDirection;
        this.newX = X;
        this.newY = Y;
        this.moving = true;
    }

    public void continueMovement() {
        if (moving) {
            switch (ballDirection) {
                case 1:
                    continueRight();
                    break;
                case 2:
                    continueDown();
                    break;
                case 3:
                    continueLeft();
                    break;
                case 4:
                    continueUp();
                    break;
                default:
                    break;
            }
        }
    }

    private void continueRight() {
        X += SPEED;
        rectF = new RectF(rectF.left + SPEED, rectF.top, rectF.right + SPEED, rectF.bottom);
    }

    private void continueDown() {
        Y += SPEED;
        rectF = new RectF(rectF.left, rectF.top + SPEED, rectF.right, rectF.bottom + SPEED);
    }

    private void continueLeft() {
        X -= SPEED;
        rectF = new RectF(rectF.left - SPEED, rectF.top, rectF.right - SPEED, rectF.bottom);
    }

    private void continueUp() {
        Y -= SPEED;
        rectF = new RectF(rectF.left, rectF.top - SPEED, rectF.right, rectF.bottom - SPEED);
    }

    public void drawOnCanvas(Canvas canvas) {
        canvas.drawBitmap(fireballSprite, null, rectF, null);
    }
}
