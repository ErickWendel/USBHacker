package com.example.erickwendel.droidhacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private AudioManager audio;
    private Button volumeDown;
    private Button volumeUp;
    private Button image;
    private Button music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        volumeDown = (Button) findViewById(R.id.volumeDown);
//        volumeUp = (Button) findViewById(R.id.volumeUp);
//        image = (Button) findViewById(R.id.image);
//        music = (Button) findViewById(R.id.music);
//        volumeDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("Click", "click");
//
//
//            }
//
//        });

        finish();

    }



    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("usb_detect")) {

//                Toast.makeText(getApplicationContext(),"USB CONECTADO",Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast_reciever, new IntentFilter("usb_detect"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(broadcast_reciever, new IntentFilter("usb_detect"));
    }

    public boolean isConnected() {
        Intent intent = registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }
}
