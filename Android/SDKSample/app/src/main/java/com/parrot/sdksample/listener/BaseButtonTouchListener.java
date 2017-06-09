package com.parrot.sdksample.listener;

import android.view.View;

import com.parrot.sdksample.drone.BebopDrone;

/**
 * Created by user on 6/8/17.
 */

public abstract class BaseButtonTouchListener implements View.OnTouchListener {
    BebopDrone bebopDrone;

    public BaseButtonTouchListener(BebopDrone bebopDrone) {
        this.bebopDrone = bebopDrone;
    }

    public BebopDrone getBebopDrone() {
        return bebopDrone;
    }
}
