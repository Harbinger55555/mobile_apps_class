package com.techexchange.mobileapps.lab14;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

class Ball {
    private float left;
    private float top;
    private float right;
    private float bottom;
    private Paint color = new Paint();
    private float speedX;
    private float speedY;
    private float radius;
    private float coordX;
    private float coordY;

    public Ball(float left, float top, float right, float bottom, float speedX, float speedY) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.speedX = speedX;
        this.speedY = speedY;
        this.radius = (right - left) / 2;
        updateCoords();
        this.color.setColor(randomizeColor());
    }

    public float getleft() {
        return left;
    }

    public void setleft(float left) {
        this.left = left;
    }

    public float gettop() {
        return top;
    }

    public void settop(float top) {
        this.top = top;
    }

    public float getright() {
        return right;
    }

    public void setright(float right) {
        this.right = right;
    }

    public float getbottom() {
        return bottom;
    }

    public void setbottom(float bottom) {
        this.bottom = bottom;
    }

    public Paint getcolor() {
        return color;
    }

    public float getspeedX() {
        return speedX;
    }

    public void setspeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getspeedY() {
        return speedY;
    }

    public void setspeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getRadius() {
        return radius;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void updateCoords() {
        this.coordX = (right + left) / 2;
        this.coordY = (top + bottom) / 2;
    }

    public void drawOnCanvas(Canvas canvas) {
        canvas.drawOval(left, top, right, bottom, color);
    }

    private int randomizeColor() {
        Random r = new Random();
        return Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }
}
