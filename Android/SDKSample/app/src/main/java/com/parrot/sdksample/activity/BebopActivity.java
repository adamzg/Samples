package com.parrot.sdksample.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.sdksample.R;
import com.parrot.sdksample.drone.BebopDrone;
import com.parrot.sdksample.drone.BebopDroneListener;
import com.parrot.sdksample.listener.BackButtonListener;
import com.parrot.sdksample.listener.DownButtonListener;
import com.parrot.sdksample.listener.ForwardButtonListener;
import com.parrot.sdksample.listener.RollLeftButtonListener;
import com.parrot.sdksample.listener.RollRightButtonListener;
import com.parrot.sdksample.listener.TakeoffLandButtonListener;
import com.parrot.sdksample.listener.UpButtonListener;
import com.parrot.sdksample.listener.YawLeftButtonListener;
import com.parrot.sdksample.listener.YawRightButtonListener;
import com.parrot.sdksample.view.BebopVideoView;

/**
 * Controls interactions with the UI
 */
public class BebopActivity extends AppCompatActivity {
    private static final String TAG = "BebopActivity";
    BebopDrone mBebopDrone;

    ProgressDialog mConnectionProgressDialog;
    ProgressDialog mDownloadProgressDialog;

    BebopVideoView mVideoView;

    TextView mBatteryLabel;
    Button mTakeOffLandBt;
    Button mDownloadBt;

    int mNbMaxDownload;
    int mCurrentDownloadIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebop);



        Intent intent = getIntent();
        ARDiscoveryDeviceService service = intent.getParcelableExtra(DeviceListActivity.EXTRA_DEVICE_SERVICE);
        setBebopDrone(new BebopDrone(this, service));
        getBebopDrone().addListener(getBebopListener());
        initIHM();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // show a loading view while the bebop drone is connecting
        if (isConnecting())
        {
            showConnectingProress();
            // if the connection to the Bebop fails, finish the activity
            if (!getBebopDrone().connect()) {
                finish();
            }
        }
    }

    void showConnectingProress() {
        setmConnectionProgressDialog(new ProgressDialog(this, R.style.AppCompatAlertDialogStyle));
        getmConnectionProgressDialog().setIndeterminate(true);
        getmConnectionProgressDialog().setMessage("Connecting ...");
        getmConnectionProgressDialog().setCancelable(false);
        getmConnectionProgressDialog().show();
    }

    boolean isConnecting() {
        return (getBebopDrone() != null) && !(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(getBebopDrone().getConnectionState()));
    }

    @Override
    public void onBackPressed() {
        if (getBebopDrone() != null)
        {
            setmConnectionProgressDialog(new ProgressDialog(this, R.style.AppCompatAlertDialogStyle));
            getmConnectionProgressDialog().setIndeterminate(true);
            getmConnectionProgressDialog().setMessage("Disconnecting ...");
            getmConnectionProgressDialog().setCancelable(false);
            getmConnectionProgressDialog().show();

            if (!getBebopDrone().disconnect()) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        getBebopDrone().dispose();
        super.onDestroy();
    }

    void initIHM() {
        mapVideoViewButton();

        mapEmergencyButton();

        mapTakeoffLandButton((Button) findViewById(R.id.takeOffOrLandBt));

        mapTakePictureButton();

        mapDownloadButton((Button) findViewById(R.id.downloadBt));

        mapUpButton();

        mapDownButton();

        mapYawLeftButton();

        mapYawRightButton();

        mapForwardButton();

        mapBackButton();

        mapRollLeftButton();

        mapRollRightButton();

        setmBatteryLabel((TextView) findViewById(R.id.batteryLabel));
    }

    void mapRollRightButton() {
        findViewById(R.id.rollRightBt).setOnTouchListener(new RollRightButtonListener(getBebopDrone()));
    }

    void mapRollLeftButton() {
        findViewById(R.id.rollLeftBt).setOnTouchListener(new RollLeftButtonListener(getBebopDrone()));
    }

    void mapBackButton() {
        findViewById(R.id.backBt).setOnTouchListener(new BackButtonListener(getBebopDrone()));
    }

    private void mapForwardButton() {
        findViewById(R.id.forwardBt).setOnTouchListener(new ForwardButtonListener(getBebopDrone()));
    }

    void mapYawRightButton() {
        findViewById(R.id.yawRightBt).setOnTouchListener(new YawRightButtonListener(getBebopDrone()));
    }

    void mapYawLeftButton() {
        findViewById(R.id.yawLeftBt).setOnTouchListener(new YawLeftButtonListener(getBebopDrone()));
    }

    void mapDownButton() {
        findViewById(R.id.gazDownBt).setOnTouchListener(new DownButtonListener(getBebopDrone()));
    }

    void mapUpButton() {
        findViewById(R.id.gazUpBt).setOnTouchListener(new UpButtonListener(getBebopDrone()));
    }

    void mapDownloadButton(Button downloadButton) {
        setmDownloadBt(downloadButton);
        getmDownloadBt().setEnabled(false);
        getmDownloadBt().setOnClickListener(new DownloadButtonListener());
    }

    private void mapTakePictureButton() {
        findViewById(R.id.takePictureBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBebopDrone().takePicture();
            }
        });
    }

    void mapTakeoffLandButton(Button takeOffLandButton) {
        setTakeOffLandBt(takeOffLandButton);
        getTakeOffLandBt().setOnClickListener(new TakeoffLandButtonListener(getBebopDrone()));
    }

    void mapEmergencyButton() {
        findViewById(R.id.emergencyBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBebopDrone().emergency();
            }
        });
    }

    void mapVideoViewButton() {
        setVideoView((BebopVideoView) findViewById(R.id.videoView));
    }

    final BebopDroneListener mBebopListener = new UserDefinedBebopDroneListener();

    public BebopDrone getBebopDrone() {
        return mBebopDrone;
    }

    public void setBebopDrone(BebopDrone mBebopDrone) {
        this.mBebopDrone = mBebopDrone;
    }

    public ProgressDialog getmConnectionProgressDialog() {
        return mConnectionProgressDialog;
    }

    public void setmConnectionProgressDialog(ProgressDialog mConnectionProgressDialog) {
        this.mConnectionProgressDialog = mConnectionProgressDialog;
    }

    public ProgressDialog getmDownloadProgressDialog() {
        return mDownloadProgressDialog;
    }

    public void setmDownloadProgressDialog(ProgressDialog mDownloadProgressDialog) {
        this.mDownloadProgressDialog = mDownloadProgressDialog;
    }

    public BebopVideoView getVideoView() {
        return mVideoView;
    }

    public void setVideoView(BebopVideoView mVideoView) {
        this.mVideoView = mVideoView;
    }

    public TextView getmBatteryLabel() {
        return mBatteryLabel;
    }

    public void setmBatteryLabel(TextView mBatteryLabel) {
        this.mBatteryLabel = mBatteryLabel;
    }

    public Button getTakeOffLandBt() {
        return mTakeOffLandBt;
    }

    public void setTakeOffLandBt(Button mTakeOffLandBt) {
        this.mTakeOffLandBt = mTakeOffLandBt;
    }

    public Button getmDownloadBt() {
        return mDownloadBt;
    }

    public void setmDownloadBt(Button mDownloadBt) {
        this.mDownloadBt = mDownloadBt;
    }

    public int getmNbMaxDownload() {
        return mNbMaxDownload;
    }

    public void setmNbMaxDownload(int mNbMaxDownload) {
        this.mNbMaxDownload = mNbMaxDownload;
    }

    public int getmCurrentDownloadIndex() {
        return mCurrentDownloadIndex;
    }

    public void setmCurrentDownloadIndex(int mCurrentDownloadIndex) {
        this.mCurrentDownloadIndex = mCurrentDownloadIndex;
    }

    public BebopDroneListener getBebopListener() {
        return mBebopListener;
    }

    private class DownloadButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            getBebopDrone().getLastFlightMedias();

            setmDownloadProgressDialog(new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle));
            getmDownloadProgressDialog().setIndeterminate(true);
            getmDownloadProgressDialog().setMessage("Fetching medias");
            getmDownloadProgressDialog().setCancelable(false);
            getmDownloadProgressDialog().setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getBebopDrone().cancelGetLastFlightMedias();
                }
            });
            getmDownloadProgressDialog().show();
        }
    }

    public class UserDefinedBebopDroneListener implements BebopDroneListener {
        @Override
        public void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            switch (state)
            {
                case ARCONTROLLER_DEVICE_STATE_RUNNING:
                    getmConnectionProgressDialog().dismiss();
                    break;

                case ARCONTROLLER_DEVICE_STATE_STOPPED:
                    // if the deviceController is stopped, go back to the previous activity
                    getmConnectionProgressDialog().dismiss();
                    finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onBatteryChargeChanged(int batteryPercentage) {
            getmBatteryLabel().setText(String.format("%d%%", batteryPercentage));
        }

        @Override
        public void onPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            switch (state) {
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                    getTakeOffLandBt().setText("Take off");
                    getTakeOffLandBt().setEnabled(true);
                    getmDownloadBt().setEnabled(true);
                    break;
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                    getTakeOffLandBt().setText("Land");
                    getTakeOffLandBt().setEnabled(true);
                    getmDownloadBt().setEnabled(false);
                    break;
                default:
                    getTakeOffLandBt().setEnabled(false);
                    getmDownloadBt().setEnabled(false);
            }
        }

        @Override
        public void onPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            Log.i(TAG, "Picture has been taken");
        }

        @Override
        public void configureDecoder(ARControllerCodec codec) {
            getVideoView().configureDecoder(codec);
        }

        @Override
        public void onFrameReceived(ARFrame frame) {
            getVideoView().displayFrame(frame);
        }

        @Override
        public void onMatchingMediasFound(int nbMedias) {
            getmDownloadProgressDialog().dismiss();

            setmNbMaxDownload(nbMedias);
            setmCurrentDownloadIndex(1);

            if (nbMedias > 0) {
                setmDownloadProgressDialog(new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle));
                getmDownloadProgressDialog().setIndeterminate(false);
                getmDownloadProgressDialog().setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                getmDownloadProgressDialog().setMessage("Downloading medias");
                getmDownloadProgressDialog().setMax(getmNbMaxDownload() * 100);
                getmDownloadProgressDialog().setSecondaryProgress(getmCurrentDownloadIndex() * 100);
                getmDownloadProgressDialog().setProgress(0);
                getmDownloadProgressDialog().setCancelable(false);
                getmDownloadProgressDialog().setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getBebopDrone().cancelGetLastFlightMedias();
                    }
                });
                getmDownloadProgressDialog().show();
            }
        }

        @Override
        public void onDownloadProgressed(String mediaName, int progress) {
            getmDownloadProgressDialog().setProgress(((getmCurrentDownloadIndex() - 1) * 100) + progress);
        }

        @Override
        public void onDownloadComplete(String mediaName) {
            setmCurrentDownloadIndex(getmCurrentDownloadIndex() + 1);
            getmDownloadProgressDialog().setSecondaryProgress(getmCurrentDownloadIndex() * 100);

            if (getmCurrentDownloadIndex() > getmNbMaxDownload()) {
                getmDownloadProgressDialog().dismiss();
                setmDownloadProgressDialog(null);
            }
        }
    }
}
