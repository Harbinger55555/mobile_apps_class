package com.techexchange.mobileapps.assignment1;

import android.graphics.Bitmap;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Random;

public class Grid {
    private ImageView[] slots;

    private Bitmap[] activeNumSprites;
    private Bitmap[] finalNumSprites;
    private int[] grid;
    private int[] winState;
    private int hole;

    public Grid(GridLayout gridLayout, Bitmap spritesheet, Bitmap empty_slot) {
        int gridChildren = gridLayout.getChildCount();
        slots = new ImageView[gridChildren];
        for (int slot = 0; slot < gridLayout.getChildCount(); ++slot) {
            slots[slot] = (ImageView) gridLayout.getChildAt(slot);
        }

        grid = new int[]{0, 1, 2,
                3, 4, 5,
                6, 7, 8};;
        winState = new int[]{1, 2, 3,
                4, 5, 6,
                7, 8, 0};
        hole = 0;

        int ssWidth = spritesheet.getWidth() / 10;
        int ssHeight = spritesheet.getHeight() / 3;
        int thirdRow = 2 * ssHeight;
        activeNumSprites = new Bitmap[9];
        finalNumSprites = new Bitmap[9];
        activeNumSprites[0] = empty_slot;
        finalNumSprites[8] = empty_slot;
        // Only using blue number sprites from 1 to 8
        for (int i = 1; i < 9; ++i) {
            activeNumSprites[i] = Bitmap.createBitmap(spritesheet, ssWidth * i, 0, ssWidth, ssHeight);
        }
        // Only using green number sprites from 1 to 8
        for (int i = 1; i < 9; ++i) {
            finalNumSprites[i - 1] = Bitmap.createBitmap(spritesheet, ssWidth * i, thirdRow, ssWidth, ssHeight);
        }
    }

    public int[] getGrid() {
        return grid;
    }

    public int getHole() {
        return hole;
    }

    public ImageView[] getSlots() {
        return slots;
    }

    public void setGrid(int[] grid) {
        this.grid = grid;
    }

    public void setHole(int hole) {
        this.hole = hole;
    }

    protected void convertToWinState() {
        for (int i = 0; i < 9; ++i) {
            slots[i].setImageBitmap(finalNumSprites[i]);
        }
    }

    protected void shuffle(int moves) {
        Random rand = new Random();
        int[] neighborOffsets = {-3, +3, -1, +1}; // up down left right
        while (moves-- > 0) {
            int neighbor;
            do {
                neighbor = hole + neighborOffsets[rand.nextInt(4)];
            } while (!canMove(neighbor));
            moveAndUpdateGrid(neighbor, true);
        }
    }

    protected boolean canMove(int pos) {
        if (pos < 0 || pos >= 3 * 3) {
            return false;                   // No such position
        }
        int diff = hole - pos;
        if (diff == -1) {                   // Slide tile left (hole goes right)
            return pos % 3 != 0;         // Unless tile is on left edge
        } else if (diff == +1) {            // Slide tile right (hole goes left)
            return hole % 3 != 0;   // Unless hole is on left edge
        } else {
            return Math.abs(diff) == 3;  // Slide vertically
        }
    }

    protected void moveAndUpdateGrid(int pos, boolean shuffling) {
        if (canMove(pos)) {
            grid[hole] = grid[pos];
            slots[hole].setImageBitmap(activeNumSprites[grid[pos]]);
            grid[hole = pos] = 0;
            slots[pos].setImageBitmap(activeNumSprites[0]);
            if (!shuffling) {
                checkState();
            }
        }
    }

    protected void updateWholeGrid() {
        for (int gridIndex = 0; gridIndex < 9; ++gridIndex) {
            slots[gridIndex].setImageBitmap(activeNumSprites[grid[gridIndex]]);
        }
    }

    protected boolean checkState() {
        if (Arrays.equals(grid, winState)) {
            convertToWinState();
            for (ImageView slot : slots) {
                slot.setEnabled(false);
            }
            return true;
        }
        return false;
    }
}
