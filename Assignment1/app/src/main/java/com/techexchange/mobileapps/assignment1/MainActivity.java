package com.techexchange.mobileapps.assignment1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Grid model;
    private GridLayout gridLayout;
    private Bitmap spritesheet;
    private Bitmap empty_slot;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("gridState", model.getGrid());
        outState.putInt("holePos", model.getHole());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        if (savedInstanceState != null) {
            model.setGrid(savedInstanceState.getIntArray("gridState"));
            model.setHole(savedInstanceState.getInt("holePos"));
        } else {
            model.shuffle(15); // if there is a savedInstanceState, there is no need to reshuffle.
        }
        model.updateWholeGrid();
        model.checkState();

        for (ImageView slot : model.getSlots()) {
            slot.setOnClickListener(v -> onSlotPressed(v));
        }
    }

    protected void initVars() {
        gridLayout = findViewById(R.id.grid);
        spritesheet = BitmapFactory.decodeResource(getResources(), R.drawable.number_sprites);
        empty_slot = BitmapFactory.decodeResource(getResources(), R.drawable.empty_slot);

        model = new Grid(gridLayout, spritesheet, empty_slot);
    }

    protected void onSlotPressed(View v) {
        ImageView slot = (ImageView) v;
        int pos = Integer.parseInt((String) slot.getTag());
        if (model.canMove(pos)) {
            model.moveAndUpdateGrid(pos, false);
            if (model.checkState()) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), "You won!", duration);
                toast.show();
            }
        }
    }
}
