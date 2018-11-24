package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

public class Tank {
    private String TAG = "Tank";
    private Bitmap tankSprite;
    private RectF rectF;
    private float X;
    private float Y;
    private float width;
    private float height;
    private int gunDirection; // right, down, left, up = 1, 2, 3, 4
    private final float SPEED_X;
    private final float SPEED_Y;
    private boolean moving;
    private float screenWidth;
    private float screenHeight;

    // These are used to check if the tank is still moving.
    private float newX;
    private float newY;

    public Tank(Bitmap tankSprite, RectF rectF) {
        this.tankSprite = tankSprite;
        this.rectF = rectF;
        this.X = rectF.centerX();
        this.Y = rectF.centerY();
        this.width = rectF.width();
        this.height = rectF.height();
        this.screenWidth = width * 8;
        this.screenHeight = height * 10;
        this.gunDirection = 1;
        this.newX = X;
        this.newY = Y;
        this.moving = false;
        this.SPEED_X = width/10;
        this.SPEED_Y = height/10;
//        Log.d(TAG, "Width = " + width);
//        Log.d(TAG, "Height = " + height);
//        Log.d(TAG, "SPEED_X = " + SPEED_X);
//        Log.d(TAG, "SPEED_Y = " + SPEED_Y);
//        Log.d(TAG, "Tank top = " + rectF.top);
    }

    public void drawOnCanvas(Canvas canvas) {
//        Log.d(TAG, "drawOnCanvas Called!");
        canvas.drawBitmap(tankSprite, null, rectF, null);
    }

    // Clockwise rotation.
    private Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public void moveTank(float tapX, float tapY) {
        if (!moving) {
            moving = true;
            float angle = getAngle(tapX, tapY);
            //        Log.d(TAG, "angle = " + angle);
            //        Log.d(TAG, "TapX = " + tapX);
            //        Log.d(TAG, "TapY = " + tapY);
            //        Log.d(TAG, "tankX = " + X);
            //        Log.d(TAG, "tankY = " + Y);

            switch (getQuadrant(tapX, tapY)) {
                case 1:
                    if (angle >= 315) goRight();
                    else goUp();
                    break;
                case 2:
                    if (angle <= 225) goLeft();
                    else goUp();
                    break;
                case 3:
                    if (angle >= 135) goLeft();
                    else goDown();
                    break;
                case 4:
                    if (angle <= 45) goRight();
                    else goDown();
                    break;
                default:
                    break;
            }
        }
    }

    public void continueMovement() {
        if (moving) {
            switch (gunDirection) {
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

    private void goRight() {
        newX += width;
        turnGun(1);
        continueRight();
    }

    private void goDown() {
        newY += height;
        turnGun(2);
        continueDown();
    }

    private void goLeft() {
        newX -= width;
        turnGun(3);
        continueLeft();
    }

    private void goUp() {
        newY -= height;
        turnGun(4);
        continueUp();
    }

    private void continueRight() {
        X += SPEED_X;
        rectF = new RectF(rectF.left + SPEED_X, rectF.top, rectF.right + SPEED_X, rectF.bottom);
        if ((int) X >= (int) newX) moving = false;
    }

    private void continueDown() {
        Y += SPEED_Y;
        rectF = new RectF(rectF.left, rectF.top + SPEED_Y, rectF.right, rectF.bottom + SPEED_Y);
        if ((int) Y >= (int) newY) moving = false;
    }

    private void continueLeft() {
        X -= SPEED_X;
        rectF = new RectF(rectF.left - SPEED_X, rectF.top, rectF.right - SPEED_X, rectF.bottom);
        if ((int) X <= (int) newX) moving = false;
    }

    private void continueUp() {
        Y -= SPEED_Y;
        rectF = new RectF(rectF.left, rectF.top - SPEED_Y, rectF.right, rectF.bottom - SPEED_Y);
        if ((int) Y <= (int) newY) moving = false;
    }

    private void turnGun(int newDirection) {
        int diff = newDirection - gunDirection;
        tankSprite = rotate(tankSprite, diff * 90);
        gunDirection = newDirection;
    }

    private int getQuadrant(float tapX, float tapY) {
        if (tapX >= X) {
            return tapY <= Y ? 1 : 4;
        } else {
            return tapY <= Y ? 2 : 3;
        }
    }

    // Clockwise from positive X-axis.
    private float getAngle(float tapX, float tapY) {
        float angle = (float) Math.toDegrees(Math.atan2(tapY - Y, tapX - X));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}