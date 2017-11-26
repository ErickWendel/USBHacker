package com.example.erickwendel.droidhacker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


public class Main2Activity extends AppCompatActivity {

    private WindowManager.LayoutParams layout;
    private int value = 0;
    private int value2 = 0;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private AudioManager audio;
    Camera.Parameters params;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        layout = getWindow().getAttributes();
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                if(value == 1) {
                    layout.screenBrightness = 0F;
                    value = 0;
                } else {
                    layout.screenBrightness = 1F;
                    value =1;
                }

                getWindow().setAttributes(layout);

                h.postDelayed(this, 1000);
            }
        }, 1000);

        final Handler hx = new Handler();
        hx.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                if(value2 == 1) {
                    value2 = 0;
                    turnOnFlash();
                } else {
                    value2 =1;
                    turnOffFlash();
                }

                getWindow().setAttributes(layout);

                hx.postDelayed(this, 100);
            }
        }, 100);

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(Main2Activity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        // get the camera
        getCamera();



    }

    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
//                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }


    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

        }

    }


    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
