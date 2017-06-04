package com.parrot.sdksample.drone;

import android.content.Context;
import android.support.annotation.NonNull;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

public class BebopDrone extends BaseBebopDrone {

    public BebopDrone(Context context, @NonNull ARDiscoveryDeviceService deviceService) {
        super(context, deviceService);
    }

    public void takeOff() {
        if (hasController() && (isRunningArcController())) {
            getDroneController().sendPilotingTakeOff();
        }
    }

    public void land() {
        if ((hasController()) && isRunningArcController()) {
            getDroneController().sendPilotingLanding();
        }
    }

    public void emergency() {
        if (hasRunningController()) {
            getDroneController().sendPilotingEmergency();
        }
    }

    public void takePicture() {
        if (hasRunningController()) {
            getDroneController().sendMediaRecordPictureV2();
        }
    }

}
