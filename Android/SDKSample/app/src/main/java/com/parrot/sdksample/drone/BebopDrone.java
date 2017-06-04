package com.parrot.sdksample.drone;

import android.content.Context;
import android.support.annotation.NonNull;

import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

public class BebopDrone extends BaseBebopDrone {

    public BebopDrone(Context context, @NonNull ARDiscoveryDeviceService deviceService) {
        super(context, deviceService);


    }

    //region Listener functions
    public void addListener(BebopDroneListener listener) {
        getmListeners().add(listener);
    }

    public void removeListener(BebopDroneListener listener) {
        getmListeners().remove(listener);
    }
    //endregion Listener

    /**
     * Connect to the drone
     * @return true if operation was successful.
     *              Returning true doesn't mean that device is connected.
     *              You can be informed of the actual connection through {@link BebopDroneListener#onDroneConnectionChanged}
     */
    public boolean connect() {
        boolean success = false;
        if ((getmDeviceController() != null) && (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED.equals(getmState()))) {
            ARCONTROLLER_ERROR_ENUM error = getmDeviceController().start();
            if (error == ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK) {
                success = true;
            }
        }
        return success;
    }

    /**
     * Disconnect from the drone
     * @return true if operation was successful.
     *              Returning true doesn't mean that device is disconnected.
     *              You can be informed of the actual disconnection through {@link BebopDroneListener#onDroneConnectionChanged}
     */
    public boolean disconnect() {
        boolean success = false;
        if ((getmDeviceController() != null) && (ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(getmState()))) {
            ARCONTROLLER_ERROR_ENUM error = getmDeviceController().stop();
            if (error == ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK) {
                success = true;
            }
        }
        return success;
    }

    /**
     * Get the current connection state
     * @return the connection state of the drone
     */
    public ARCONTROLLER_DEVICE_STATE_ENUM getConnectionState() {
        return getmState();
    }

    /**
     * Get the current flying state
     * @return the flying state
     */
    public ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM getFlyingState() {
        return getmFlyingState();
    }

    public void takeOff() {
        if ((getmDeviceController() != null) && (getmState().equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            getmDeviceController().getFeatureARDrone3().sendPilotingTakeOff();
        }
    }

    public void land() {
        if ((getmDeviceController() != null) && (getmState().equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            getmDeviceController().getFeatureARDrone3().sendPilotingLanding();
        }
    }

    public void emergency() {
        if ((getmDeviceController() != null) && (getmState().equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            getmDeviceController().getFeatureARDrone3().sendPilotingEmergency();
        }
    }

    public void takePicture() {
        if ((getmDeviceController() != null) && (getmState().equals(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING))) {
            getmDeviceController().getFeatureARDrone3().sendMediaRecordPictureV2();
        }
    }

    //endregion notify listener block

}
