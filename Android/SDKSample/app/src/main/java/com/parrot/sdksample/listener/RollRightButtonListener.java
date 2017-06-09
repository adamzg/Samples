package com.parrot.sdksample.listener;

import android.view.MotionEvent;
import android.view.View;

import com.parrot.sdksample.drone.BebopDrone;

/**
 * Created by user on 6/8/17.
 */
public class RollRightButtonListener extends BaseButtonTouchListener {

    public RollRightButtonListener(BebopDrone bebopDrone) {
        super(bebopDrone);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        BebopDrone bebopDrone = getBebopDrone();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                bebopDrone.setRoll((byte) 50);
                bebopDrone.setFlag((byte) 1);
                break;

            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                bebopDrone.setRoll((byte) 0);
                bebopDrone.setFlag((byte) 0);
                break;

            default:

                break;
        }

        return true;
    }

}
