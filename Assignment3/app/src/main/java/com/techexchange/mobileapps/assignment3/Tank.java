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
    private Bitmap fireballSprite;
    private Fireball fireball;

    // These are used to check if the tank is still moving.
    private float newX;
    private float newY;

    public Tank(Bitmap tankSprite, RectF rectF, Bitmap fireballSprite) {
        this.tankSprite = tankSprite;
        this.fireballSprite = fireballSprite;
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
    }

    public RectF getRectF() {
        return rectF;
    }

    public Fireball getFireball() {
        return fireball;
    }

    public void setFireball(Fireball fireball) {
        this.fireball = fireball;
    }

    public void drawOnCanvas(Canvas canvas) {
        canvas.drawBitmap(tankSprite, null, rectF, null);
    }

    // Clockwise rotation.
    private Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public void moveTank(float tapX, float tapY, Tank[] tanks, Bricks bricks) {
        if (!moving) {
            float angle = getAngle(tapX, tapY);

            switch (getQuadrant(tapX, tapY)) {
                case 1:
                    if (angle >= 315) goRight(tanks, bricks);
                    else goUp(tanks, bricks);
                    break;
                case 2:
                    if (angle <= 225) goLeft(tanks, bricks);
                    else goUp(tanks, bricks);
                    break;
                case 3:
                    if (angle >= 135) goLeft(tanks, bricks);
                    else goDown(tanks, bricks);
                    break;
                case 4:
                    if (angle <= 45) goRight(tanks, bricks);
                    else goDown(tanks, bricks);
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

    private void goRight(Tank[] tanks, Bricks bricks) {
        turnGun(1);
        if (!hasCollisions(X + width, Y, tanks, bricks)) {
            newX += width;
            moving = true;
            continueRight();
        }
    }

    private void goDown(Tank[] tanks, Bricks bricks) {
        turnGun(2);
        if (!hasCollisions(X,Y + height, tanks, bricks)) {
            newY += height;
            moving = true;
            continueDown();
        }
    }

    private void goLeft(Tank[] tanks, Bricks bricks) {
        turnGun(3);
        if (!hasCollisions(X - width, Y, tanks, bricks)) {
            newX -= width;
            moving = true;
            continueLeft();
        }
    }

    private void goUp(Tank[] tanks, Bricks bricks) {
        turnGun(4);
        if (!hasCollisions(X, Y - height, tanks, bricks)) {
            newY -= height;
            moving = true;
            continueUp();
        }
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

    private boolean boundaryCollision(float feintNewX, float feintNewY) {
        return (feintNewX > screenWidth || feintNewY > screenHeight ||
                feintNewX < 0 || feintNewY < 0);
    }

    private boolean tankCollision(Tank[] tanks, float feintNewX, float feintNewY) {
        for (Tank tank : tanks) {
            if (this == tank) continue;
            if (tank.getRectF().contains(feintNewX, feintNewY)) return true;
        }
        return false;
    }

    private boolean hasCollisions(float feintNewX, float feintNewY, Tank[] tanks, Bricks bricks) {
        return (boundaryCollision(feintNewX, feintNewY) ||
                bricks.brickWallCollision(feintNewX, feintNewY) ||
                tankCollision(tanks, feintNewX, feintNewY));
    }

    public void shootFireball(int direction) {
        RectF fireballRectF;
        if (fireball == null) {
            switch (direction) {
                case 1:
                    fireballRectF = new RectF(rectF.right, rectF.top, rectF.right + width, rectF.bottom);
                    break;
                case 2:
                    fireballRectF = new RectF(rectF.left, rectF.bottom, rectF.right, rectF.bottom + height);
                    break;
                case 3:
                    fireballRectF = new RectF(rectF.left - width, rectF.top, rectF.left, rectF.bottom);
                    break;
                case 4:
                    fireballRectF = new RectF(rectF.left, rectF.top - height, rectF.right, rectF.top);
                    break;
                default:
                    fireballRectF = new RectF();
                    break;
            }
            fireball = new Fireball(fireballSprite, fireballRectF, direction);
        }
    }
}
