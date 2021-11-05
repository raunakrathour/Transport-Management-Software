package com.groupname.tripmate;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

class ButtonHighlighterOnTouchListener implements View.OnTouchListener {

    final Button imageButton;

    public ButtonHighlighterOnTouchListener(final Button imageButton) {
        super();
        this.imageButton = imageButton;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //grey color filter, you can change the color as you like
           // imageButton.setBackgroundColor(Color.argb(155, 0, 185, 0));
            //imageButton.setBackground();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
           // imageButton.setBackground(Drawable.createFromPath("@drawable/button_pressed_1"));
        }
        return false;
    }

}
