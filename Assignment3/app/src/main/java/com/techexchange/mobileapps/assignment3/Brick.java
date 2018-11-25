package com.techexchange.mobileapps.assignment3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Brick {
    private Bitmap brickSprite;
    private Boolean isDamaged;
    private RectF rectF;

    public Brick(Bitmap brickSprite, RectF rectF) {
        this.brickSprite = brickSprite;
        this.isDamaged = false;
        this.rectF = rectF;
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

    public RectF getRectF() { return rectF; }

    public void drawOnCanvas(Canvas canvas) {
        canvas.drawBitmap(brickSprite, null, rectF,null);
    }
}
