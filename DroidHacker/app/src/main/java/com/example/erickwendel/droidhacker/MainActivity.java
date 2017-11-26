package com.example.erickwendel.droidhacker;

import android.content.Context;
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
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumeDown = (Button) findViewById(R.id.volumeDown);
        volumeUp = (Button) findViewById(R.id.volumeUp);
        image = (Button) findViewById(R.id.image);
        music = (Button) findViewById(R.id.music);
        volumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click", "click");
                final int originalVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                Uri notification =  Uri.parse("android.resource://com.example.erickwendel.droidhacker/" + R.raw.gemidao);

                MediaPlayer mp=new MediaPlayer();
                mp.setLooping(true);
                mp = MediaPlayer.create(MainActivity.this, notification);

                mp.start();

            }

        });

//    private void playMusic() {
//        MediaPlayer mp = new MediaPlayer();
//
////        try {
////            mp.setDataSource(path + File.separator + fileName);
////            mp.prepare();
////            mp.start();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        }
//    }


    }

}
