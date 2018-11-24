package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Brick {
    private Bitmap brickSprite;
    private Boolean isDamaged;
    private Rect rect;

    public Brick(Bitmap brickSprite, Rect rect) {
        this.brickSprite = brickSprite;
        this.isDamaged = false;
        this.rect = rect;
    }

    public Boolean getDamaged() {
        return isDamaged;
    }

    public void setDamaged(Boolean damaged) {
        isDamaged = damaged;
    }

    public void setBrickSprite(Bitmap brickSprite) {
        this.brickSprite = brickSprite;
    }

    public void drawOnCanvas(Canvas canvas) {
        canvas.drawBitmap(brickSprite, null, rect,null);
    }
}
