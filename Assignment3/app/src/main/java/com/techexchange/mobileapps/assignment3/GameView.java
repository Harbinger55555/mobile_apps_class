package com.techexchange.mobileapps.assignment3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class GameView extends View {
    private static final String TAG = "GameView";
    private static long SLEEP_MS = 30;
    private static final float TIME_STEP = SLEEP_MS / 1000.f;
    private Bricks bricks;
    private Tank tank1;
    private Tank tank2;
    private Tank[] tanks;

    private float screenWidth;
    private float screenHeight;

    public GameView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bricks.drawOnCanvas(canvas);
        tank1.drawOnCanvas(canvas);
        tank2.drawOnCanvas(canvas);
        if (tank1.getFireball() != null) tank1.getFireball().drawOnCanvas(canvas);

        update();

        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException ex) {
            Log.e(TAG, "Sleep interrupted!", ex);
        }

        invalidate(); // Force a redraw.
    }

    private void update() {
        tank1.continueMovement();
        if (tank1.getFireball() != null) tank1.getFireball().continueMovement(tanks, bricks);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        float spriteWidth = screenWidth / 8;
        float spriteHeight = screenHeight / 10;

        Bitmap defaultBrick = BitmapFactory.decodeResource(getResources(), R.drawable.default_brick);
        Bitmap damagedBrick = BitmapFactory.decodeResource(getResources(), R.drawable.damaged_brick);
        Bitmap tanksSprite = BitmapFactory.decodeResource(getResources(), R.drawable.tanks);
        Bitmap fireballSprite = BitmapFactory.decodeResource(getResources(), R.drawable.fireball);
        float tankWidth = tanksSprite.getWidth() / 8;
        float tankHeight = tanksSprite.getHeight() / 8;

        bricks = new Bricks(screenWidth, screenHeight, defaultBrick, damagedBrick);
        tank1 = new Tank(
                Bitmap.createBitmap(tanksSprite, 0, 0, (int) tankWidth, (int) tankHeight),
                new RectF(spriteWidth * 4, spriteHeight * 9, spriteWidth * 5, screenHeight),
                fireballSprite);
        tank2 = new Tank(
                Bitmap.createBitmap(tanksSprite, 0, (int) tankHeight, (int) tankWidth, (int) tankHeight),
                new RectF(spriteWidth * 4, 0, spriteWidth * 5, spriteHeight),
                fireballSprite);
        tanks = new Tank[] {tank1, tank2};
    }

    public void onSingleTap(float x, float y) {
        tank1.moveTank(x, y, tanks, bricks);
    }

    public void onFling(int direction) {
        tank1.shootFireball(direction);
    }
}
