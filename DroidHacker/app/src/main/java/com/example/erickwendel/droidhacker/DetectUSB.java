package com.example.erickwendel.droidhacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by erickwendel on 11/26/17.
 */

public class DetectUSB extends BroadcastReceiver {

    private AudioManager audio;

    @Override
    public void onReceive(Context context, Intent intent) {

        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Intent intent1 = new Intent("usb_detect");
        context.sendBroadcast(intent1);

//        Toast.makeText(context,"USB CONECTADO",Toast.LENGTH_SHORT).show();

        final int originalVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        Uri notification =  Uri.parse("android.resource://com.example.erickwendel.droidhacker/" + R.raw.gemidao);

        MediaPlayer mp=new MediaPlayer();
        mp.setLooping(true);
        mp = MediaPlayer.create(context, notification);

        mp.start();

        Intent i = new Intent(context, Main2Activity.class);
        context.startActivity(i);

    }

}
