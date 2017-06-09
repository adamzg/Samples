package com.parrot.sdksample.listener;

import android.view.View;

import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.sdksample.drone.BebopDrone;

import static com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.*;

/**
 * Created by user on 6/8/17.
 */
public class TakeoffLandButtonListener implements View.OnClickListener {
    private BebopDrone bebopDrone;

    public TakeoffLandButtonListener(BebopDrone bebopDrone) {
        this.bebopDrone = bebopDrone;

    }

    public void onClick(View v) {
        switch (getBebopDrone().getFlyingState()) {
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                getBebopDrone().takeOff();
                break;
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                getBebopDrone().land();
                break;
            default:
        }
    }

    public BebopDrone getBebopDrone() {
        return bebopDrone;
    }
}
