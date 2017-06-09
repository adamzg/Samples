package com.parrot.sdksample.listener;

import android.view.MotionEvent;
import android.view.View;

import com.parrot.sdksample.drone.BebopDrone;

/**
 * Created by user on 6/8/17.
 */
public class ForwardButtonListener extends BaseButtonTouchListener implements View.OnTouchListener {
    public ForwardButtonListener(BebopDrone bebopDrone) {
        super(bebopDrone);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                getBebopDrone().setPitch((byte) 50);
                getBebopDrone().setFlag((byte) 1);
                break;

            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                getBebopDrone().setPitch((byte) 0);
                getBebopDrone().setFlag((byte) 0);
                break;

            default:

                break;
        }

        return true;
    }
}
