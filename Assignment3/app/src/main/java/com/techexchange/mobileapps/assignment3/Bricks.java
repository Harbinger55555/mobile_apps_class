package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class Bricks {
    private final String TAG = "Bricks";
    private Bitmap damagedSprite;
    private Brick[] bricks;

    public Bricks(float screenWidth, float screenHeight, Bitmap defaultSprite, Bitmap damagedSprite) {
        this.damagedSprite = damagedSprite;
        float brickWidth = screenWidth/8;
        float brickHeight = screenHeight/10;
        this.bricks = new Brick[]{
                new Brick(defaultSprite, new RectF(brickWidth, 0, brickWidth * 2, brickHeight)),
                new Brick(defaultSprite, new RectF(brickWidth * 5, 0, brickWidth * 6, brickHeight)),
                new Brick(defaultSprite, new RectF(brickWidth * 6, 0, brickWidth * 7, brickHeight)),
                new Brick(defaultSprite, new RectF(brickWidth, brickHeight, brickWidth * 2, brickHeight * 2)),
                new Brick(defaultSprite, new RectF(brickWidth, brickHeight * 2, brickWidth * 2, brickHeight * 3)),
                new Brick(defaultSprite, new RectF(brickWidth, brickHeight * 3, brickWidth * 2, brickHeight * 4)),
                new Brick(defaultSprite, new RectF(brickWidth * 2, brickHeight * 3, brickWidth * 3, brickHeight * 4)),
                new Brick(defaultSprite, new RectF(brickWidth * 3, brickHeight * 3, brickWidth * 4, brickHeight * 4)),
                new Brick(defaultSprite, new RectF(brickWidth * 3, brickHeight * 3, brickWidth * 4, brickHeight * 4)),
                new Brick(defaultSprite, new RectF(brickWidth * 4, brickHeight * 6, brickWidth * 5, brickHeight * 7)),
                new Brick(defaultSprite, new RectF(brickWidth * 2, brickHeight * 7, brickWidth * 3, brickHeight * 8)),
                new Brick(defaultSprite, new RectF(brickWidth * 3, brickHeight * 7, brickWidth * 4, brickHeight * 8)),
                new Brick(defaultSprite, new RectF(brickWidth * 4, brickHeight * 7, brickWidth * 5, brickHeight * 8)),
                new Brick(defaultSprite, new RectF(brickWidth * 5, brickHeight * 7, brickWidth * 6, brickHeight * 8)),
                new Brick(defaultSprite, new RectF(brickWidth * 5, brickHeight * 8, brickWidth * 6, brickHeight * 9)),
                new Brick(defaultSprite, new RectF(brickWidth * 5, brickHeight * 8, brickWidth * 6, brickHeight * 9)),
                new Brick(defaultSprite, new RectF(brickWidth * 5, brickHeight * 9, brickWidth * 6, screenHeight))
        };
    }

    public void drawOnCanvas(Canvas canvas) {
        for (Brick brick : bricks) brick.drawOnCanvas(canvas);
    }

    public boolean brickWallCollision(float feintNewX, float feintNewY) {
        for (Brick brick : bricks) {
            RectF rectF = brick.getRectF();
            if (rectF.contains(feintNewX, feintNewY)) return true;
        }
        return false;
    }
}
