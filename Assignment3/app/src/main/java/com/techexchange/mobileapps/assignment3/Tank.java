package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class Tank {
    private String TAG = "Tank";
    private Bitmap tankSprite;
    private Rect rect;
    private int X;
    private int Y;
    private int width;
    private int height;

    public Tank(Bitmap tankSprite, Rect rect) {
        this.tankSprite = tankSprite;
        this.rect = rect;
        this.X = rect.centerX();
        this.Y = rect.centerY();
        this.width = rect.width();
        this.height = rect.height();
    }

    public void drawOnCanvas(Canvas canvas) {
//        Log.d(TAG, "drawOnCanvas Called!");
        canvas.drawBitmap(tankSprite, null, rect, null);
    }

    // Clockwise rotation.
    private Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public void moveTank(int tapX, int tapY) {
        float angle = getAngle(tapX, tapY);
        Log.d(TAG, "angle = " + angle);
        Log.d(TAG, "TapX = " + tapX);
        Log.d(TAG, "TapY = " + tapY);
        Log.d(TAG, "tankX = " + X);
        Log.d(TAG, "tankY = " + Y);

        switch(getQuadrant(tapX, tapY)) {
            case 1:
                Log.d(TAG, "Quadrant 1 entered");
                if (angle >= 315) goRight();
                else goUp();
                break;
            case 2:
                Log.d(TAG, "Quadrant 2 entered");
                if (angle <= 225) goLeft();
                else goUp();
                break;
            case 3:
                Log.d(TAG, "Quadrant 3 entered");
                if (angle >= 135) goLeft();
                else goDown();
                break;
            case 4:
                Log.d(TAG, "Quadrant 4 entered");
                if (angle <= 45) goRight();
                else goDown();
                break;
            default:
                break;
        }
    }

    private void goRight() {
        X += width;
        rect = new Rect(rect.left + width, rect.top, rect.right + width, rect.bottom);
    }

    private void goDown() {
        Y += height;
        rect = new Rect(rect.left, rect.top + height, rect.right, rect.bottom + height);
    }

    private void goLeft() {
        X -= width;
        rect = new Rect(rect.left - width, rect.top, rect.right - width, rect.bottom);
    }

    private void goUp() {
        Y -= height;
        rect = new Rect(rect.left, rect.top - height, rect.right, rect.bottom - height);
    }

    private int getQuadrant(int tapX, int tapY) {
        if (tapX >= X) {
            return tapY <= Y ? 1 : 4;
        } else {
            return tapY <= Y ? 2 : 3;
        }
    }

    // Clockwise from positive X-axis.
    private float getAngle(int tapX, int tapY) {
        float angle = (float) Math.toDegrees(Math.atan2(tapY - Y, tapX - X));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}
