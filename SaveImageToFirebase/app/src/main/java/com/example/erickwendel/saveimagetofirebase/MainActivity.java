package com.example.erickwendel.saveimagetofirebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private String dir;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        Button capture = (Button) findViewById(R.id.btnCamera);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePhoto();
//                count++;
//                String file = dir + count + ".jpg";
//                File newfile = new File(file);
//                try {
//                    newfile.createNewFile();
//                } catch (IOException e) {
//                }
//
//                Uri outputFileUri = FileProvider.getUriForFile(MainActivity.this,
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        newfile);
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageLabel.setImageBitmap(imageBitmap);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//            Log.e("img", imageEncoded);
//            DatabaseReference ref = FirebaseDatabase.getInstance()
//                    .getReference(SyncStateContract.Constants.FIREBASE_CHILD_RESTAURANTS)
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .child(mRestaurant.getPushId())
//                    .child("imageUrl");
//            ref.setValue(imageEncoded);
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
                SurfaceTexture dummySurfaceTextureF = new SurfaceTexture(0);
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
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        ImageView image = findViewById(R.id.image);
                        image.setImageBitmap(bitmap);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                        camerax.release();

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://saveimagetofirebase.appspot.com");
                        StorageReference mountainsRef = storageRef.child("mountains.jpg");

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
