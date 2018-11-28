package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class Fireball {
    private final String TAG = "Fireball";
    private Bitmap fireballSprite;
    private RectF rectF;
    private final float SPEED;
    private boolean moving;
    private int ballDirection; // right, down, left, up = 1, 2, 3, 4
    private float X;
    private float Y;
    private float width;
    private float height;
    private float screenWidth;
    private float screenHeight;

    // These are used to check if the fireball is still moving.
    private float newX;
    private float newY;

    public Fireball(Bitmap fireballSprite, RectF rectF, int ballDirection) {
        this.fireballSprite = fireballSprite;
        this.rectF = rectF;
        this.width = rectF.width();
        this.height = rectF.height();
        this.screenWidth = width * 8;
        this.screenHeight = height * 10;
        this.SPEED = width/5;
        this.X = rectF.centerX();
        this.Y = rectF.centerY();
        this.ballDirection = ballDirection;
        this.newX = X;
        this.newY = Y;
        this.moving = true;
    }

    public boolean isMoving() {
        return moving;
    }

    public void continueMovement(Tank[] tanks, Bricks bricks) {
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
            checkCollisions(tanks, bricks);
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

    private void checkCollisions(Tank[] tanks, Bricks bricks) {
        if (boundaryCollision()) {
            moving = false;
        }
        if (bricks.brickWallCollisionFireball(X, Y)) moving = false;
        if (tankCollision(tanks)) moving = false;
    }

    private boolean boundaryCollision() {
        return (X > screenWidth || Y > screenHeight ||
                X < 0 || Y < 0);
    }

    private boolean tankCollision(Tank[] tanks) {
        for (Tank tank : tanks) {
            if (tank.getRectF().contains(X, Y)) {
                Log.d(TAG, "Tank hit!");
                return true;
            }
        }
        return false;
    }
}
